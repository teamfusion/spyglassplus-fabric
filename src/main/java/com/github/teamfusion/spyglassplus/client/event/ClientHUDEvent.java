package com.github.teamfusion.spyglassplus.client.event;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = SpyglassPlus.MOD_ID, value = Dist.CLIENT)
public class ClientHUDEvent {
	private static final ResourceLocation TRIAL_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/trial_icon.png");


	@SubscribeEvent
	public static void renderHudEvent(RenderGameOverlayEvent.Post event) {
		PoseStack stack = event.getMatrixStack();

		Minecraft mc = Minecraft.getInstance();

		int k = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.DISCOVERY.get(), mc.player.getUseItem());
		if (k > 0) {
			if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
				stack.pushPose();
				Entity entity = checkEntityWithNoBlockClip(mc.player, 64.0D);
				if (entity != null) {
					mc.font.draw(stack, entity.getDisplayName(), (int) 20, (int) 50, 0xe0e0e0);

					if (entity instanceof LivingEntity) {
						InventoryScreen.renderEntityInInventory(30, 200, 24, 0.0F, 0.0F, (LivingEntity) entity);


						ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.WHITE};

						MutableComponent s = new TranslatableComponent(SpyglassPlus.MOD_ID + ".spyglass.info.health").withStyle(textformatting);

						MutableComponent s2 = new TextComponent("(  * " + ((LivingEntity) entity).getHealth() / 2 + ")").withStyle(textformatting);


						mc.font.draw(stack, s, (int) 350, (int) 50, 0xe0e0e0);
						mc.font.draw(stack, s2, (int) 350, (int) 60, 0xe0e0e0);
						RenderSystem.setShaderTexture(0, Gui.GUI_ICONS_LOCATION);
						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

						renderHeart(mc.gui, stack, (int) 353, (int) 60, true);
						renderHeart(mc.gui, stack, (int) 353, (int) 60, false);
					}
				}
				stack.popPose();
			}
		}
	}

	private static void renderHeart(Gui gui, PoseStack posestack, int x, int y, boolean isContainer) {
		gui.blit(posestack, x, y, 16 + (2 * 2 + (isContainer ? 1 : 0)) * 9, 0, 9, 9);
	}

	private static Entity checkEntityWithNoBlockClip(LivingEntity user, double distance) {
		Predicate<Entity> e = entity -> !entity.isSpectator() && entity.isAlive();
		Vec3 eyePos = user.getEyePosition(1.0F);
		Vec3 lookVec = user.getLookAngle();
		Vec3 distanceVec = eyePos.add(lookVec.scale(distance));
		AABB playerBox = user.getBoundingBox().expandTowards(lookVec.scale(distance)).inflate(1.0D);

		HitResult hitresult = user.level.clip(new ClipContext(eyePos, distanceVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, user));

		if (hitresult.getType() != HitResult.Type.MISS) {
			distanceVec = hitresult.getLocation();
		}

		EntityHitResult traceResult = ProjectileUtil.getEntityHitResult(user.getLevel(), user, eyePos, distanceVec, playerBox, e);

		if (traceResult == null) {
			return null;
		}


		return traceResult.getEntity();
	}
}
