package com.github.teamfusion.spyglassplus.client.event;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.client.ClientRegistrar;
import com.github.teamfusion.spyglassplus.common.message.ResetTargetMessage;
import com.github.teamfusion.spyglassplus.common.message.TargetMessage;
import com.github.teamfusion.spyglassplus.core.ISpyable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpyglassPlus.MOD_ID, value = Dist.CLIENT)
public class InputEvents {
	private static boolean keyPush;
	private static boolean keyPushed;
	private static boolean resetKeyPush;
	private static boolean resetKeyPushed;

	@SubscribeEvent
	public static void onMouseClick(TickEvent.ClientTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;

		onInput(mc);

		if (mc.player.isScoping() && keyPush && !keyPushed) {
			SpyglassPlus.CHANNEL.sendToServer(new TargetMessage(mc.player.getId()));
			keyPushed = true;

		}

		if (!keyPush) {
			keyPushed = false;
		}
	}

	private static void onInput(Minecraft mc) {
		if (mc.player instanceof ISpyable && mc.player.isScoping()) {
			if (!((ISpyable) mc.player).isCommand()) {
				keyPush = ClientRegistrar.KEY_BIND_SPYGLASS_SET_TARGET.isDown();
			} else {
				resetKeyPush = ClientRegistrar.KEY_BIND_SPYGLASS_RESET_TARGET.isDown();
			}
		}


		if (resetKeyPush && !resetKeyPushed) {
			SpyglassPlus.CHANNEL.sendToServer(new ResetTargetMessage(mc.player.getId()));

			resetKeyPushed = true;
		}

		if (!resetKeyPush) {
			resetKeyPushed = false;
		}
	}
}
