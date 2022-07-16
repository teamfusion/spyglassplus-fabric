package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.message.ScrutinyResetMessage;
import com.github.teamfusion.spyglassplus.core.ISpyable;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.function.Predicate;

@Mixin(SpyglassItem.class)
public class SpyglassItemMixin extends Item {
	private boolean isCommanded = false;
	public SpyglassItemMixin(Properties settings) {
		super(settings);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public int getEnchantmentValue() {
		return 1;
	}

	public boolean isCommanded() {
		return this.isCommanded;
	}

	public void setCommanded(boolean commanded) {
		this.isCommanded = commanded;
	}

	@Override
	public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (user instanceof Player) {
			boolean flag = ((ISpyable) user).getSpyGlassStands() != null && !((ISpyable) user).getSpyGlassStands().getSpyGlass().isEmpty();

			ItemStack itemstack2 = flag ? ((ISpyable) user).getSpyGlassStands().getSpyGlass() : user.getUseItem();

			if (!level.isClientSide) {
				int i = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.ILLUMINATING.get(), itemstack2);
				if (i > 0) {
					user.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1, 1, false, false, false));
				}
				int j = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.INDICATING.get(), itemstack2);
				if (j > 0) {
					Entity entity = checkEntity(flag ? ((ISpyable) user).getSpyGlassStands() : user, 64.0D);
					MobEffectInstance effectInstance = new MobEffectInstance(MobEffects.GLOWING, 2, 1, false, false, false);
					if (entity instanceof LivingEntity) {
						((LivingEntity) entity).addEffect(effectInstance);
					}
				}
				int k = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.COMMAND.get(), stack);
				if (k > 0) {
					Entity entity = checkEntityWithNoBlockClip(flag ? ((ISpyable) user).getSpyGlassStands() : user, 64.0D);
					MobEffectInstance effectInstance = new MobEffectInstance(MobEffects.GLOWING, 2, 1, false, false, false);
					if (entity instanceof LivingEntity) {
						((LivingEntity) entity).addEffect(effectInstance);
					}
				}
			}
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		Player user = (Player) entity;
		boolean flag = ((ISpyable) user).getSpyGlassStands() != null && !((ISpyable) user).getSpyGlassStands().getSpyGlass().isEmpty();

		ItemStack itemstack2 = flag ? ((ISpyable) user).getSpyGlassStands().getSpyGlass() : user.getUseItem();


		if (!user.isScoping() && EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.SCRUTINY.get(), itemstack2) > 0) {
			if (world.isClientSide()) return;
			if (user instanceof ServerPlayer) {
				SpyglassPlus.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) user), new ScrutinyResetMessage());
			}
		}
		if (user instanceof ISpyable) {
			if (((ISpyable) user).getCommandTick() > 0) {
				((ISpyable) user).setCommandTick(((ISpyable) user).getCommandTick() - 1);
			}
		}

		if (user.isScoping()) {
			if (user instanceof ISpyable && ((ISpyable) user).isCommand()) {
				if (EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.COMMAND.get(), itemstack2) > 0) {
					if (((ISpyable) user).getCommandTick() == 0) {
						this.setCommanded(false);
						Entity target = checkEntityWithNoBlockClip(flag ? ((ISpyable) user).getSpyGlassStands() : user, 64.0D);
						AABB box = new AABB(user.blockPosition()).inflate(32.0D);
						List<TamableAnimal> nearbyTamableAnimals = world.getEntitiesOfClass(TamableAnimal.class, box, TamableAnimal::isTame);
						List<AbstractGolem> nearbyGolems = world.getEntitiesOfClass(AbstractGolem.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
						List<Fox> nearbyFox = world.getEntitiesOfClass(Fox.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
						List<Axolotl> nearbyAxolotl = world.getEntitiesOfClass(Axolotl.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);


						if (target != null && !(target instanceof TamableAnimal tamableTarget && tamableTarget.isTame() && tamableTarget.isOwnedBy(user))) {
							this.setCommanded(true);
							((ISpyable) user).setCommandTick(100);
							if (this.isCommanded()) {
								for (TamableAnimal tamableAnimal : nearbyTamableAnimals) {
									if (tamableAnimal.isAlive() && target != tamableAnimal && target instanceof LivingEntity livingTarget && tamableAnimal.isOwnedBy(user)) {
										tamableAnimal.setTarget(livingTarget);
									}
								}
								for (Fox fox : nearbyFox) {
									if (fox.isAlive() && target != fox && target instanceof LivingEntity livingTarget && fox.getTarget() != user) {
										fox.setTarget(livingTarget);
									}
								}
								for (Axolotl axolotl : nearbyAxolotl) {
									if (axolotl.isAlive() && target != axolotl && target instanceof LivingEntity livingTarget && axolotl.getTarget() != user) {
										axolotl.setTarget(livingTarget);
									}
								}

								for (AbstractGolem golemEntity : nearbyGolems) {
									if (golemEntity.isAlive() && target != golemEntity && target instanceof LivingEntity livingTarget && golemEntity.getTarget() != user) {
										golemEntity.setTarget(livingTarget);
									}
								}
								((ISpyable) user).setCommand(false);
							}

						}
					}
				}
			}
		}
	}

	private static Entity checkEntity(Entity user, double distance) {
		Predicate<Entity> e = entity -> !entity.isSpectator() && entity.isAlive();
		Vec3 eyePos = user.getEyePosition(1.0F);
		Vec3 lookVec = user.getLookAngle();
		Vec3 distanceVec = eyePos.add(lookVec.scale(distance));
		AABB playerBox = user.getBoundingBox().expandTowards(lookVec.scale(distance)).inflate(1.0D);
		EntityHitResult traceResult = ProjectileUtil.getEntityHitResult(user, eyePos, distanceVec, playerBox, e, distance * distance);
		if (traceResult == null) {
			return null;
		}
		return traceResult.getEntity();
	}

	private static Entity checkEntityWithNoBlockClip(Entity user, double distance) {
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