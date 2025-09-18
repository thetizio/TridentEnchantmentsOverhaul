package com.tizio.tridentenchantmentsoverhaul.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class ImpalingChannelingRemoveLoyalty extends PersistentProjectileEntity {

    @Shadow
    private boolean dealtDamage;

    protected ImpalingChannelingRemoveLoyalty(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("TAIL"), method = "onEntityHit")
    private void impalingChanneling(EntityHitResult entityHitResult, CallbackInfo ci) {
        if(EnchantmentHelper.getLevel(Enchantments.IMPALING,this.asItemStack())>0) {
            this.dealtDamage = false;
            this.setVelocity(this.getVelocity().multiply(-100, -10, -100));
        }

        if (this.getWorld() instanceof ServerWorld && EnchantmentHelper.hasChanneling(this.asItemStack())) {
            LightningEntity lightningEntity = (LightningEntity)EntityType.LIGHTNING_BOLT.create(this.getWorld());
            if (lightningEntity != null) {
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(entityHitResult.getEntity().getBlockPos()));
                this.getWorld().spawnEntity(lightningEntity);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void removeLoyalty(CallbackInfo ci) {
        super.tick();
        ci.cancel();
    }

}