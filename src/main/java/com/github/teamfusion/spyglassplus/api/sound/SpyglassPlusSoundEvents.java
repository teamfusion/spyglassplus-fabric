package com.github.teamfusion.spyglassplus.api.sound;

import com.github.teamfusion.spyglassplus.api.SpyglassPlus;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface SpyglassPlusSoundEvents {
    SoundEvent ITEM_BINOCULARS_USE = binoculars("use");
    SoundEvent ITEM_BINOCULARS_STOP_USING = binoculars("stop_using");
    SoundEvent ITEM_BINOCULARS_ADJUST = binoculars("adjust");
    SoundEvent ITEM_BINOCULARS_RESET_ADJUST = binoculars("reset_adjust");

    private static SoundEvent binoculars(String id) {
        return item("binoculars", id);
    }

    SoundEvent ITEM_SPYGLASS_ADJUST = spyglass("adjust");
    SoundEvent ITEM_SPYGLASS_RESET_ADJUST = spyglass("reset_adjust");

    static SoundEvent spyglass(String id) {
        return item("spyglass", id);
    }

    static SoundEvent item(String item, String id) {
        return register("item.%s.%s".formatted(item, id));
    }

    SoundEvent ENTITY_SPYGLASS_STAND_PLACE = spyglassStand("place");
    SoundEvent ENTITY_SPYGLASS_STAND_BREAK = spyglassStand("break");
    SoundEvent ENTITY_SPYGLASS_STAND_HIT = spyglassStand("hit");
    SoundEvent ENTITY_SPYGLASS_STAND_FALL = spyglassStand("fall");
    SoundEvent ENTITY_SPYGLASS_STAND_SHRINK = spyglassStand("shrink");
    SoundEvent ENTITY_SPYGLASS_STAND_ENLARGE = spyglassStand("enlarge");

    static SoundEvent spyglassStand(String id) {
        return entity("spyglass_stand", id);
    }

    static SoundEvent entity(String entity, String id) {
        return register("entity.%s.%s".formatted(entity, id));
    }

    static SoundEvent register(String id) {
        Identifier identifier = new Identifier(SpyglassPlus.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
