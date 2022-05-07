package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ISpyable {
    private static final EntityDataAccessor<Boolean> DATA_COMMAND_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    protected void defineSynchedData(CallbackInfo callbackInfo) {
        this.entityData.define(DATA_COMMAND_ID, false);
    }

    @Inject(method = "isScoping", at = @At("RETURN"), cancellable = true)
    private void usingSpyglass(CallbackInfoReturnable<Boolean> cir) {
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
}
