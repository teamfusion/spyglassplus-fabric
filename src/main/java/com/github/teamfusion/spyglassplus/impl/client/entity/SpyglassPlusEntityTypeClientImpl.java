package com.github.teamfusion.spyglassplus.impl.client.entity;

import com.github.teamfusion.spyglassplus.api.client.render.entity.SpyglassStandEntityRenderer;
import com.github.teamfusion.spyglassplus.api.entity.SpyglassPlusEntityType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public final class SpyglassPlusEntityTypeClientImpl implements SpyglassPlusEntityType, ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(SPYGLASS_STAND, SpyglassStandEntityRenderer::new);
    }
}
