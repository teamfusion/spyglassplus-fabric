package com.github.teamfusion.spyglassplus.core.registry;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.items.BinocularsItem;
import com.github.teamfusion.spyglassplus.common.items.SpyglassStandItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SpyglassPlusItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpyglassPlus.MOD_ID);


	public static final RegistryObject<Item> BINOCULARS = ITEMS.register("binoculars", () -> new BinocularsItem(new Item.Properties().tab(SpyglassPlus.SPYGLASSPLUS_TAB)));
	public static final RegistryObject<Item> SPYGLASS_STAND = ITEMS.register("spyglass_stand", () -> new SpyglassStandItem(new Item.Properties().stacksTo(1).tab(SpyglassPlus.SPYGLASSPLUS_TAB)));

}
