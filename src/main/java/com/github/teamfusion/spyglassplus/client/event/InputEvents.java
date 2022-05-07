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
	private static boolean resetKeyPush;
	private static boolean resetKeyPushed;

	private static int keyCountDown;

	@SubscribeEvent
	public static void onMouseClick(TickEvent.ClientTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;

		onInput(mc);

		if (mc.player != null && mc.player.isScoping() && keyPush) {
			if (keyCountDown <= 60) {
				keyCountDown += 1;
			} else {

				SpyglassPlus.CHANNEL.sendToServer(new TargetMessage(true));
				keyPush = false;
			}
		} else {
			keyCountDown = 0;
		}
	}

	private static void onInput(Minecraft mc) {
		if (ClientRegistrar.KEY_BIND_SPYGLASS_SET_TARGET.isDown()) {
			keyPush = true;
		}

		if (mc.player != null && mc.player.isScoping()) {

			resetKeyPush = ClientRegistrar.KEY_BIND_SPYGLASS_RESET_TARGET.isDown();

			if (resetKeyPush && !resetKeyPushed) {
				SpyglassPlus.CHANNEL.sendToServer(new ResetTargetMessage());

				resetKeyPushed = true;
			}

			if (!resetKeyPush) {
				resetKeyPushed = false;
			}
		}
	}
}
