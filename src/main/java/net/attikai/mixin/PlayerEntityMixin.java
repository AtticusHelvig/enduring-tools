package net.attikai.mixin;

import net.attikai.EnduringTools;
import net.attikai.tag.ModTags;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends Avatar {
    @Shadow @Final
    Inventory inventory;
    @Unique
    private static final float BROKEN_MODIFIER = 1.0f / 8.0f;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", at = @At(value = "RETURN"), cancellable = true)
    private void modifyBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        ItemStack tool = this.inventory.getSelectedItem();
        if (!tool.is(ModTags.SHOULD_ENDURE)) {
            return;
        }
        if (tool.getMaxDamage() - tool.getDamageValue() != 1) {
            return;
        }
        // emulate vanilla efficiency behavior
        float efficiency = tool.getDestroySpeed(block);
        if (efficiency > 1f) {
            efficiency += (float) this.getAttributeValue(Attributes.MINING_EFFICIENCY);
        }
        float unbroken = cir.getReturnValueF();
        // don't be worse than hand mining
        float result = Math.max(unbroken / efficiency, unbroken * BROKEN_MODIFIER);
        cir.setReturnValue(result);
    }
}
