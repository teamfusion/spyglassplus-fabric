package com.github.teamfusion.spyglassplus.impl.item;

import com.github.teamfusion.spyglassplus.api.entity.SpyglassStandEntity;
import com.github.teamfusion.spyglassplus.api.item.ISpyglass;
import com.github.teamfusion.spyglassplus.api.item.SpyglassPlusItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public final class SpyglassPlusItemsImpl implements SpyglassPlusItems, ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(ISpyglass.UPDATE_LOCAL_SCRUTINY_PACKET, ISpyglass::updateLocalScrutinyServer);

        DispenserBlock.registerBehavior(SYPGLASS_STAND, new ItemDispenserBehavior() {
            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
                BlockPos pos = pointer.getPos().offset(direction);
                ServerWorld world = pointer.getWorld();
                SpyglassStandEntity entity = new SpyglassStandEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5);
                EntityType.loadFromEntityNbt(world, null, entity, stack.getNbt());
                entity.setYaw(direction.asRotation());
                world.spawnEntity(entity);
                stack.decrement(1);
                return stack;
            }
        });
    }
}
