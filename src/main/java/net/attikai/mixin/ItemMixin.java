package net.attikai.mixin;

import net.attikai.tag.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
    private void modifyName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
        if (!stack.is(ModTags.SHOULD_ENDURE)) {
            return;
        }
        if (stack.getMaxDamage() - stack.getDamageValue() <= 1) {
            Component name = Component.literal("Broken ").append(cir.getReturnValue());
            cir.setReturnValue(name);
        }
    }
}
