package com.github.teamfusion.spyglassplus.common.enchantments;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class DiscoveryEnchantment extends Enchantment {

    public DiscoveryEnchantment() {
        super(Rarity.UNCOMMON, SpyglassPlusEnchantments.SPYGLASS, new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return true;
    }
}
