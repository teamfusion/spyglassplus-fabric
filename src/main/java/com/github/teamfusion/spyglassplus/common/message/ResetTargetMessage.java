package com.github.teamfusion.spyglassplus.common.message;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ResetTargetMessage {

	private int entityId;

	public ResetTargetMessage(Entity entity) {
		this.entityId = entity.getId();
	}

	public ResetTargetMessage(int entityId) {
		this.entityId = entityId;
	}


	public void serialize(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
	}

	public static ResetTargetMessage deserialize(FriendlyByteBuf buffer) {
		return new ResetTargetMessage(buffer.readInt());
	}

	public static boolean handle(ResetTargetMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		context.enqueueWork(() -> {
			Player player = (Player) context.getSender().level.getEntity(message.entityId);

			if (player == null) return;

			if (player instanceof ISpyable) {
				((ISpyable) player).setCommand(false);
			}

            AABB box = new AABB(player.blockPosition()).inflate(32.0D);
            List<TamableAnimal> nearbyTamableAnimals = player.level.getEntitiesOfClass(TamableAnimal.class, box, TamableAnimal::isTame);
            List<AbstractGolem> nearbyGolems = player.level.getEntitiesOfClass(AbstractGolem.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);

            for (TamableAnimal tamableAnimal : nearbyTamableAnimals) {
                if (tamableAnimal.isOwnedBy(player)) {
                    tamableAnimal.setTarget(null);
                }
            }
            for (AbstractGolem golemEntity : nearbyGolems) {
                if (golemEntity.getTarget() != player) {
                    golemEntity.setTarget(null);
                }
            }

		});

		return true;
	}
}