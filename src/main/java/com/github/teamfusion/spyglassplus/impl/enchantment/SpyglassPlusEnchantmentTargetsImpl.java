package com.github.teamfusion.spyglassplus.impl.enchantment;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import net.minecraft.util.Identifier;
import net.moddingplayground.frame.api.enchantment.v0.FrameEnchantmentTargetsEntrypoint;
import net.moddingplayground.frame.api.enchantment.v0.target.EnchantmentTargetInfo;
import net.moddingplayground.frame.impl.enchantment.EnchantmentTargetManager;

public final class SpyglassPlusEnchantmentTargetsImpl implements FrameEnchantmentTargetsEntrypoint {
    public static EnchantmentTargetInfo SCOPING;

    @Override
    public void registerEnchantmentTargets(EnchantmentTargetManager manager) {
        SCOPING = manager.register(new Identifier(SpyglassPlus.MOD_ID, "scoping"), "com.github.teamfusion.spyglassplus.api.enchantment.target.ScopingEnchantmentTarget");
    }
}
