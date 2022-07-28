package com.github.teamfusion.spyglassplus.mixin;

import com.github.teamfusion.spyglassplus.api.item.ISpyglass;
import net.minecraft.item.SpyglassItem;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Implements our {@link ISpyglass} interface to {@link SpyglassItem}.
 */
@Mixin(SpyglassItem.class)
public class SpyglassItemMixin implements ISpyglass {
}
