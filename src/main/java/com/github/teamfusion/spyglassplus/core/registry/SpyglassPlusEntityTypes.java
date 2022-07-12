package com.github.teamfusion.spyglassplus.core.registry;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.entity.SpyglassStandEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = SpyglassPlus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpyglassPlusEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, SpyglassPlus.MOD_ID);

	public static final RegistryObject<EntityType<SpyglassStandEntity>> SPYGLASS_STAND = ENTITIES_REGISTRY.register("spyglass_stand", () -> EntityType.Builder.<SpyglassStandEntity>of(SpyglassStandEntity::new, MobCategory.MISC).sized(0.6F, 1.6F).clientTrackingRange(8).build(prefix("spyglass_stand")));

	private static String prefix(String path) {
		return "spyglassplus." + path;
	}
}