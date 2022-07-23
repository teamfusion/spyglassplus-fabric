package com.github.teamfusion.spyglassplus.impl.data;

import com.github.teamfusion.spyglassplus.api.tag.SpyglassPlusItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import static com.github.teamfusion.spyglassplus.api.item.SpyglassPlusItems.*;
import static net.minecraft.item.Items.*;

public final class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ItemTagProvider(FabricDataGenerator gen, BlockTagProvider blockTagProvider) {
        super(gen, blockTagProvider);
    }

    @Override
    protected void generateTags() {
        this.getOrCreateTagBuilder(SpyglassPlusItemTags.SCOPING_ITEMS).add(
            SPYGLASS,
            BINOCULARS
        );
    }
}
