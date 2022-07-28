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

    static SoundEvent register(String id) {
        Identifier identifier = new Identifier(SpyglassPlus.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
