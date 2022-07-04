package com.github.teamfusion.spyglassplus.common.message;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TargetMessage {
	private int entityId;

	public TargetMessage(Entity entity) {
		this.entityId = entity.getId();
	}

	public TargetMessage(int entityId) {
		this.entityId = entityId;
	}


	public void serialize(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
	}

	public static TargetMessage deserialize(FriendlyByteBuf buffer) {
		return new TargetMessage(buffer.readInt());
	}

	public static boolean handle(TargetMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		context.enqueueWork(() -> {
			Player player = (Player) context.getSender().level.getEntity(message.entityId);


			if (player == null) return;

			if (player instanceof ISpyable) {
				((ISpyable) player).setCommand(true);
			}
		});

		return true;
	}
}