package com.github.teamfusion.spyglassplus.core.registry;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.items.BinocularsItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class SpyglassPlusItems {

    public static final Item BINOCULARS = register("binoculars", new BinocularsItem(new FabricItemSettings().group(SpyglassPlus.SPYGLASSPLUS_TAB)));

    public static<I extends Item> I register(String path, I item) {
        return Registry.register(Registry.ITEM, SpyglassPlus.id(path), item);
    }
}
