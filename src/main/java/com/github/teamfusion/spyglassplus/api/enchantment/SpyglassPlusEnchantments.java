package com.github.teamfusion.spyglassplus.api.enchantment;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.enchantment.Enchantment.Rarity.*;

public interface SpyglassPlusEnchantments {
    Enchantment DISCOVERY = register("discovery", new DiscoveryEnchantment(UNCOMMON));
    Enchantment INDICATE = register("indicate", new IndicateEnchantment(UNCOMMON));
    Enchantment ILLUMINATE = register("illuminate", new IlluminateEnchantment(UNCOMMON));
    Enchantment SCRUTINY = register("scrutiny", new ScrutinyEnchantment(UNCOMMON));
    Enchantment COMMAND = register("command", new CommandEnchantment(COMMON));

    private static Enchantment register(String id, Enchantment enchantment) {
        return Registry.register(Registry.ENCHANTMENT, new Identifier(SpyglassPlus.MOD_ID, id), enchantment);
    }
}
