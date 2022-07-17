package com.github.teamfusion.spyglassplus.client;

import com.github.teamfusion.spyglassplus.SpyglassPlus;
import com.github.teamfusion.spyglassplus.client.model.SmallSpyglassStandModel;
import com.github.teamfusion.spyglassplus.client.model.SpyglassStandModel;
import com.github.teamfusion.spyglassplus.client.render.SpyglassStandRender;
import com.github.teamfusion.spyglassplus.core.registry.SpyglassPlusEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpyglassPlus.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SpyglassPlusEntityTypes.SPYGLASS_STAND.get(), SpyglassStandRender::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayer.SPYGLASS_STAND, SpyglassStandModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayer.SMALL_SPYGLASS_STAND, SmallSpyglassStandModel::createBodyLayer);
	}
}
