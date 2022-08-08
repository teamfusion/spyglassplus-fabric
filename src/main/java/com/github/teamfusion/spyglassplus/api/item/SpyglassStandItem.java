package com.github.teamfusion.spyglassplus.api.item;

import com.github.teamfusion.spyglassplus.api.entity.SpyglassPlusEntityType;
import com.github.teamfusion.spyglassplus.api.entity.SpyglassStandEntity;
import com.github.teamfusion.spyglassplus.api.sound.SpyglassPlusSoundEvents;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class SpyglassStandItem extends Item {
    public SpyglassStandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext usage) {
        if (usage.getSide() == Direction.DOWN) return ActionResult.FAIL;

        ItemPlacementContext placement = new ItemPlacementContext(usage);
        BlockPos pos = placement.getBlockPos();

        World world = usage.getWorld();
        Vec3d vec = Vec3d.ofBottomCenter(pos);
        Box box = SpyglassPlusEntityType.SPYGLASS_STAND.getDimensions().getBoxAt(vec.getX(), vec.getY(), vec.getZ());
        if (!world.isSpaceEmpty(null, box) || !world.getOtherEntities(null, box).isEmpty()) return ActionResult.FAIL;

        ItemStack stack = usage.getStack();
        if (world instanceof ServerWorld serverWorld) {
            SpyglassStandEntity entity = SpyglassPlusEntityType.SPYGLASS_STAND.create(serverWorld, stack.getNbt(), null, usage.getPlayer(), pos, SpawnReason.SPAWN_EGG, true, true);
            if (entity == null) return ActionResult.FAIL;

            float yaw = (float) MathHelper.floor((MathHelper.wrapDegrees(usage.getPlayerYaw()) + 22.5f) / 45.0f) * 45.0f;
            entity.setSpyglassYaw(yaw);
            entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), yaw, 0.0f);
            serverWorld.spawnEntityAndPassengers(entity);

            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpyglassPlusSoundEvents.ENTITY_SPYGLASS_STAND_PLACE, SoundCategory.BLOCKS, 0.75f, 0.8f);
            entity.emitGameEvent(GameEvent.ENTITY_PLACE, usage.getPlayer());
        }
        stack.decrement(1);
        return ActionResult.success(world.isClient);
    }
}
