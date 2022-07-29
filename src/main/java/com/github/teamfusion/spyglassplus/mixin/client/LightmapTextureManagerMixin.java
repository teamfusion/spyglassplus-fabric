package com.github.teamfusion.spyglassplus.mixin.client;

import com.github.teamfusion.spyglassplus.api.enchantment.SpyglassPlusEnchantments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Unique private static long lastOpenedSpyglassAt;

    /**
     * Modifies brightness for the {@link SpyglassPlusEnchantments#ILLUMINATE illuminate enchantment}.
     */
    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    private static void modifyBrightnessForIlluminate(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!client.options.getPerspective().isFirstPerson()) return;

        if (client.getCameraEntity() instanceof AbstractClientPlayerEntity player) {
            ItemStack stack = player.getActiveItem();
            long time = Util.getMeasuringTimeMs();
            if (EnchantmentHelper.getLevel(SpyglassPlusEnchantments.ILLUMINATE, stack) > 0) {
                long diff = time - lastOpenedSpyglassAt;
                cir.setReturnValue(diff / 1000f);
            } else lastOpenedSpyglassAt = time;
        }
    }
}
