package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin implements ScrutinyAccess {
    private int zoom;

	@Inject(at = @At("HEAD"), method = "onScroll")
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		Minecraft minecraftClient = Minecraft.getInstance();
		LocalPlayer player = minecraftClient.player;
		if (player != null) {
			int i = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.SCRUTINY.get(), player.getUseItem());
			if (player.isScoping() && i > 0) {
				if (vertical > 0.0D) {
					if (this.zoom <= EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.SCRUTINY.get(), player.getUseItem()) * 2) {
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
