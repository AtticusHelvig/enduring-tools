package net.attikai.mixin;

import net.attikai.EnduringTools;
import net.attikai.tag.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends PlayerLikeEntity {
    @Shadow @Final
    PlayerInventory inventory;
    @Unique
    private static final float BROKEN_MODIFIER = 1.0f / 8.0f;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F", at = @At(value = "RETURN"), cancellable = true)
    private void modifyBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        ItemStack tool = this.inventory.getSelectedStack();
        if (!tool.isIn(ModTags.SHOULD_ENDURE)) {
            return;
        }
        if (tool.getMaxDamage() - tool.getDamage() != 1) {
            return;
        }
        // emulate vanilla efficiency behavior
        float efficiency = tool.getMiningSpeedMultiplier(block);
        if (efficiency > 1f) {
            efficiency += (float) this.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);
        }
        float unbroken = cir.getReturnValueF();
        // don't be worse than hand mining
        float result = Math.max(unbroken / efficiency, unbroken * BROKEN_MODIFIER);
        cir.setReturnValue(result);
    }
}
