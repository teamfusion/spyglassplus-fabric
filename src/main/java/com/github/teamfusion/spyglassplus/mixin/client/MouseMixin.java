package com.github.teamfusion.spyglassplus.mixin.client;

import com.github.teamfusion.spyglassplus.api.enchantment.SpyglassPlusEnchantments;
import com.github.teamfusion.spyglassplus.api.entity.ScopingEntity;
import com.github.teamfusion.spyglassplus.api.item.ISpyglass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private double eventDeltaWheel;

    /**
     * Updates the local scrutiny level based on client scrolling.
     */
    @Inject(
        method = "onMouseScroll",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/Mouse;eventDeltaWheel:D",
            ordinal = 7
        ),
        cancellable = true
    )
    private void handleZoomScrolling(CallbackInfo ci) {
        ClientPlayerEntity player = this.client.player;

        Entity camera = this.client.getCameraEntity();
        if (camera != player && camera instanceof ScopingEntity scoping && scoping.isScoping()) ci.cancel();

        if (!this.client.options.getPerspective().isFirstPerson() || this.eventDeltaWheel == 0) return;

        ItemStack stack = player.getScopingStack();
        if (!(stack.getItem() instanceof ISpyglass item)) return;

        int level = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, stack);
        if (level > 0) {
            int before = ISpyglass.getLocalScrutinyLevel(stack);
            int delta = (int) this.eventDeltaWheel;
            if (item.adjustScrutiny(stack, level, delta) != before) {
                ClientPlayNetworking.send(ISpyglass.UPDATE_LOCAL_SCRUTINY_PACKET, Util.make(PacketByteBufs.create(), buf -> buf.writeInt(delta)));
                player.playSound(item.getAdjustSound(), 1.0F, 1.0F);
                this.eventDeltaWheel = 0;
            }

            ci.cancel();
        }
    }

    /**
     * Resets the local scrutiny level on middle mouse click.
     */
    @Inject(
        method = "onMouseButton",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V"
        ),
        cancellable = true
    )
    private void resetOnMiddleMouseClick(long window, int button, int action, int mods, CallbackInfo ci) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW.GLFW_PRESS) {
            if (!this.client.options.getPerspective().isFirstPerson()) return;

            ItemStack stack = this.client.getCameraEntity() instanceof ScopingEntity scoping ? scoping.getScopingStack() : ItemStack.EMPTY;
            if (!(stack.getItem() instanceof ISpyglass item)) return;

            int level = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, stack);
            if (level > 0) {
                int before = ISpyglass.getLocalScrutinyLevel(stack);
                if (item.adjustScrutiny(stack, level, 0) != before) {
                    ClientPlayNetworking.send(ISpyglass.UPDATE_LOCAL_SCRUTINY_PACKET, Util.make(PacketByteBufs.create(), buf -> buf.writeInt(0)));
                    this.client.player.playSound(item.getResetAdjustSound(), 1.0F, 1.0F);
                }

                ci.cancel();
            }
        }
    }
}
