package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    private float fov;
    @Shadow
    private float oldFov;

    @Inject(at = @At("HEAD"), method = "getNightVisionScale", cancellable = true)
    private static void getNightVisionStrength(LivingEntity entity, float f, CallbackInfoReturnable<Float> cir) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.ILLUMINATING.get(), entity.getUseItem());
        if (((Player) entity).isScoping() && i > 0) {
            cir.setReturnValue(1.0F);
        }
    }

    @Inject(at = @At("RETURN"), method = "getFov", cancellable = true)
    public void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        Minecraft minecraftClient = Minecraft.getInstance();
        LocalPlayer player = minecraftClient.player;
        if (player != null) {
            if (minecraftClient.options.getCameraType().isFirstPerson()) {
                int i = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.SCRUTINY.get(), player.getUseItem());
                if (player.isScoping() && i > 0) {
                    double defaultValue = 10.0D;
                    int zoomValue = ((ScrutinyAccess) minecraftClient.mouseHandler).getZoom();
                    double lerpValue = Mth.lerp(defaultValue, this.fov, this.oldFov);
                    double finalValue = Mth.ceil(lerpValue) + 9.0D;
                    finalValue -= zoomValue;
                    cir.setReturnValue(finalValue);
                }
            }
        }
    }

}
