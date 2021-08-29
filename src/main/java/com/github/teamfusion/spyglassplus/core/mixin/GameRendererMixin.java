package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Shadow private float lastMovementFovMultiplier;

    @Shadow private float movementFovMultiplier;

    @Inject(at = @At("RETURN"), method = "getFov", cancellable = true)
    public void getFOV(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraftClient.player;
        if (player != null) {
            int i = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, player.getActiveItem());
            if (player.isUsingSpyglass() && i > 0) {
                double value = 6D - i;
                double lerpvalue = MathHelper.lerp(value, this.movementFovMultiplier, this.lastMovementFovMultiplier);
                lerpvalue += 8.0D + (i - 1.0D) * -1.2D;
                cir.setReturnValue((double) MathHelper.ceil(lerpvalue));
            }
        }
    }

}
