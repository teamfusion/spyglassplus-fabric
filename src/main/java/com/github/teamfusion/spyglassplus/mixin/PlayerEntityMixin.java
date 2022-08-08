package com.github.teamfusion.spyglassplus.mixin;

import com.github.teamfusion.spyglassplus.api.entity.ScopingEntity;
import com.github.teamfusion.spyglassplus.api.entity.ScopingPlayer;
import com.github.teamfusion.spyglassplus.api.entity.SpyglassStandEntity;
import com.github.teamfusion.spyglassplus.api.tag.SpyglassPlusItemTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ScopingPlayer, ScopingEntity {
    @Shadow public abstract boolean isUsingSpyglass();

    @Unique private Optional<Integer> spyglassStand = Optional.empty();

    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    @Override
    public void setSpyglassStand(Integer id) {
        this.spyglassStand = Optional.ofNullable(id);
    }

    @Unique
    @Override
    public void setSpyglassStandEntity(SpyglassStandEntity entity) {
        this.spyglassStand = Optional.of(entity).map(SpyglassStandEntity::getId);
    }

    @Unique
    @Override
    public Optional<Integer> getSpyglassStand() {
        return this.spyglassStand;
    }

    @Unique
    @Override
    public Optional<SpyglassStandEntity> getSpyglassStandEntity() {
        return this.getSpyglassStand()
                   .map(this.world::getEntityById)
                   .filter(SpyglassStandEntity.class::isInstance)
                   .map(SpyglassStandEntity.class::cast);
    }

    @Unique
    @Override
    public ItemStack getScopingStack() {
        return this.getSpyglassStandEntity()
                   .map(SpyglassStandEntity::getScopingStack)
                   .orElseGet(() -> this.isUsingItem() ? this.getActiveItem() : ItemStack.EMPTY);
    }

    @Unique
    @Override
    public boolean isScoping() {
        return this.isUsingSpyglass();
    }

    /**
     * Adds spyglass stands and  the tag {@link SpyglassPlusItemTags#SCOPING_ITEMS} as valid spyglass states.
     */
    @Inject(method = "isUsingSpyglass", at = @At("RETURN"), cancellable = true)
    private void onIsUsingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            if (this.getScopingStack().isIn(SpyglassPlusItemTags.SCOPING_ITEMS)) cir.setReturnValue(true);
        }
    }
}
