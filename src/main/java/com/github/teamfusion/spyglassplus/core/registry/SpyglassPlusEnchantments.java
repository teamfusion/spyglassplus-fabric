package com.github.teamfusion.spyglassplus.core.registry;

import com.chocohead.mm.api.ClassTinkerers;
import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.enchantments.DiscoveryEnchantment;
import com.github.teamfusion.spyglassplus.common.enchantments.IndicateEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.util.registry.Registry;

public class SpyglassPlusEnchantments {
    public static final EnchantmentTarget SPYGLASS = ClassTinkerers.getEnum(EnchantmentTarget.class, "SPYGLASS");

    // ENCHANTMENTS //
    private static Enchantment DISCOVERY = register("discovery", new DiscoveryEnchantment());
    private static Enchantment INDICATE = register("indicate", new IndicateEnchantment());

    public static <E extends Enchantment> E register(String path, E enchantment) {
        return Registry.register(Registry.ENCHANTMENT, SpyglassPlus.id(path), enchantment);
    }
}
