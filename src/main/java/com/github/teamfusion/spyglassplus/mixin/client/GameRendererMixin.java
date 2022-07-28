package com.github.teamfusion.spyglassplus.mixin.client;

import com.github.teamfusion.spyglassplus.api.enchantment.SpyglassPlusEnchantments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private float fovMultiplier;

    /**
     * Modifies {@link #fovMultiplier} for the {@link SpyglassPlusEnchantments#SCRUTINY scrutiny enchantment}.
     */
    @Inject(method = "updateFovMultiplier", at = @At("TAIL"))
    private void modifyFovMultiplierForScrutiny(CallbackInfo ci) {
        if (this.client.getCameraEntity() instanceof AbstractClientPlayerEntity player) {
            ItemStack stack = player.getActiveItem();
            int level = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, stack);
            if (level > 0) this.fovMultiplier *= 0.4F / level;
        }
    }
}
