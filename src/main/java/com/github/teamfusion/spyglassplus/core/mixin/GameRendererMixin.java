package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at = @At("HEAD"), method = "getNightVisionStrength", cancellable = true)
    private static void getNightVisionStrength(LivingEntity entity, float f, CallbackInfoReturnable<Float> cir) {
        int i = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.ILLUMINATING, entity.getActiveItem());
        if (((PlayerEntity) entity).isUsingSpyglass() && i > 0) {
            cir.setReturnValue(1.0F);
        }
    }

}
