package com.github.teamfusion.spyglassplus.api.enchantment;

public class IlluminateEnchantment extends ScopingEnchantment {
    public IlluminateEnchantment(Rarity weight) {
        super(weight);
    }

    @Override
    public int getMinPower(int level) {
        return 15;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
