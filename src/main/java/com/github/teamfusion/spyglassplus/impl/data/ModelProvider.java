package com.github.teamfusion.spyglassplus.impl.data;

import com.github.teamfusion.spyglassplus.api.item.SpyglassPlusItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.moddingplayground.frame.api.toymaker.v0.model.uploader.ItemModelUploader;

public final class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataGenerator gen) {
        super(gen);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        ItemModelUploader uploader = ItemModelUploader.of(gen);
        uploader.register(Models.GENERATED, SpyglassPlusItems.BINOCULARS);
    }
}
