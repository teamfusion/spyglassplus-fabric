package com.github.teamfusion.spyglassplus.core.mixin;

import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    private float currentZoom;
    @Shadow
    private float fov;
    @Shadow
    private float oldFov;

    @Inject(at = @At("HEAD"), method = "getNightVisionScale", cancellable = true)
    private static void getNightVisionStrength(LivingEntity entity, float f, CallbackInfoReturnable<Float> cir) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.ILLUMINATING.get(), entity.getUseItem());
        if (((Player) entity).isScoping() && i > 0) {
            cir.setReturnValue(1.0F);
        }
    }

    @Inject(at = @At("HEAD"), method = "getFov", cancellable = true)
    public void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        Minecraft minecraftClient = Minecraft.getInstance();
        LocalPlayer player = minecraftClient.player;
        if (player != null) {
            if (minecraftClient.options.getCameraType().isFirstPerson()) {
                if (player.isScoping()) {
                    double defaultValue = 10.0D;
                    double lerpValue = Mth.lerp(defaultValue, this.fov, this.oldFov);
                    double finalValue = Mth.ceil(lerpValue) + 9.0D;
                    int i = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.SCRUTINY.get(), player.getUseItem());
                    if (player.isScoping() && i > 0) {

                        int zoomValue = ((ScrutinyAccess) minecraftClient.mouseHandler).getZoom();

                        finalValue -= zoomValue;

                    }

                    int j = EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.COMMAND.get(), player.getUseItem());

                    if (player.isScoping() && j > 0 && checkEntityWithNoBlockClip(player, 64.0D) != null) {
                        if (currentZoom <= 1.0F) {
                            currentZoom += 0.001F;
                        }
                        finalValue -= currentZoom;
                    } else if (currentZoom > 0) {
                        currentZoom += 0.01F;
                        finalValue -= currentZoom;
                    }
                    cir.setReturnValue(finalValue);
                } else {
                    currentZoom = 0;
                }
            }
        }
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
