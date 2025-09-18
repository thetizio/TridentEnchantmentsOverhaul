package com.tizio.tridentenchantmentsoverhaul.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.Vanishable;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public abstract class LoyaltyRiptide extends Item implements Vanishable {

    public LoyaltyRiptide(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "onStoppedUsing", cancellable = true)
    private void loyalty(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {

        if (user instanceof PlayerEntity playerEntity) {
            if (!world.isClient) {
                if (EnchantmentHelper.getLoyalty(stack) > 0) {
                    if(this.getMaxUseTime(stack) - remainingUseTicks >= 10) {
                        stack.damage(1, playerEntity, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
                        TridentEntity tridentEntity = new TridentEntity(world, playerEntity, stack);
                        tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F, 1.0F);
                        tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;

                        world.spawnEntity(tridentEntity);
                        world.playSoundFromEntity((PlayerEntity)null, tridentEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                    ci.cancel();
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "onStoppedUsing")
    private void riptide(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {

        if (user instanceof PlayerEntity playerEntity) {
            if (this.getMaxUseTime(stack) - remainingUseTicks >= 10) {
                if (playerEntity.isTouchingWaterOrRain()) {
                    if (EnchantmentHelper.getRiptide(stack) > 0) playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,100,0));
                }
            }
        }

    }

}