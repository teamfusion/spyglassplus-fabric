package com.github.teamfusion.spyglassplus.api.item;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import com.github.teamfusion.spyglassplus.api.enchantment.ScopingEnchantment;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public interface SpyglassPlusItemGroups {
    ItemGroup ALL = FabricItemGroupBuilder.create(new Identifier(SpyglassPlus.MOD_ID, "item_group"))
                                          .icon(() -> new ItemStack(Items.SPYGLASS))
                                          .appendItems((list, group) -> {
                                              DefaultedList<ItemStack> stacks = (DefaultedList<ItemStack>) list;

                                              Registry.ITEM.forEach(item -> item.appendStacks(group, stacks));

                                              Registry.ENCHANTMENT.stream()
                                                                  .filter(ScopingEnchantment.class::isInstance)
                                                                  .forEach(enchantment -> {
                                                                      ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
                                                                      stack.addEnchantment(enchantment, enchantment.getMaxLevel());
                                                                      stacks.add(stack);
                                                                  });
                                          })
                                          .build();
}
