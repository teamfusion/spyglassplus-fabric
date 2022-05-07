package com.github.teamfusion.spyglassplus.common.message;

import com.github.teamfusion.spyglassplus.core.ISpyable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.TamableAnimal;
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
	public ResetTargetMessage() {
	}

	public void serialize(FriendlyByteBuf buffer) {
	}

	public static ResetTargetMessage deserialize(FriendlyByteBuf buffer) {
		return new ResetTargetMessage();
	}

	public static boolean handle(ResetTargetMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		context.enqueueWork(() -> {
			Player player = context.getSender();

			if (player == null) return;

			if (player instanceof ISpyable) {
				((ISpyable) player).setCommand(false);
			}

			AABB box = new AABB(player.blockPosition()).inflate(6.0D);
			List<Wolf> nearbyWolves = player.level.getEntitiesOfClass(Wolf.class, box, TamableAnimal::isTame);
			List<IronGolem> nearbyIronGolems = player.level.getEntitiesOfClass(IronGolem.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
			List<SnowGolem> nearbySnowGolems = player.level.getEntitiesOfClass(SnowGolem.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
			List<Fox> nearbyFoxes = player.level.getEntitiesOfClass(Fox.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
			List<Axolotl> nearbyAxolotl = player.level.getEntitiesOfClass(Axolotl.class, box, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
			for (Wolf wolfEntity : nearbyWolves) {
				wolfEntity.setTarget(null);
			}
			for (IronGolem ironGolemEntity : nearbyIronGolems) {
				ironGolemEntity.setTarget(null);

			}
			for (SnowGolem snowGolemEntity : nearbySnowGolems) {
				snowGolemEntity.setTarget(null);
			}
			for (Fox foxEntity : nearbyFoxes) {
				foxEntity.setTarget(null);
			}

			for (Axolotl axolotlEntity : nearbyAxolotl) {
				axolotlEntity.setTarget(null);
			}

		});

		return true;
	}
}