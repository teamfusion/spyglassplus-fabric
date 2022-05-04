package com.github.teamfusion.spyglassplus.common.enchantments;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class SPEnchantment extends Enchantment {

    public SPEnchantment(Rarity rarity) {
        super(rarity, SpyglassPlusEnchantments.SPYGLASS, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }
}