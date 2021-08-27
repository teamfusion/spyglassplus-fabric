package com.github.teamfusion.spyglassplus.core;

import com.github.teamfusion.spyglassplus.core.mixin.EnchantmentTargetAccess;
import net.minecraft.item.Item;
import net.minecraft.item.SpyglassItem;

public class SpyglassTarget extends EnchantmentTargetAccess {
    @Override
    public boolean isAcceptableItem(Item item) {
        return item instanceof SpyglassItem;
    }
}
