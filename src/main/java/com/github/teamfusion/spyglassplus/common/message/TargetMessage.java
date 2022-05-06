package com.github.teamfusion.spyglassplus.common.message;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TargetMessage {
	public boolean press;

	public TargetMessage(boolean press) {
		this.press = press;
	}

	public void serialize(FriendlyByteBuf buffer) {
		buffer.writeBoolean(this.press);
	}

	public static TargetMessage deserialize(FriendlyByteBuf buffer) {
		return new TargetMessage(buffer.readBoolean());
	}

	public static boolean handle(TargetMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		context.enqueueWork(() -> {
			Player player = context.getSender();

			if (player == null) return;

			if (player instanceof ISpyable) {
				((ISpyable) player).setCommand(message.press);
			}
		});

		return true;
	}
}