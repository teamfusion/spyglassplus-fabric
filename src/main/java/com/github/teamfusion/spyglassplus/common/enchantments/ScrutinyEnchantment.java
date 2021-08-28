package com.github.teamfusion.spyglassplus.common.enchantments;

public class ScrutinyEnchantment extends SPEnchantment {

    public ScrutinyEnchantment () {
        super(Rarity.UNCOMMON);
    }

    @Override
    public int getMinPower(int level) {
        return 15;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return true;
    }
}
