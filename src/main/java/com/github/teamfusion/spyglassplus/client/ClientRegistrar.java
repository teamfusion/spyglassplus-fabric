package com.github.teamfusion.spyglassplus.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.lwjgl.glfw.GLFW;

public class ClientRegistrar {
	public static final KeyMapping KEY_BIND_SPYGLASS_SET_TARGET = new KeyMapping(
			"key.spyglassplus.set_target", GLFW.GLFW_MOUSE_BUTTON_LEFT, "key.categories.misc");

	public static final KeyMapping KEY_BIND_SPYGLASS_RESET_TARGET = new KeyMapping(
			"key.spyglassplus.reset_target", GLFW.GLFW_MOUSE_BUTTON_LEFT, "key.categories.misc");


	public static void setup(FMLCommonSetupEvent event) {
		ClientRegistry.registerKeyBinding(KEY_BIND_SPYGLASS_SET_TARGET);
		ClientRegistry.registerKeyBinding(KEY_BIND_SPYGLASS_RESET_TARGET);
	}
}
