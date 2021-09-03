package com.github.teamfusion.spyglassplus.common.network;

import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

public class S2C_ScrutinyReset implements ClientPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.execute(() -> {
            World world = client.world;

            if (world == null) return;

            ((ScrutinyAccess)client.mouse).setZero();
        });
    }
}
