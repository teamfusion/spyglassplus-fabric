package com.github.teamfusion.spyglassplus.core.registry;

import com.chocohead.mm.api.ClassTinkerers;
import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.enchantments.DiscoveryEnchantment;
import com.github.teamfusion.spyglassplus.common.enchantments.IlluminateEnchantment;
import com.github.teamfusion.spyglassplus.common.enchantments.IndicateEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.util.registry.Registry;

public class SpyglassPlusEnchantments {
    public static final EnchantmentTarget SPYGLASS = ClassTinkerers.getEnum(EnchantmentTarget.class, "SPYGLASS");

    // ENCHANTMENTS //
    public static Enchantment DISCOVERY = register("discovery", new DiscoveryEnchantment());
    public static Enchantment INDICATING = register("indicate", new IndicateEnchantment());
    public static Enchantment ILLUMINATING = register("illuminate", new IlluminateEnchantment());

    public static <E extends Enchantment> E register(String path, E enchantment) {
        return Registry.register(Registry.ENCHANTMENT, SpyglassPlus.id(path), enchantment);
    }
}
