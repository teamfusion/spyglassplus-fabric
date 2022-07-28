package com.github.teamfusion.spyglassplus.impl.client.item;

import com.github.teamfusion.spyglassplus.api.item.ISpyglass;
import com.github.teamfusion.spyglassplus.api.item.SpyglassPlusItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

@Environment(EnvType.CLIENT)
public final class SpyglassPlusItemsClientImpl implements SpyglassPlusItems, ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(ISpyglass::appendLocalScrutinyLevelTooltip);
    }
}
