package com.github.teamfusion.spyglassplus;

import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEnchantments;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusItems;
import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpyglassPlus implements ModInitializer {
	public static Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "spyglassplus";
	public static final String MOD_NAME = "Spyglass+";

	public static final ItemGroup SPYGLASSPLUS_TAB = FabricItemGroupBuilder.create(
			new Identifier(MOD_ID, "spyglassplus_tab"))
			.icon(() -> new ItemStack(Items.SPYGLASS))
			.build();

	@Override
	public void onInitialize() {
		Reflection.initialize(
				SpyglassPlusEnchantments.class,
				SpyglassPlusItems.class
		);
		SpyglassPlusItems.register();

		log(Level.INFO, "Enhancing Spyglasses!");
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	public static void log(Level level, String message){
		LOGGER.log(level, "["+MOD_NAME+"] " + message);
	}
}
