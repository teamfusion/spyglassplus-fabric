package com.github.teamfusion.spyglassplus.api.item;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import com.github.teamfusion.spyglassplus.api.enchantment.target.SpyglassPlusEnchantmentTargets;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public interface SpyglassPlusItemGroups {
    ItemGroup ALL = FabricItemGroupBuilder.create(new Identifier(SpyglassPlus.MOD_ID, "item_group"))
                                          .icon(() -> new ItemStack(Items.SPYGLASS))
                                          .appendItems((stacks, group) -> {
                                              DefaultedList<ItemStack> list = (DefaultedList<ItemStack>) stacks;
                                              Registry.ITEM.stream().filter(ISpyglass.class::isInstance)
                                                           .filter(item -> Objects.nonNull(item.getGroup()))
                                                           .forEach(item -> list.add(new ItemStack(item)));
                                              for (Item item : Registry.ITEM) item.appendStacks(group, list);
                                          })
                                          .build()
                                          .setEnchantments(SpyglassPlusEnchantmentTargets.SCOPING);
}
