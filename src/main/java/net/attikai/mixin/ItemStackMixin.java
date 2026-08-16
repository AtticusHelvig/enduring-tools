package net.attikai.mixin;

import net.attikai.tag.ModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemInstance {

    @Shadow public abstract int getMaxDamage();
    @Shadow public abstract int getDamageValue();
    @Shadow public abstract boolean nextDamageWillBreak();

    @Inject(method = "processDurabilityChange(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;)I", at = @At("HEAD"), cancellable = true)
    private void modifyDamageCalculation(int baseDamage, ServerLevel world, ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
        // do nothing if not tagged
        if (!this.is(ModTags.SHOULD_ENDURE)) {
            return;
        }
        // if it won't break let it be
        if (!this.nextDamageWillBreak()) {
            return;
        }
        // survive on 1 durability
        cir.setReturnValue(getMaxDamage() - getDamageValue() - 1);
        cir.cancel();
    }
}
