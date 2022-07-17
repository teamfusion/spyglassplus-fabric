package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.SmoothDouble;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseMixin implements ScrutinyAccess {
	private int zoom;
	@Shadow
	@Final
	private Minecraft minecraft;
	@Shadow
	private final SmoothDouble smoothTurnX = new SmoothDouble();
	@Shadow
	private final SmoothDouble smoothTurnY = new SmoothDouble();
	@Shadow
	private double accumulatedDX;
	@Shadow
	private double accumulatedDY;
	@Shadow
	private double lastMouseEventTime = Double.MIN_VALUE;

	@Inject(at = @At("HEAD"), method = "onScroll")
	public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		Minecraft minecraftClient = Minecraft.getInstance();
		LocalPlayer player = minecraftClient.player;
		if (player != null) {
			boolean flag = ((ISpyable) player).getSpyGlassStands() != null && !((ISpyable) player).getSpyGlassStands().getSpyGlass().isEmpty();
			ItemStack itemstack = flag ? ((ISpyable) player).getSpyGlassStands().getSpyGlass() : player.getUseItem();

			int i = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.SCRUTINY.get(), itemstack);
			if (player.isScoping() && i > 0) {
				if (vertical > 0.0D) {
					if (this.zoom <= EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.SCRUTINY.get(), itemstack) * 2) {
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

	@Shadow
	abstract boolean isMouseGrabbed();


	/*@Inject(at = @At("HEAD"), method = "onMove")
	private void onMove(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (window == Minecraft.getInstance().getWindow().getWindow()) {
			Minecraft minecraftClient = Minecraft.getInstance();
			LocalPlayer player = minecraftClient.player;
			if (player != null) {
				if (Minecraft.getInstance().player.isScoping() && Minecraft.getInstance().player instanceof ISpyable) {
					if (((ISpyable) Minecraft.getInstance().player).getSpyGlassStands() != null) {
						ci.cancel();
					}
				}
			}
		}
	}*/

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
