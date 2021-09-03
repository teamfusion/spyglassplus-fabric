package com.github.teamfusion.spyglassplus.core.registry;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.common.network.S2C_ScrutinyReset;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class SpyglassPlusNetwork {
    public static final Identifier SCRUTINY_RESET = new Identifier(SpyglassPlus.MOD_ID, "scrutiny_reset");

    @Environment(EnvType.CLIENT)
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(SCRUTINY_RESET, new S2C_ScrutinyReset());
    }
}
