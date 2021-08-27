package com.github.teamfusion.spyglassplus.core.registry;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class SpyglassPlusItems {

    // ENCHANTED BOOKS //
    // book

    public static <T extends Item> T register(String path, T item) {
        Registry.register(Registry.ITEM, SpyglassPlus.id(path), item);

        return item;
    }

    public static Item register(String path, ItemGroup group) {
        return register(path, new Item(new Item.Settings().group(group)));
    }
}