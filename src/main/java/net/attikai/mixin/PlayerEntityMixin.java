package net.attikai.mixin;

import net.attikai.tag.ModTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow @Final
    PlayerInventory inventory;
    @Unique
    private static final float BROKEN_MODIFIER = 1.0f / 8.0f;

    @ModifyVariable(method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", shift = At.Shift.AFTER), ordinal = 0)
    private float modifyBreakingSpeed(float value) {
        ItemStack tool = this.inventory.getSelectedStack();
        if (!tool.isIn(ModTags.SHOULD_ENDURE)) {
            return value;
        }
        if (tool.getMaxDamage() - tool.getDamage() != 1) {
            return value;
        }
        return value * BROKEN_MODIFIER;
    }
}
