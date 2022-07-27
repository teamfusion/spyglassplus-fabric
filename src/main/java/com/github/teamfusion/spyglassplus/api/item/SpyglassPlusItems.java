package com.github.teamfusion.spyglassplus.api.item;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

public interface SpyglassPlusItems {
    Item BINOCULARS = unstackable("binoculars", noGroup(BinocularsItem::new));

    private static Function<FabricItemSettings, Item> noGroup(Function<FabricItemSettings, Item> item) {
        return settings -> item.apply(settings.group(null));
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SpyglassPlus.MOD_ID, id), item);
    }

    private static Item register(String id, Function<FabricItemSettings, Item> item) {
        return register(id, item.apply(new FabricItemSettings().group(SpyglassPlusItemGroups.ALL)));
    }

    private static Item unstackable(String id, Function<FabricItemSettings, Item> item) {
        return register(id, settings -> item.apply(settings.maxCount(1)));
    }
}
