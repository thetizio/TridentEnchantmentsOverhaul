package com.tizio.tridentenchantmentsoverhaul.mixin;

import com.tizio.tridentenchantmentsoverhaul.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ImpalingChannelingRemoveLoyalty extends AbstractArrow {

    @Shadow
    private boolean dealtDamage;

    protected ImpalingChannelingRemoveLoyalty(EntityType<? extends AbstractArrow> entity, Level level) {
        super(entity, level);
    }

    @Inject(at = @At("TAIL"), method = "onHitEntity")
    private void impalingChanneling(EntityHitResult entityHitResult, CallbackInfo ci) {

        if (Config.IMPALING_PIERCE.get()){
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.IMPALING,this.getPickupItem())>0) {
                this.dealtDamage = false;
                this.setDeltaMovement(this.getDeltaMovement().multiply(-100, -10, -100));
            }
        }

        if (Config.CHANNELING_UNIVERSAL.get()) {
            if (this.level() instanceof ServerLevel && EnchantmentHelper.hasChanneling(this.getPickupItem())) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(this.blockPosition()));
                    this.level().addFreshEntity(lightningbolt);
                }
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