package com.github.teamfusion.spyglassplus.common.message;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpyglassCameraRotateMessage {
	private int entityId;
	private float rotX;
	private float rotY;

	public SpyglassCameraRotateMessage(Entity entity) {
		this.entityId = entity.getId();
		this.rotX = entity.getXRot();
		this.rotY = entity.getYRot();
	}

	public SpyglassCameraRotateMessage(int entityId, float rotX, float rotY) {
		this.entityId = entityId;
		this.rotX = rotX;
		this.rotY = rotY;
	}


	public void serialize(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeFloat(this.rotX);
		buffer.writeFloat(this.rotY);
	}

	public static SpyglassCameraRotateMessage deserialize(FriendlyByteBuf buffer) {
		return new SpyglassCameraRotateMessage(buffer.readInt(), buffer.readFloat(), buffer.readFloat());
	}

	public static boolean handle(SpyglassCameraRotateMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		context.enqueueWork(() -> {
			Entity entity = context.getSender().level.getEntity(message.entityId);
			if (entity == null) return;

			if (entity instanceof ISpyable) {
				((ISpyable) entity).setCameraRotX(message.rotX);
				((ISpyable) entity).setCameraRotY(message.rotY);
			}
		});

		return true;
	}
}