package com.github.teamfusion.spyglassplus.impl;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import com.github.teamfusion.spyglassplus.api.enchantment.SpyglassPlusEnchantments;
import com.github.teamfusion.spyglassplus.api.entity.SpyglassPlusEntityType;
import com.github.teamfusion.spyglassplus.api.item.SpyglassPlusItemGroups;
import com.github.teamfusion.spyglassplus.api.item.SpyglassPlusItems;
import com.github.teamfusion.spyglassplus.api.sound.SpyglassPlusSoundEvents;
import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;

public final class SpyglassPlusImpl implements SpyglassPlus, ModInitializer {
    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {}", MOD_NAME);
        Reflection.initialize(
            SpyglassPlusItemGroups.class,
            SpyglassPlusSoundEvents.class,
            SpyglassPlusItems.class,
            SpyglassPlusEntityType.class,
            SpyglassPlusEnchantments.class
        );
    }
}
