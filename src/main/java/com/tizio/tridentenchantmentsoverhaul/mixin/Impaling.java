package com.tizio.tridentenchantmentsoverhaul.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class Impaling extends PersistentProjectileEntity {

    @Shadow private boolean dealtDamage;

    protected Impaling(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("TAIL"), method = "onEntityHit")
    private void init(EntityHitResult entityHitResult, CallbackInfo ci) {
        World world = this.getWorld();
        int enchantmentLevel = EnchantmentHelper.getLevel((RegistryEntry<net.minecraft.enchantment.Enchantment>) world.getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.IMPALING),this.asItemStack());
        if(enchantmentLevel>0) {
            this.dealtDamage = false;
            this.setVelocity(this.getVelocity().multiply(-100, -10, -100));
        }
    }
}