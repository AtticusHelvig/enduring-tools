package net.attikai.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.attikai.EnduringTools;
import net.attikai.tag.ModTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Unique
    private static final float BROKEN_DAMAGE_MODIFIER = 1.0f / 8.0f;

    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), name = "damage", argsOnly = true)
    float modifyDamage(float damage, @Local(argsOnly = true, name = "source") DamageSource source) {
        ItemStack weapon = source.getWeaponItem();
        // don't modify non-weapon attacks
        if (weapon == null) {
            return damage;
        }
        // modify only tagged items
        if (!weapon.is(ModTags.SHOULD_ENDURE)) {
            return damage;
        }
        // and only if they're broken
        if (weapon.nextDamageWillBreak()) {
            EnduringTools.LOGGER.info("Damage modified.");
            return damage * BROKEN_DAMAGE_MODIFIER;
        }
        return damage;
    }
}
