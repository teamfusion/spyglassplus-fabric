package com.github.teamfusion.spyglassplus.client.render;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.client.ModModelLayer;
import com.github.teamfusion.spyglassplus.client.model.SmallSpyglassStandModel;
import com.github.teamfusion.spyglassplus.client.model.SpyglassStandBaseModel;
import com.github.teamfusion.spyglassplus.client.model.SpyglassStandModel;
import com.github.teamfusion.spyglassplus.common.entity.SpyglassStandEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SpyglassStandRender<T extends SpyglassStandEntity> extends EntityRenderer<T> {
	private static final ResourceLocation SPYGLASS_STAND_LOCATION = new ResourceLocation(SpyglassPlus.MOD_ID, "textures/entity/spyglass_stand.png");

	protected final SpyglassStandModel<T> model;
	protected final SmallSpyglassStandModel<T> small_model;

	public SpyglassStandRender(EntityRendererProvider.Context p_174008_) {
		super(p_174008_);
		this.model = new SpyglassStandModel<>(p_174008_.bakeLayer(ModModelLayer.SPYGLASS_STAND));
		this.small_model = new SmallSpyglassStandModel<>(p_174008_.bakeLayer(ModModelLayer.SMALL_SPYGLASS_STAND));
	}

	@Override
	public void render(T entity, float p_115419_, float tick, PoseStack posestack, MultiBufferSource buffersource, int p_115423_) {
		super.render(entity, p_115419_, tick, posestack, buffersource, p_115423_);
		posestack.pushPose();
		long i = (long) entity.getId() * 493286711L;
		i = i * i * 4392167121L + i * 98761L;
		float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		posestack.translate((double) f, (double) f1, (double) f2);
		double d0 = Mth.lerp((double) tick, entity.xOld, entity.getX());
		double d1 = Mth.lerp((double) tick, entity.yOld, entity.getY());
		double d2 = Mth.lerp((double) tick, entity.zOld, entity.getZ());

		//posestack.translate(0.0D, 0.375D, 0.0D);
		//posestack.mulPose(Vector3f.YP.rotationDegrees(180.0F - p_115419_));
		float f5 = (float) entity.getHurtTime() - tick;
		float f6 = entity.getDamage() - tick;
		if (f6 < 0.0F) {
			f6 = 0.0F;
		}

		posestack.mulPose(Vector3f.YP.rotationDegrees(180.0F - Mth.lerp(tick, entity.yRotO, entity.getYRot())));
		if (f5 > 0.0F) {
			posestack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(f5) * f5 * f6 / 10.0F * 1));
		}


		posestack.scale(-1.0F, -1.0F, 1.0F);
		posestack.translate(0.0F, -1.501F, 0.0F);

		SpyglassStandBaseModel<T> entityModel = entity.isHigh() ? this.model : this.small_model;

		entityModel.onlySpyglass = false;
		entityModel.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		VertexConsumer vertexconsumer = buffersource.getBuffer(entityModel.renderType(this.getTextureLocation(entity)));
		entityModel.renderToBuffer(posestack, vertexconsumer, p_115423_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		if (!entity.getSpyGlass().isEmpty() && entity.getSpyGlass().getItem().isFoil(entity.getSpyGlass())) {
			entityModel.onlySpyglass = true;
			entityModel.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			VertexConsumer vertexconsumer2 = buffersource.getBuffer(RenderType.glintTranslucent());
			entityModel.renderToBuffer(posestack, vertexconsumer2, p_115423_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
		entityModel.onlySpyglass = false;
		posestack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(T p_114482_) {
		return SPYGLASS_STAND_LOCATION;
	}
}
