package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import com.mojang.blaze3d.Blaze3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
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
public class MouseMixin implements ScrutinyAccess {
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

	@Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
	public void turnPlayer(CallbackInfo callbackInfo) {
		double d0 = Blaze3D.getTime();
		double d1 = d0 - this.lastMouseEventTime;
		if (this.isMouseGrabbed() && this.minecraft.isWindowActive() && ((ISpyable) this.minecraft.player).getSpyGlassStands() != null) {
			double d4 = this.minecraft.options.sensitivity * (double) 0.6F + (double) 0.2F;
			double d5 = d4 * d4 * d4;
			double d6 = d5 * 8.0D;
			double d2;
			double d3;
			if (this.minecraft.options.getCameraType().isFirstPerson() && this.minecraft.player.isScoping()) {
				this.smoothTurnX.reset();
				this.smoothTurnY.reset();
				d2 = this.accumulatedDX * d5;
				d3 = this.accumulatedDY * d5;
			} else {
				this.smoothTurnX.reset();
				this.smoothTurnY.reset();
				d2 = this.accumulatedDX * d6;
				d3 = this.accumulatedDY * d6;
			}

			this.accumulatedDX = 0.0D;
			this.accumulatedDY = 0.0D;
			int i = 1;
			if (this.minecraft.options.invertYMouse) {
				i = -1;
			}

			this.minecraft.getTutorial().onMouse(d2, d3);
			if (this.minecraft.player != null) {
				turn((ISpyable) this.minecraft.player, d2, d3 * (double) i);
			}
			callbackInfo.cancel();
		}
	}

	@Shadow
	private boolean isMouseGrabbed() {
		return false;
	}

	public void turn(ISpyable spyable, double p_19885_, double p_19886_) {
		float f = (float) p_19886_ * 0.15F;
		float f1 = (float) p_19885_ * 0.15F;
		spyable.setCameraRotX(spyable.getCameraRotX() + f);
		spyable.setCameraRotY(spyable.getCameraRotY() + f1);
		spyable.setCameraRotX(Mth.clamp(spyable.getCameraRotX(), -90.0F, 90.0F));
		spyable.setCameraRotY(Mth.clamp(spyable.getCameraRotY(), -90.0F, 90.0F));
	}

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
