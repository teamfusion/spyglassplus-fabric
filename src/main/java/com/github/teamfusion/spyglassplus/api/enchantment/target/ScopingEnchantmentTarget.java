package com.github.teamfusion.spyglassplus.api.enchantment.target;

import com.github.teamfusion.spyglassplus.api.item.ISpyglass;
import com.github.teamfusion.spyglassplus.impl.enchantment.SpyglassPlusEnchantmentTargetsImpl;
import net.minecraft.item.Item;
import net.moddingplayground.frame.api.enchantment.v0.target.CustomEnchantmentTarget;

/**
 * @implNote Implemented through {@link SpyglassPlusEnchantmentTargetsImpl}
 */
@SuppressWarnings("unused")
public class ScopingEnchantmentTarget extends CustomEnchantmentTarget {
    public boolean isAcceptableItem(Item item) {
        return item instanceof ISpyglass;
    }
}
