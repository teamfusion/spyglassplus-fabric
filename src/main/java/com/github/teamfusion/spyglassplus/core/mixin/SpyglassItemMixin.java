package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.message.ScrutinyResetMessage;
import com.github.teamfusion.spyglassplus.core.ISpyable;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Wolf;
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
	private int commandTicks = 100;
	private boolean isCommanded = false;
	private boolean initiallyCommanded = false;

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

	public void setCommandTicks(int commandTicks) {
		this.commandTicks = commandTicks;
	}

	public int getCommandTicks() {
		return this.commandTicks;
	}

	public void setInitiallyCommanded(boolean initiallyCommanded) {
		this.initiallyCommanded = initiallyCommanded;
	}

	@Override
	public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (user instanceof Player) {
			int i = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.ILLUMINATING.get(), stack);
			if (i > 0) {
				user.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1, 1, false, false, false));
			}
			int j = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.INDICATING.get(), stack);
			if (j > 0) {
				Entity entity = checkEntity(user, 64.0D);
				MobEffectInstance effectInstance = new MobEffectInstance(MobEffects.GLOWING, 2, 1, false, false, false);
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).addEffect(effectInstance);
				}
			}
			int k = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.COMMAND.get(), stack);
			if (k > 0) {
				Entity entity = checkEntityWithNoBlockClip(user, 64.0D);
				MobEffectInstance effectInstance = new MobEffectInstance(MobEffects.GLOWING, 2, 1, false, false, false);
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).addEffect(effectInstance);
					this.setInitiallyCommanded(true);
				}
			}

		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity user, int slot, boolean selected) {
		if (!((Player) user).isScoping() && EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.SCRUTINY.get(), stack) > 0) {
			if (world.isClientSide()) return;
			if (user instanceof ServerPlayer) {
				SpyglassPlus.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) user), new ScrutinyResetMessage());
			}
		}

		if (this.initiallyCommanded) {
			this.commandTicks--;
			if (this.getCommandTicks() == 0) {
				this.setCommanded(false);
				Entity entity = checkEntityWithNoBlockClip((LivingEntity) user, 64.0D);
				AABB box = new AABB(user.blockPosition()).inflate(6.0D);
				List<Wolf> nearbyWolves = world.getEntitiesOfClass(Wolf.class, box, TamableAnimal::isTame);
				List<IronGolem> nearbyIronGolems = world.getEntitiesOfClass(IronGolem.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
				List<SnowGolem> nearbySnowGolems = world.getEntitiesOfClass(SnowGolem.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
				List<Fox> nearbyFoxes = world.getEntitiesOfClass(Fox.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
				List<Axolotl> nearbyAxolotl = world.getEntitiesOfClass(Axolotl.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);

				if (entity != null && !(entity instanceof TamableAnimal && ((TamableAnimal) entity).isTame() && ((TamableAnimal) entity).isOwnedBy((LivingEntity) user))) {
					if (entity.getType() == EntityType.IRON_GOLEM && !((IronGolem) entity).isPlayerCreated() || entity.getType() != EntityType.IRON_GOLEM && entity.getType() != EntityType.SNOW_GOLEM) {
						if (user instanceof ISpyable && ((ISpyable) user).isCommand())
							this.setCommanded(true);
						this.setInitiallyCommanded(false);
						this.setCommandTicks(100);
						if (this.isCommanded()) {
							for (Wolf wolfEntity : nearbyWolves) {
								if (wolfEntity.isAlive() && entity != wolfEntity && entity instanceof LivingEntity) {
									wolfEntity.setTarget((LivingEntity) entity);
								}
							}
							for (IronGolem ironGolemEntity : nearbyIronGolems) {
								if (ironGolemEntity.isAlive() && entity != ironGolemEntity && entity instanceof LivingEntity) {
									ironGolemEntity.setTarget((LivingEntity) entity);
								}
							}
							for (SnowGolem snowGolemEntity : nearbySnowGolems) {
								if (snowGolemEntity.isAlive() && entity != snowGolemEntity && entity instanceof LivingEntity) {
									snowGolemEntity.setTarget((LivingEntity) entity);
								}
							}
							for (Fox foxEntity : nearbyFoxes) {
								if (foxEntity.isAlive() && entity != foxEntity && entity instanceof LivingEntity) {
									foxEntity.setTarget((LivingEntity) entity);
								}
							}

							for (Axolotl axolotlEntity : nearbyAxolotl) {
								if (axolotlEntity.isAlive() && entity != axolotlEntity && entity instanceof LivingEntity) {
									axolotlEntity.setTarget((LivingEntity) entity);
								}
							}
						}
					}

				}
			}
		}
	}

	private static Entity checkEntity(LivingEntity user, double distance) {
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