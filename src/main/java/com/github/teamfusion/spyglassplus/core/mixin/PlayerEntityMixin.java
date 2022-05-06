package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ISpyable {
    private boolean command;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "isScoping", at = @At("RETURN"), cancellable = true)
    private void usingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && this.isUsingItem() && this.getUseItem().is(SpyglassPlusItems.BINOCULARS.get())) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void setCommand(boolean command) {
        this.command = command;
    }

    @Override
    public boolean isCommand() {
        return this.command;
    }
}
