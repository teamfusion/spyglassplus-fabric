package com.github.teamfusion.spyglassplus.core.registry;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.enchantments.*;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SpyglassPlusEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SpyglassPlus.MOD_ID);


    public static final EnchantmentCategory SPYGLASS = EnchantmentCategory.create("SPYGLASS", item -> {
        return item instanceof SpyglassItem;
    });

    // ENCHANTMENTS //
    public static RegistryObject<Enchantment> DISCOVERY = ENCHANTMENTS.register("discovery", DiscoveryEnchantment::new);
    public static RegistryObject<Enchantment> INDICATING = ENCHANTMENTS.register("indicate", IndicateEnchantment::new);
    public static RegistryObject<Enchantment> ILLUMINATING = ENCHANTMENTS.register("illuminating", IlluminatingEnchantment::new);
    public static RegistryObject<Enchantment> SCRUTINY = ENCHANTMENTS.register("scrutiny", ScrutinyEnchantment::new);
    public static RegistryObject<Enchantment> COMMAND = ENCHANTMENTS.register("command", CommandEnchantment::new);

}
