package com.github.teamfusion.spyglassplus.client.event;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.client.ClientRegistrar;
import com.github.teamfusion.spyglassplus.common.message.ResetTargetMessage;
import com.github.teamfusion.spyglassplus.common.message.TargetMessage;
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

	private static int keyCountDown;

	private static int keyCooldown;

	@SubscribeEvent
	public static void onMouseClick(TickEvent.ClientTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;

		onInput(mc);

		if (mc.player.isScoping() && keyPush && !keyPushed) {
			if (keyCountDown <= 60) {
				keyCountDown += 1;
				keyCooldown = 60;
			} else {

				SpyglassPlus.CHANNEL.sendToServer(new TargetMessage(mc.player.getId()));
				keyPushed = true;
			}
		} else {
			if (keyCooldown > 0) {
				keyCooldown -= 1;
			}

			if (keyCountDown > 0 && keyCooldown <= 0) {
				keyCountDown -= 1;
			}
		}

		if (!keyPush) {
			keyPushed = false;
		}
	}

	private static void onInput(Minecraft mc) {

		if (mc.player.isScoping()) {
			keyPush = ClientRegistrar.KEY_BIND_SPYGLASS_SET_TARGET.isDown();
			resetKeyPush = ClientRegistrar.KEY_BIND_SPYGLASS_RESET_TARGET.isDown();

			if (resetKeyPush && !resetKeyPushed) {
				SpyglassPlus.CHANNEL.sendToServer(new ResetTargetMessage(mc.player.getId()));

				resetKeyPushed = true;
			}

			if (!resetKeyPush) {
				resetKeyPushed = false;
			}
		}
	}
}
