package com.tizio.tridentenchantmentsoverhaul.mixin;

import com.tizio.tridentenchantmentsoverhaul.config.Config;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class Impaling extends PersistentProjectileEntity {

    protected Impaling(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "onEntityHit", cancellable = true)
    private void impaling(EntityHitResult entityHitResult, CallbackInfo ci) {

        if (Config.impalingPierce) {
            if (EnchantmentHelper.getLevel(this.getEntityWorld().getRegistryManager().getEntryOrThrow(Enchantments.IMPALING),this.getWeaponStack())>0) {

                Entity entity = entityHitResult.getEntity();
                float f = 8.0F;
                Entity entity2 = this.getOwner();
                DamageSource damageSource = this.getDamageSources().trident(this, (Entity) (entity2 == null ? this : entity2));
                World var7 = this.getEntityWorld();
                if (var7 instanceof ServerWorld serverWorld) {
                    f = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, f);
                }

                if (entity.sidedDamage(damageSource, f)) {
                    if (entity.getType() == EntityType.ENDERMAN) {
                        return;
                    }

                    var7 = this.getEntityWorld();
                    if (var7 instanceof ServerWorld) {
                        ServerWorld serverWorld = (ServerWorld) var7;
                        EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack(), (item) -> this.kill(serverWorld));
                    }

                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        this.knockback(livingEntity, damageSource);
                        this.onHit(livingEntity);
                    }
                }
                super.tick();
                ci.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void removeLoyalty(CallbackInfo ci) {
        if (Config.loyaltyInventory) {
            super.tick();
            ci.cancel();
        }
    }

}