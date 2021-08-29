package com.github.teamfusion.spyglassplus.core.registry;

import com.chocohead.mm.api.ClassTinkerers;
import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.enchantments.CommandEnchantment;
import com.github.teamfusion.spyglassplus.common.enchantments.DiscoveryEnchantment;
import com.github.teamfusion.spyglassplus.common.enchantments.IlluminatingEnchantment;
import com.github.teamfusion.spyglassplus.common.enchantments.IndicatingEnchantment;
import com.github.teamfusion.spyglassplus.common.enchantments.ScrutinyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.util.registry.Registry;

public class SpyglassPlusEnchantments {
    public static final EnchantmentTarget SPYGLASS = ClassTinkerers.getEnum(EnchantmentTarget.class, "SPYGLASS");

    // ENCHANTMENTS //
    public static Enchantment DISCOVERY = register("discovery", new DiscoveryEnchantment());
    public static Enchantment INDICATE = register("indicate", new IndicatingEnchantment());
    public static Enchantment ILLUMINATING = register("illuminating", new IlluminatingEnchantment());
    public static Enchantment SCRUTINY = register("scrutiny", new ScrutinyEnchantment());
    public static Enchantment COMMAND = register("command", new CommandEnchantment());

    public static <E extends Enchantment> E register(String path, E enchantment) {
        return Registry.register(Registry.ENCHANTMENT, SpyglassPlus.id(path), enchantment);
    }
}
