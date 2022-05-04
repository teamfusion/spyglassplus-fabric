package com.github.teamfusion.spyglassplus.core;

import com.github.teamfusion.spyglassplus.core.mixin.EnchantmentTargetAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpyglassItem;

public class SpyglassTarget extends EnchantmentTargetAccess {
	@Override
	public boolean canEnchant(Item item) {
		return item instanceof SpyglassItem;
	}
}
