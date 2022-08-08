package com.github.teamfusion.spyglassplus.api.entity;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface SpyglassPlusEntityType {
    EntityType<SpyglassStandEntity> SPYGLASS_STAND = register("spyglass_stand",
        FabricEntityTypeBuilder.<SpyglassStandEntity>createLiving()
                               .entityFactory(SpyglassStandEntity::new)
                               .spawnGroup(SpawnGroup.MISC)
                               .defaultAttributes(SpyglassStandEntity::createLivingAttributes)
                               .dimensions(EntityDimensions.changing(0.6F, 1.9F))
                               .trackRangeChunks(8)
    );

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> builder) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(SpyglassPlus.MOD_ID, id), builder.build());
    }
}
