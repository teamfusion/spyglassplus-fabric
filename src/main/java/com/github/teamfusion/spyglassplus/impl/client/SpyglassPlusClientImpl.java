package com.github.teamfusion.spyglassplus.impl.client;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import com.github.teamfusion.spyglassplus.api.client.model.entity.SpyglassPlusEntityModelLayers;
import com.google.common.reflect.Reflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class SpyglassPlusClientImpl implements SpyglassPlus, ClientModInitializer {
    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        Reflection.initialize(SpyglassPlusEntityModelLayers.class);
    }
}
