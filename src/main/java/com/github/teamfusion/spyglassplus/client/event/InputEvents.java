package com.github.teamfusion.spyglassplus.client.event;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.client.ClientRegistrar;
import com.github.teamfusion.spyglassplus.common.message.ResetTargetMessage;
import com.github.teamfusion.spyglassplus.common.message.TargetMessage;
import com.github.teamfusion.spyglassplus.core.ISpyable;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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

		//reset spyglass stands look
		if (Minecraft.getInstance().player != Minecraft.getInstance().cameraEntity && Minecraft.getInstance().player.isShiftKeyDown()) {
			Minecraft.getInstance().setCameraEntity(Minecraft.getInstance().player);
			((ISpyable) Minecraft.getInstance().player).setSpyglassStands(null);
			((ISpyable) Minecraft.getInstance().player).setCameraRotX(0.0F);
			((ISpyable) Minecraft.getInstance().player).setCameraRotY(0.0F);
		}

		if (!keyPush) {
			keyPushed = false;
		}
	}

	private static void onInput(Minecraft mc) {
        int commandLevel = Math.max(EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.COMMAND.get(), mc.player.getItemInHand(InteractionHand.MAIN_HAND)), EnchantmentHelper.getItemEnchantmentLevel(SpyglassPlusEnchantments.COMMAND.get(), mc.player.getItemInHand(InteractionHand.OFF_HAND)));

        if (mc.player instanceof ISpyable && commandLevel > 0) {
            if (mc.player.isScoping()) {
                keyPush = ClientRegistrar.KEY_BIND_SPYGLASS_SET_TARGET.isDown();
            }
            resetKeyPush = ClientRegistrar.KEY_BIND_SPYGLASS_RESET_TARGET.isDown();
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
