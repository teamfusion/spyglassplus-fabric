package com.github.teamfusion.spyglassplus.client.event;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

import java.util.Collection;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = SpyglassPlus.MOD_ID, value = Dist.CLIENT)
public class ClientHUDEvent {
	private static final ResourceLocation SCOPE_GUI_LOCATION = new ResourceLocation(SpyglassPlus.MOD_ID, "textures/gui/scope_gui.png");
	private static final ResourceLocation SCOPE_GUI_ICON_LOCATION = new ResourceLocation(SpyglassPlus.MOD_ID, "textures/gui/scope_gui_icons.png");

	private static int eyetick;

	private static boolean isEyeBlink = true;
	private static int eyePhase;

	protected static int leftPos;
	protected static int rightPos;
	protected static int topPos;

	private static int width;
	private static int height;

	private static int widthOffset;

	@SubscribeEvent
	public static void renderHudEvent(RenderGameOverlayEvent.Post event) {
		PoseStack stack = event.getMatrixStack();

		Minecraft mc = Minecraft.getInstance();

		if (mc.getWindow() != null) {
			width = mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
			height = mc.getWindow().getGuiScaledHeight();
			widthOffset = (width / 800);
			leftPos = (int) ((width) / 1.5);
			rightPos = (int) ((width) / 0.5);
			topPos = (height) / 2;
		}

		if (eyetick < 30 * 20 * 20) {
			++eyetick;
		} else {
			if (isEyeBlink) {
				eyePhase += 1;
				if (eyePhase == 4) {
					isEyeBlink = false;
				}
			} else {
				eyePhase -= 1;
				if (eyePhase == 0) {
					isEyeBlink = true;
				}
			}

			eyetick = 0;
		}

		int k = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.DISCOVERY.get(), mc.player.getUseItem());
		if (k > 0) {
			if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
				stack.pushPose();
				stack.translate((double) leftPos, (double) topPos, 0.0D);
				Entity entity = checkEntityWithNoBlockClip(mc.player, 64.0D);
				if (entity != null) {
					mc.font.draw(stack, entity.getDisplayName(), (int) 50 + widthOffset, (int) -100, 0xe0e0e0);

					if (entity instanceof LivingEntity) {

						ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.WHITE};

						MutableComponent s = new TranslatableComponent(SpyglassPlus.MOD_ID + ".spyglass.info.health").withStyle(textformatting);

						MutableComponent s2 = new TextComponent("(  * " + ((LivingEntity) entity).getHealth() / 2 + ")").withStyle(textformatting);

						/*
						 * right side render start
						 */
						stack.pushPose();

						//set right translate
						stack.translate((double) -leftPos + rightPos, (double) 0.0F, 0.0D);
						mc.font.draw(stack, s, (int) 450 + widthOffset, (int) -70, 0xe0e0e0);
						mc.font.draw(stack, s2, (int) 450 + widthOffset, (int) -60, 0xe0e0e0);
						RenderSystem.setShader(GameRenderer::getPositionTexShader);
						RenderSystem.setShaderTexture(0, Gui.GUI_ICONS_LOCATION);

						renderHeart(mc.gui, stack, 453 + widthOffset, (int) -60, true);
						renderHeart(mc.gui, stack, 453 + widthOffset, (int) -60, false);

						//attack damage
						if (((LivingEntity) entity).getAttribute(Attributes.ATTACK_DAMAGE) != null) {
							MutableComponent s3 = new TranslatableComponent(SpyglassPlus.MOD_ID + ".spyglass.info.damage").withStyle(textformatting);

							MutableComponent s4 = new TextComponent("(  * " + ((LivingEntity) entity).getAttributeValue(Attributes.ATTACK_DAMAGE) / 2 + ")").withStyle(textformatting);
							mc.font.draw(stack, s3, (int) 450 + widthOffset, (int) -50, 0xe0e0e0);
							mc.font.draw(stack, s4, (int) 450 + widthOffset, (int) -40, 0xe0e0e0);
							RenderSystem.setShader(GameRenderer::getPositionTexShader);
							RenderSystem.setShaderTexture(0, Gui.GUI_ICONS_LOCATION);

							renderHeart(mc.gui, stack, (int) 453 + widthOffset, (int) -40, true);
							renderHeart(mc.gui, stack, (int) 453 + widthOffset, (int) -40, false);
						}

						if (k > 1) {
							Collection<MobEffectInstance> collection = ((LivingEntity) entity).getActiveEffects();

							if (!collection.isEmpty()) {
								RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
								int k2 = 33;
								if (collection.size() > 5) {
									k2 = 132 / (collection.size() - 1);
								}


								Iterable<MobEffectInstance> iterable = collection.stream().filter(net.minecraftforge.client.ForgeHooksClient::shouldRenderEffect).sorted().collect(java.util.stream.Collectors.toList());
								renderIcons(stack, 453 + widthOffset, -(k2 - 8), 6, iterable);
							}
						}

						//reset
						stack.popPose();
						/*
						 * right side render finished
						 */

						//entity and gui
						//idk why I should have to double
						stack.pushPose();
						stack.scale(0.75F, 0.75F, 0.75F);
						RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
						RenderSystem.setShaderTexture(0, SCOPE_GUI_LOCATION);
						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
						mc.gui.blit(stack, 26 + widthOffset, -80, 0, 0, 64 * 2, 124 * 2);
						mc.gui.blit(stack, 58 + widthOffset, -80, 128, (32 * eyePhase * 1), 64, 32);
						stack.popPose();
						//render entity
						stack.pushPose();
						float entityWidth = entity.getDimensions(entity.getPose()).width;
						float entityHeight = entity.getDimensions(entity.getPose()).height;

						InventoryScreen.renderEntityInInventory(leftPos + 65 + widthOffset, topPos + 90, (int) (25 * (1 / entityWidth)), 0.0F, 0.0F, (LivingEntity) entity);
						stack.popPose();
					}
				}
				stack.popPose();
			}
		}
	}

	private static void renderIcons(PoseStack p_194009_, int x, int y, int offsetX, Iterable<MobEffectInstance> p_194012_) {
		int i = 0;
		MobEffectTextureManager mobeffecttexturemanager = Minecraft.getInstance().getMobEffectTextures();
		for (MobEffectInstance mobeffectinstance : p_194012_) {
			MobEffect mobeffect = mobeffectinstance.getEffect();
			TextureAtlasSprite textureatlassprite = mobeffecttexturemanager.get(mobeffect);
			RenderSystem.setShaderTexture(0, textureatlassprite.atlas().location());
			Minecraft.getInstance().gui.blit(p_194009_, x + 6 + i, y, 0, 18, 18, textureatlassprite);
			i += offsetX;
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
