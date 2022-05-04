package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class PlayerInventoryMixin {

	@Inject(at = @At("HEAD"), method = "swapPaint", cancellable = true)
	public void swapPaint(double scrollAmount, CallbackInfo ci) {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player != null) {
			int i = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.SCRUTINY.get(), player.getUseItem());
			if (player.isScoping() && i > 0) {
				ci.cancel();
			}
		}
	}

}
