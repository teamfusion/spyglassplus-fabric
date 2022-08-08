package com.github.teamfusion.spyglassplus.api.client.model.entity;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public interface SpyglassPlusEntityModelLayers {
    EntityModelLayer SPYGLASS_STAND                = registerSpyglass("main", SpyglassStandEntityModel::getTexturedModelData);
    EntityModelLayer SPYGLASS_STAND_SMALL          = registerSpyglass("small", SpyglassStandEntityModel::getSmallTexturedModelData);

    EntityModelLayer SPYGLASS_STAND_SPYGLASS       = registerSpyglass("spyglass", SpyglassStandEntityModel::getSpyglassTexturedModelData);
    EntityModelLayer SPYGLASS_STAND_SPYGLASS_SMALL = registerSpyglass("spyglass_small", SpyglassStandEntityModel::getSpyglassSmallTexturedModelData);

    private static EntityModelLayer register(String id, String name, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        EntityModelLayer layer = new EntityModelLayer(new Identifier(SpyglassPlus.MOD_ID, id), name);
        EntityModelLayerRegistry.registerModelLayer(layer, provider);
        return layer;
    }

    private static EntityModelLayer registerSpyglass(String id, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        return register("spyglass", id, provider);
    }

    private static EntityModelLayer main(String id, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        return register(id, "main", provider);
    }
}
