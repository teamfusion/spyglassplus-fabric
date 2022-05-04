package com.github.teamfusion.spyglassplus.common.enchantments;

public class IlluminatingEnchantment extends SPEnchantment {

    public IlluminatingEnchantment() {
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
