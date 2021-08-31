package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin implements ScrutinyAccess {
    private int zoom;

    @Inject(at = @At("HEAD"), method = "onMouseScroll")
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraftClient.player;
        if (player != null) {
            int i = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, player.getActiveItem());
            if (player.isUsingSpyglass() && i > 0) {
                if (vertical > 0.0D) {
                    if (this.zoom <= EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, player.getActiveItem()) * 2) {
                        this.zoom++;
                    }
                } else if (vertical < 0.0D) {
                    if (this.zoom > 0) {
                        this.zoom--;
                    }
                }
            }
        }
    }

    @Override
    public void addZoom(int zoom) {
        this.zoom += zoom;
    }

    @Override
    public void setZero() {
        this.zoom *= 0;
    }

    @Override
    public int getZoom() {
        return this.zoom;
    }
}
