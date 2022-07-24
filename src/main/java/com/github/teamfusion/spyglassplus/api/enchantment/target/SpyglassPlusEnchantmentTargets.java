package com.github.teamfusion.spyglassplus.api.enchantment.target;

import com.github.teamfusion.spyglassplus.impl.enchantment.SpyglassPlusEnchantmentTargetsImpl;
import net.minecraft.enchantment.EnchantmentTarget;

public interface SpyglassPlusEnchantmentTargets {
    EnchantmentTarget SCOPING = SpyglassPlusEnchantmentTargetsImpl.SCOPING.getEnchantmentTarget();
}
