package com.github.teamfusion.spyglassplus;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusNetwork;
import net.fabricmc.api.ClientModInitializer;

public class SpyglassPlusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpyglassPlusNetwork.init();
    }

}
