package com.github.teamfusion.spyglassplus.impl.item;

import com.github.teamfusion.spyglassplus.api.item.ISpyglass;
import com.github.teamfusion.spyglassplus.api.item.SpyglassPlusItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class SpyglassPlusItemsImpl implements SpyglassPlusItems, ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(ISpyglass.UPDATE_LOCAL_SCRUTINY_PACKET, ISpyglass::updateLocalScrutinyServer);
    }
}
