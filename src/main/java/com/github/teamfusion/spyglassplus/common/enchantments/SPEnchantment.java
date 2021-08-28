package com.github.teamfusion.spyglassplus.common.enchantments;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class SPEnchantment extends Enchantment {

    public SPEnchantment(Rarity rarity) {
        super(rarity, SpyglassPlusEnchantments.SPYGLASS, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }
}