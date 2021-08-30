package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "isUsingSpyglass", cancellable = true)
    private void usingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.isUsingItem() && (this.getActiveItem().isOf(Items.SPYGLASS) || this.getActiveItem().isOf(SpyglassPlusItems.BINOCULARS)));
    }
}