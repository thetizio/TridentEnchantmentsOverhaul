package com.tizio.tridentenchantmentsoverhaul.mixin;

import com.tizio.tridentenchantmentsoverhaul.Config;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class Impaling extends AbstractArrow {

    @Shadow private boolean dealtDamage;

    protected Impaling(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "onHitEntity")
    private void impaling(EntityHitResult result, CallbackInfo ci) {
        if (Config.IMPALING_PIERCE.get()) {
            int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(this.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.IMPALING), this.getWeaponItem());
            if (enchantmentLevel > 0) {
                this.dealtDamage = false;
                this.setDeltaMovement(this.getDeltaMovement().multiply(-100, -10, -100));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void removeLoyalty(CallbackInfo ci) {
        if (Config.LOYALTY_LOYAL.get()) {
            super.tick();
            ci.cancel();
        }
    }

}