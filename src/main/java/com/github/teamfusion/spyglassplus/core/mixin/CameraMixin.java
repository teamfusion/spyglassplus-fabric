package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

	@Inject(method = "setup", at = @At("TAIL"))
	public void setup(BlockGetter p_90576_, Entity p_90577_, boolean p_90578_, boolean p_90579_, float p_90580_, CallbackInfo callbackInfo) {
		if (p_90577_ != Minecraft.getInstance().player && Minecraft.getInstance().player.isScoping()) {
			ISpyable spyable = ((ISpyable) Minecraft.getInstance().player);
			this.setRotation(p_90577_.getViewYRot(p_90580_) + spyable.getCameraRotY(), p_90577_.getViewXRot(p_90580_) + spyable.getCameraRotX());
		}
	}

	@Shadow
	public Entity getEntity() {
		return null;
	}

	@Shadow
	protected abstract void setRotation(float p_90573_, float p_90574_);
}
