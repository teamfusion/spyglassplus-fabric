package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.entity.SpyglassStandEntity;
import com.github.teamfusion.spyglassplus.common.message.SpyglassCameraRotateMessage;
import com.github.teamfusion.spyglassplus.core.ISpyable;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ISpyable {
    private static final EntityDataAccessor<Boolean> DATA_COMMAND_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_COMMAND_TICK_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<OptionalInt> DATA_SPYGLASS_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);

    private float cameraRotX;
    private float cameraRotY;


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    protected void defineSynchedData(CallbackInfo callbackInfo) {
        this.entityData.define(DATA_COMMAND_ID, false);
        this.entityData.define(DATA_COMMAND_TICK_ID, 0);
        this.entityData.define(DATA_SPYGLASS_ID, OptionalInt.empty());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    protected void tick(CallbackInfo callbackInfo) {
        if (this.getSpyGlassStands() != null) {
            this.getSpyGlassStands().getSpyGlass().onUseTick(this.level, this, 0);
            this.getSpyGlassStands().getSpyGlass().inventoryTick(this.level, this, 0, false);
        }
    }

    @Inject(method = "isScoping", at = @At("RETURN"), cancellable = true)
    private void usingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && this.getSpyGlassStands() != null) {
            cir.setReturnValue(true);
        }

        if (!cir.getReturnValueZ() && this.isUsingItem() && this.getUseItem().is(SpyglassPlusItems.BINOCULARS.get())) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void setCommand(boolean command) {
        this.entityData.set(DATA_COMMAND_ID, command);
    }

    @Override
    public boolean isCommand() {
        return this.entityData.get(DATA_COMMAND_ID);
    }

    @Override
    public void setCommandTick(int tick) {
        this.entityData.set(DATA_COMMAND_TICK_ID, tick);
    }

    @Override
    public int getCommandTick() {
        return this.entityData.get(DATA_COMMAND_TICK_ID);
    }

    @Override
    public void setSpyglassStands(SpyglassStandEntity entity) {
        if (entity == null) {
            this.entityData.set(DATA_SPYGLASS_ID, OptionalInt.empty());
        } else {
            this.entityData.set(DATA_SPYGLASS_ID, OptionalInt.of(entity.getId()));
        }
    }

    @Override
    public SpyglassStandEntity getSpyGlassStands() {
        OptionalInt optionalInt = this.entityData.get(DATA_SPYGLASS_ID);
        if (optionalInt.isPresent()) {
            Entity entity = this.level.getEntity(optionalInt.getAsInt());
            return entity instanceof SpyglassStandEntity ? (SpyglassStandEntity) entity : null;
        }
        return null;
    }

    @Override
    public void setCameraRotX(float cameraRotX) {
        this.cameraRotX = cameraRotX;
    }

    @Override
    public void setCameraRotY(float cameraRotY) {
        this.cameraRotY = cameraRotY;
    }

    @Override
    public float getCameraRotX() {
        return cameraRotX;
    }

    @Override
    public float getCameraRotY() {
        return cameraRotY;
    }

    @Override
    public void turn(double p_19885_, double p_19886_) {
        float f = (float) p_19886_ * 0.15F;
        float f1 = (float) p_19885_ * 0.15F;

        if (this.getSpyGlassStands() != null) {
            SpyglassStandEntity spyglassStand = this.getSpyGlassStands();
            this.setCameraRotX(this.getCameraRotX() + f);
            this.setCameraRotY(this.getCameraRotY() + f1);
            this.setCameraRotX(Mth.clamp(this.getCameraRotX(), -90.0F, 90.0F));
            this.setCameraRotY(Mth.clamp(this.getCameraRotY(), -90.0F, 90.0F));
            SpyglassPlus.CHANNEL.sendToServer(new SpyglassCameraRotateMessage(this.getId(), this.getCameraRotX(), this.getCameraRotY()));
        } else {
            super.turn(p_19885_, p_19886_);
        }
    }
}
