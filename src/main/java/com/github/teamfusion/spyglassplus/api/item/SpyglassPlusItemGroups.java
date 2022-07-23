package com.github.teamfusion.spyglassplus.api.item;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public interface SpyglassPlusItemGroups {
    ItemGroup ALL = FabricItemGroupBuilder.build(new Identifier(SpyglassPlus.MOD_ID, "item_group"), () -> new ItemStack(Items.SPYGLASS));
}
