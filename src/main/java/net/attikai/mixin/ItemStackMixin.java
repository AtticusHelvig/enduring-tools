package net.attikai.mixin;

import net.attikai.tag.ModTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean isIn(TagKey<Item> tag);
    @Shadow public abstract int getMaxDamage();
    @Shadow public abstract int getDamage();

    @Inject(method = "calculateDamage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;)I", at = @At("HEAD"), cancellable = true)
    private void modifyDamageCalculation(int baseDamage, ServerWorld world, ServerPlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        // do nothing if not tagged
        if (!this.isIn(ModTags.SHOULD_ENDURE)) {
            return;
        }
        // if it won't break let it be
        if (getDamage() + baseDamage < getMaxDamage()) {
            return;
        }
        // survive on 1 durability
        cir.setReturnValue(getMaxDamage() - getDamage() - 1);
        cir.cancel();
    }
}
