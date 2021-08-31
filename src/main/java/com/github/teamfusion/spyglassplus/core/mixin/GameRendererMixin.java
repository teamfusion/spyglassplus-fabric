package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
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

    @Shadow private float lastMovementFovMultiplier;

    @Shadow private float movementFovMultiplier;

    @Inject(at = @At("HEAD"), method = "getNightVisionStrength", cancellable = true)
    private static void getNightVisionStrength(LivingEntity entity, float f, CallbackInfoReturnable<Float> cir) {
        int i = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.ILLUMINATING, entity.getActiveItem());
        if (((PlayerEntity) entity).isUsingSpyglass() && i > 0) {
            cir.setReturnValue(1.0F);
        }
    }

    @Inject(at = @At("RETURN"), method = "getFov", cancellable = true)
    public void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraftClient.player;
        if (player != null) {
            if (minecraftClient.options.getPerspective().isFirstPerson()) {
                int i = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, player.getActiveItem());
                if (player.isUsingSpyglass() && i > 0) {
                    double defaultValue = 10.0D;
                    int zoomValue = ((ScrutinyAccess)minecraftClient.mouse).getZoom();
                    double lerpValue = MathHelper.lerp(defaultValue, this.movementFovMultiplier, this.lastMovementFovMultiplier);
                    double finalValue = MathHelper.ceil(lerpValue) + 9.0D;
                    finalValue -= zoomValue;
                    cir.setReturnValue(finalValue);
                }
            }
        }
    }

}
