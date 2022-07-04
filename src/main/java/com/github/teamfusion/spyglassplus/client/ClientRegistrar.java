package com.github.teamfusion.spyglassplus.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientRegistrar {
	public static final KeyMapping KEY_BIND_SPYGLASS_SET_TARGET = new KeyMapping(
			"key.spyglassplus.set_target", InputConstants.Type.MOUSE, 0, "key.categories.gameplay");

	public static final KeyMapping KEY_BIND_SPYGLASS_RESET_TARGET = new KeyMapping(
			"key.spyglassplus.reset_target", InputConstants.Type.MOUSE, 0, "key.categories.gameplay");


	public static void setup(FMLCommonSetupEvent event) {
		ClientRegistry.registerKeyBinding(KEY_BIND_SPYGLASS_SET_TARGET);
		ClientRegistry.registerKeyBinding(KEY_BIND_SPYGLASS_RESET_TARGET);
	}
}
