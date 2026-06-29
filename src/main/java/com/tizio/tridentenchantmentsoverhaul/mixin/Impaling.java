package com.tizio.tridentenchantmentsoverhaul.mixin;

import com.tizio.tridentenchantmentsoverhaul.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.entity.projectile.arrow.ThrownTrident.class)
public abstract class Impaling extends AbstractArrow {

    @Shadow private boolean dealtDamage;

    protected Impaling(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "onHitEntity", cancellable = true)
    private void impaling(EntityHitResult result, CallbackInfo ci) {

        if (Config.IMPALING_PIERCE.get()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(this.level().registryAccess().getOrThrow(Enchantments.IMPALING), this.getWeaponItem()) > 0) {

                Entity entity = result.getEntity();
                float f = 8.0F;
                Entity entity1 = this.getOwner();
                DamageSource damagesource = this.damageSources().trident(this, (Entity)(entity1 == null ? this : entity1));
                Level level = this.level();
                if (level instanceof ServerLevel serverlevel) {
                    f = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, f);
                }

                if (entity.hurtOrSimulate(damagesource, f)) {
                    if (entity.getType() == EntityType.ENDERMAN) {
                        return;
                    }

                    level = this.level();
                    if (level instanceof ServerLevel) {
                        ServerLevel serverlevel1 = (ServerLevel)level;
                        EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverlevel1, entity, damagesource, this.getWeaponItem(), (p_478671_) -> this.kill(serverlevel1));
                    }

                    if (entity instanceof LivingEntity) {
                        LivingEntity livingentity = (LivingEntity)entity;
                        this.doKnockback(livingentity, damagesource);
                        this.doPostHurtEffects(livingentity);
                    }
                }
                super.tick();
                ci.cancel();
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