package com.github.teamfusion.spyglassplus.api.item;

import com.github.teamfusion.spyglassplus.mixin.SpyglassItemMixin;
import net.minecraft.item.SpyglassItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

/**
 * Injected interface to {@link SpyglassItem}.
 * @see SpyglassItemMixin
 */
public interface ISpyglass {
    default SoundEvent getUseSound() {
        return SoundEvents.ITEM_SPYGLASS_USE;
    }

    default SoundEvent getStopUsingSound() {
        return SoundEvents.ITEM_SPYGLASS_STOP_USING;
    }
}
