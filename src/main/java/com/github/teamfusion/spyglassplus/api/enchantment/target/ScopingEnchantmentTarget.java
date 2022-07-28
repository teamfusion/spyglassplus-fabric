package com.github.teamfusion.spyglassplus.api.enchantment.target;

import com.github.teamfusion.spyglassplus.api.tag.SpyglassPlusItemTags;
import com.github.teamfusion.spyglassplus.impl.enchantment.SpyglassPlusEnchantmentTargetsImpl;
import net.minecraft.item.Item;
import net.moddingplayground.frame.api.enchantment.v0.target.CustomEnchantmentTarget;

/**
 * @implNote Implemented through {@link SpyglassPlusEnchantmentTargetsImpl}
 */
@SuppressWarnings({ "unused", "deprecation" })
public class ScopingEnchantmentTarget extends CustomEnchantmentTarget {
    @Override
    public boolean method_8177(Item item) {
        return item.getRegistryEntry().isIn(SpyglassPlusItemTags.SCOPING_ITEMS);
    }
}
