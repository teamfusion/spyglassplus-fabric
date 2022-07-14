package com.github.teamfusion.spyglassplus.client.render.item;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.client.ModModelLayer;
import com.github.teamfusion.spyglassplus.client.model.SmallSpyglassStandModel;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpyglassStandBWLR extends BlockEntityWithoutLevelRenderer {
	private static final ResourceLocation SPYGLASS_STAND_LOCATION = new ResourceLocation(SpyglassPlus.MOD_ID, "textures/entity/spyglass_stand.png");

	private SmallSpyglassStandModel model;

	public SpyglassStandBWLR() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
		this.model = new SmallSpyglassStandModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModModelLayer.SPYGLASS_STAND));
	}

	@Override
	public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pOverlay) {
		if (pStack.is(SpyglassPlusItems.SPYGLASS_STAND.get())) {
			pPoseStack.pushPose();
			pPoseStack.scale(1.0F, -1.0F, -1.0F);
			VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(SPYGLASS_STAND_LOCATION), true, pStack.hasFoil());
			this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, pOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
			pPoseStack.popPose();
		}
	}
}