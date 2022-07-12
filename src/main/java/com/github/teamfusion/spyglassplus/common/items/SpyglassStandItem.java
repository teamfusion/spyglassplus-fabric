package com.github.teamfusion.spyglassplus.common.items;

import com.github.teamfusion.spyglassplus.client.render.item.SpyglassStandBWLR;
import com.github.teamfusion.spyglassplus.common.entity.SpyglassStandEntity;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEntityTypes;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class SpyglassStandItem extends Item {
	public SpyglassStandItem(Properties p_41383_) {
		super(p_41383_);
	}

	public InteractionResult useOn(UseOnContext p_40510_) {
		Direction direction = p_40510_.getClickedFace();
		if (direction == Direction.DOWN) {
			return InteractionResult.FAIL;
		} else {
			Level level = p_40510_.getLevel();
			BlockPlaceContext blockplacecontext = new BlockPlaceContext(p_40510_);
			BlockPos blockpos = blockplacecontext.getClickedPos();
			ItemStack itemstack = p_40510_.getItemInHand();
			Vec3 vec3 = Vec3.atBottomCenterOf(blockpos);
			AABB aabb = SpyglassPlusEntityTypes.SPYGLASS_STAND.get().getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());
			if (level.noCollision((Entity) null, aabb) && level.getEntities((Entity) null, aabb).isEmpty()) {
				if (level instanceof ServerLevel) {
					ServerLevel serverlevel = (ServerLevel) level;
					SpyglassStandEntity stand = SpyglassPlusEntityTypes.SPYGLASS_STAND.get().create(serverlevel, itemstack.getTag(), (Component) null, p_40510_.getPlayer(), blockpos, MobSpawnType.SPAWN_EGG, true, true);
					if (stand == null) {
						return InteractionResult.FAIL;
					}

					stand.moveTo(stand.getX(), stand.getY(), stand.getZ(), 0.0F, 0.0F);
					if (p_40510_.getPlayer() != null) {
						stand.setXRot(p_40510_.getPlayer().getXRot());
						stand.setYRot(p_40510_.getPlayer().getYRot());
					}

					serverlevel.addFreshEntityWithPassengers(stand);
					level.playSound(null, stand.getX(), stand.getY(), stand.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
					level.gameEvent(p_40510_.getPlayer(), GameEvent.ENTITY_PLACE, stand);
				}

				itemstack.shrink(1);
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else {
				return InteractionResult.FAIL;
			}
		}
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IItemRenderProperties() {

			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return new SpyglassStandBWLR();
			}
		});
	}
}
