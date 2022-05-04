package com.github.teamfusion.spyglassplus.common.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.Level;

public class BinocularsItem extends SpyglassItem {
	public BinocularsItem(Item.Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
		user.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
		user.awardStat(Stats.ITEM_USED.get(this));
		return super.use(level, user, hand);
	}
}
