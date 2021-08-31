package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Inject(at = @At("HEAD"), method = "scrollInHotbar", cancellable = true)
    public void scrollInHotbar(double scrollAmount, CallbackInfo ci) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraftClient.player;
        if (player != null) {
            int i = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, player.getActiveItem());
            if (player.isUsingSpyglass() && i > 0) {
                ci.cancel();
            }
        }
    }

}
