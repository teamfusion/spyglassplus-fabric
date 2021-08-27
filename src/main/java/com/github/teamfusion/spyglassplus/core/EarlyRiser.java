package com.github.teamfusion.spyglassplus.core;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class EarlyRiser implements Runnable {
    @Override
    public void run() {
        MappingResolver mapResolver = FabricLoader.getInstance().getMappingResolver();

        var enchTargetClass = mapResolver.mapClassName("intermediary", "net.minecraft.class_1886");

        var enchTargetAdder = ClassTinkerers.enumBuilder(enchTargetClass, new Class[0]);
        enchTargetAdder.addEnumSubclass("SPYGLASS", "com.github.teamfusion.spyglassplus.core.SpyglassTarget");
        enchTargetAdder.build();
    }
}
