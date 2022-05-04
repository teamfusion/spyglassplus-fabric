package com.github.teamfusion.spyglassplus.common.message;

import com.github.teamfusion.spyglassplus.core.ScrutinyAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ScrutinyResetMessage {
	public ScrutinyResetMessage() {
	}

	public void serialize(FriendlyByteBuf buffer) {
	}

	public static ScrutinyResetMessage deserialize(FriendlyByteBuf buffer) {
		return new ScrutinyResetMessage();
	}

	public static boolean handle(ScrutinyResetMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Level world = Minecraft.getInstance().level;

				if (world == null) return;

				((ScrutinyAccess) Minecraft.getInstance().mouseHandler).setZero();
			});
		}

		return true;
	}
}