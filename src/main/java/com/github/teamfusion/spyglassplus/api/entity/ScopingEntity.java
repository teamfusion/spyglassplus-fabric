package com.github.teamfusion.spyglassplus.api.entity;

import net.minecraft.item.ItemStack;

public interface ScopingEntity {
    default ItemStack getScopingStack() {
        return ItemStack.EMPTY;
    }

    default boolean isScoping() {
        return false;
    }
}
