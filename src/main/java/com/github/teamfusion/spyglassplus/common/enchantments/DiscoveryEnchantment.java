package com.github.teamfusion.spyglassplus.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class DiscoveryEnchantment extends Enchantment {

    public DiscoveryEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }
}
