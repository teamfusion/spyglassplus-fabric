package com.github.teamfusion.spyglassplus.impl;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import net.fabricmc.api.ModInitializer;

public final class SpyglassPlusImpl implements SpyglassPlus, ModInitializer {
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing " + MOD_NAME);
    }
}
