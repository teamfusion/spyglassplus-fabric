package com.github.teamfusion.spyglassplus.common.message;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpyglassStandResetMessage {
	private int entityId;

	public SpyglassStandResetMessage(Entity entity) {
		this.entityId = entity.getId();
	}

	public SpyglassStandResetMessage(int entityId) {
		this.entityId = entityId;
	}


	public void serialize(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
	}

	public static SpyglassStandResetMessage deserialize(FriendlyByteBuf buffer) {
		return new SpyglassStandResetMessage(buffer.readInt());
	}

	public static boolean handle(SpyglassStandResetMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		context.enqueueWork(() -> {
			Player player = (Player) context.getSender().level.getEntity(message.entityId);
			if (player == null) return;

			if (player instanceof ISpyable) {
				if (((ISpyable) player).getSpyGlassStands() != null) {
					((ISpyable) player).getSpyGlassStands().setXRot(((ISpyable) player).getCameraRotX());
					((ISpyable) player).getSpyGlassStands().setOwner(null);
				}
				((ISpyable) player).setSpyglassStands(null);
			}
		});

		return true;
	}
}