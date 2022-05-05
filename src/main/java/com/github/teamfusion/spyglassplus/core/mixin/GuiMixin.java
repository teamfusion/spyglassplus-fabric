package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
	private static final ResourceLocation BINOCULARS_SCOPE_LOCATION = new ResourceLocation(SpyglassPlus.MOD_ID, "textures/misc/binoculars_scope.png");

	@Shadow
	@Final
	protected Minecraft minecraft;

	@Shadow
	protected int screenWidth;
	@Shadow
	protected int screenHeight;


	@Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
	protected void renderSpyglassOverlay(float p_168676_, CallbackInfo ci) {
		if (this.minecraft.player.getUseItem().is(SpyglassPlusItems.BINOCULARS.get())) {
			RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.defaultBlendFunc();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, BINOCULARS_SCOPE_LOCATION);
			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder bufferbuilder = tesselator.getBuilder();
			float f = (float) Math.min(this.screenWidth, this.screenHeight);
			float f1 = Math.min((float) this.screenWidth / f, (float) this.screenHeight / f) * p_168676_;
			float f2 = f * f1;
			float f3 = f * f1;
			float f4 = ((float) this.screenWidth - f2) / 2.0F;
			float f5 = ((float) this.screenHeight - f3) / 2.0F;
			float f6 = f4 + f2;
			float f7 = f5 + f3;
			bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			bufferbuilder.vertex((double) f4, (double) f7, -90.0D).uv(0.0F, 1.0F).endVertex();
			bufferbuilder.vertex((double) f6, (double) f7, -90.0D).uv(1.0F, 1.0F).endVertex();
			bufferbuilder.vertex((double) f6, (double) f5, -90.0D).uv(1.0F, 0.0F).endVertex();
			bufferbuilder.vertex((double) f4, (double) f5, -90.0D).uv(0.0F, 0.0F).endVertex();
			tesselator.end();
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			RenderSystem.disableTexture();
			bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			bufferbuilder.vertex(0.0D, (double) this.screenHeight, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) this.screenWidth, (double) this.screenHeight, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) this.screenWidth, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(0.0D, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(0.0D, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) this.screenWidth, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) this.screenWidth, 0.0D, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(0.0D, 0.0D, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(0.0D, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) f4, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) f4, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex(0.0D, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) f6, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) this.screenWidth, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) this.screenWidth, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.vertex((double) f6, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
			tesselator.end();
			RenderSystem.enableTexture();
			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			ci.cancel();
		}
	}
}
