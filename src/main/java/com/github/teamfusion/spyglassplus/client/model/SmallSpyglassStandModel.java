package com.github.teamfusion.spyglassplus.client.model;// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.github.teamfusion.spyglassplus.common.entity.SpyglassStandEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SmallSpyglassStandModel<T extends SpyglassStandEntity> extends EntityModel<T> {
	private final ModelPart all;
	private final ModelPart holderrotate;
	private final ModelPart spyglass;


	public SmallSpyglassStandModel(ModelPart root) {
		this.all = root.getChild("all");
		this.holderrotate = this.all.getChild("holderrotate");
		this.spyglass = this.holderrotate.getChild("spyglass");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 41.0F, 0.0F));

		PartDefinition legs = all.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leg3_r1 = legs.addOrReplaceChild("leg3_r1", CubeListBuilder.create().texOffs(0, 26).addBox(-0.6437F, -24.8677F, 6.3748F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition leg2_r1 = legs.addOrReplaceChild("leg2_r1", CubeListBuilder.create().texOffs(0, 26).addBox(-9.0349F, -24.511F, -0.1156F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2135F, -0.6227F, 0.3747F));

		PartDefinition leg1_r1 = legs.addOrReplaceChild("leg1_r1", CubeListBuilder.create().texOffs(0, 26).addBox(7.0349F, -24.511F, -0.1156F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2135F, 0.6227F, -0.3747F));

		PartDefinition holderrotate = all.addOrReplaceChild("holderrotate", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -26.0F, 0.0F));

		PartDefinition spyglass = holderrotate.addOrReplaceChild("spyglass", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -7.0F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(0, 13).addBox(-1.2F, -3.2F, -7.2F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.spyglass.visible = !entity.getSpyGlass().isEmpty();
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}