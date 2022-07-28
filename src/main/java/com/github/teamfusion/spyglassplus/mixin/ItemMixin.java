package com.github.teamfusion.spyglassplus.mixin;

import com.github.teamfusion.spyglassplus.api.item.ISpyglass;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    /**
     * Makes all instances of {@link ISpyglass} enchantable.
     */
    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    private void fixSpyglassIsEnchantable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Item that = (Item) (Object) this;
        if (that instanceof ISpyglass) cir.setReturnValue(true);
    }
}
