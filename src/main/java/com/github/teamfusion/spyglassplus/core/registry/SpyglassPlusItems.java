package com.github.teamfusion.spyglassplus.core.registry;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.items.BinocularsItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpyglassPlusItems {
    /// INIT ///
    public static final Item BINOCULARS = new BinocularsItem(new FabricItemSettings().group(SpyglassPlus.SPYGLASSPLUS_TAB));

    /// REGISTRY ///
    public static void register() {
        Registry.register(Registry.ITEM, new Identifier("spyglassplus", "binoculars"), BINOCULARS);
    }
}
