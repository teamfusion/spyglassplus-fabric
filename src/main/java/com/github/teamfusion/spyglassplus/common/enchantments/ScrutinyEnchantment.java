package com.github.teamfusion.spyglassplus.common.enchantments;

public class ScrutinyEnchantment extends SPEnchantment {

    public ScrutinyEnchantment () {
        super(Rarity.UNCOMMON);
    }

    @Override
    public int getMinCost(int level) {
        return 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
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
