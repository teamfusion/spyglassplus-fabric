package com.github.teamfusion.spyglassplus.mixin;

import com.github.teamfusion.spyglassplus.api.tag.SpyglassPlusItemTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "isUsingSpyglass", at = @At("RETURN"), cancellable = true)
    private void onIsUsingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            if (this.getActiveItem().isIn(SpyglassPlusItemTags.SCOPING_ITEMS)) cir.setReturnValue(true);
        }
    }
}
