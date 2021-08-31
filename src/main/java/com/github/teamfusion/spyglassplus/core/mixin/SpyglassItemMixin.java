package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpyglassItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.function.Predicate;

@Mixin(SpyglassItem.class)
public class SpyglassItemMixin extends Item {
    private int commandTicks = 100;
    private boolean isCommanded = false;
    private boolean initiallyCommanded = false;

    public SpyglassItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantability() {
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
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            if (minecraftClient.options.getPerspective().isFirstPerson()) {
                int i = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.ILLUMINATING, stack);
                if (i > 0) {
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1, 1, false, false, false));
                }
                int j = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.INDICATING, stack);
                if (j > 0) {
                    Entity entity = checkEntity(user, 64.0D);
                    StatusEffectInstance effectInstance = new StatusEffectInstance(StatusEffects.GLOWING, 2, 1, false, false, false);
                    if (entity != null) {
                        ((LivingEntity) entity).addStatusEffect(effectInstance);
                    }
                }
                int k = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.COMMAND, stack);
                if (k > 0) {
                    Entity entity = checkEntity(user, 64.0D);
                    if (entity != null) {
                        this.setInitiallyCommanded(true);
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity user, int slot, boolean selected) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (!((PlayerEntity)user).isUsingSpyglass() && EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, stack) > 0) {
            ((ScrutinyAccess)minecraftClient.mouse).setZero();
        }
        if (this.initiallyCommanded) {
            this.commandTicks--;
            if (this.getCommandTicks() == 0) {
                this.setCommanded(true);
                this.setInitiallyCommanded(false);
                this.setCommandTicks(100);
                if (this.isCommanded()) {
                    this.setCommanded(false);
                    Entity entity = checkEntity((LivingEntity) user, 64.0D);
                    Box box = new Box(user.getBlockPos()).expand(6.0D);
                    List<WolfEntity> nearbyWolves = world.getEntitiesByClass(WolfEntity.class, box, TameableEntity::isTamed);
                    List<IronGolemEntity> nearbyIronGolems = world.getEntitiesByClass(IronGolemEntity.class, box, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);
                    List<SnowGolemEntity> nearbySnowGolems = world.getEntitiesByClass(SnowGolemEntity.class, box, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);
                    List<FoxEntity> nearbyFoxes = world.getEntitiesByClass(FoxEntity.class, box, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);
                    for (WolfEntity wolfEntity : nearbyWolves) {
                        if (wolfEntity.isAlive() && entity != wolfEntity) {
                            wolfEntity.setTarget((LivingEntity) entity);
                        }
                    }
                    for (IronGolemEntity ironGolemEntity : nearbyIronGolems) {
                        if (ironGolemEntity.isAlive() && entity != ironGolemEntity) {
                            ironGolemEntity.setTarget((LivingEntity) entity);
                        }
                    }
                    for (SnowGolemEntity snowGolemEntity : nearbySnowGolems) {
                        if (snowGolemEntity.isAlive() && entity != snowGolemEntity) {
                            snowGolemEntity.setTarget((LivingEntity) entity);
                        }
                    }
                    for (FoxEntity foxEntity : nearbyFoxes) {
                        if (foxEntity.isAlive() && entity != foxEntity) {
                            foxEntity.setTarget((LivingEntity) entity);
                        }
                    }
                }
            }
        }
    }

    private static Entity checkEntity(LivingEntity user, double distance) {
        Predicate<Entity> e = entity -> !entity.isSpectator() && entity.isLiving();
        Vec3d eyePos = user.getCameraPosVec(1.0F);
        Vec3d lookVec = user.getRotationVector();
        Vec3d distanceVec = eyePos.add(lookVec.multiply(distance));
        Box playerBox = user.getBoundingBox().stretch(lookVec.multiply(distance)).expand(1.0D);
        EntityHitResult traceResult = ProjectileUtil.raycast(user, eyePos, distanceVec, playerBox, e, distance * distance);
        if (traceResult == null) {
            return null;
        }
        return traceResult.getEntity();
    }
}