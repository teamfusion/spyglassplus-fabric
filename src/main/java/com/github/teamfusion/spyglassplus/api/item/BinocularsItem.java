package com.github.teamfusion.spyglassplus.api.item;

import com.github.teamfusion.spyglassplus.api.sound.SpyglassPlusSoundEvents;
import net.minecraft.sound.SoundEvent;

public class BinocularsItem extends CustomSpyglassItem {
    public BinocularsItem(Settings settings) {
        super(settings);
    }

    @Override
    public SoundEvent getUseSound() {
        return SpyglassPlusSoundEvents.ITEM_BINOCULARS_USE;
    }

    @Override
    public SoundEvent getStopUsingSound() {
        return SpyglassPlusSoundEvents.ITEM_BINOCULARS_STOP_USING;
    }

    @Override
    public SoundEvent getAdjustSound() {
        return SpyglassPlusSoundEvents.ITEM_BINOCULARS_ADJUST;
    }

    @Override
    public SoundEvent getResetAdjustSound() {
        return SpyglassPlusSoundEvents.ITEM_BINOCULARS_RESET_ADJUST;
    }
}
