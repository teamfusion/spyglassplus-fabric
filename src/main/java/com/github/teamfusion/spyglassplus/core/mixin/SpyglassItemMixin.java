package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpyglassItem;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Predicate;

@Mixin(SpyglassItem.class)
public class SpyglassItemMixin extends Item {

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
            }
        }
    }

    private static Entity checkEntity(LivingEntity user, double distance) {
        Predicate<Entity> e = entity -> !entity.isSpectator();
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