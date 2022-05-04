package com.github.teamfusion.spyglassplus.common.enchantments;

public class IndicateEnchantment extends SPEnchantment{

    public IndicateEnchantment() {
        super(Rarity.UNCOMMON);
    }

    @Override
	public int getMinCost(int level) {
		return 15;
	}

    @Override
    public int getMaxLevel() {
        return 1;
    }

	@Override
	public boolean isTradeable() {
		return true;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}

}
