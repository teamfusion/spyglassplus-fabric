package com.github.teamfusion.spyglassplus.core.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpyglassItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpyglassItem.class)
public class SpyglassItemMixin extends Item {

    public SpyglassItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantability() {
        return 1;
    }
}