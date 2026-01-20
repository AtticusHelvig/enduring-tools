package net.attikai.mixin;

import net.attikai.tag.ModTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", at = @At("RETURN"), cancellable = true)
    private void modifyName(ItemStack stack, CallbackInfoReturnable<Text> cir) {
        if (!stack.isIn(ModTags.SHOULD_ENDURE)) {
            return;
        }
        if (stack.getMaxDamage() - stack.getDamage() <= 1) {
            Text name = Text.literal("Broken ").append(cir.getReturnValue());
            cir.setReturnValue(name);
        }
    }
}
