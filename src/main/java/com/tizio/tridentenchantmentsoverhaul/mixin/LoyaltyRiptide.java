package com.tizio.tridentenchantmentsoverhaul.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.item.TridentItem.class)
public abstract class LoyaltyRiptide extends Item implements Vanishable {

    public LoyaltyRiptide(Properties settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "releaseUsing", cancellable = true)
    private void loyalty(ItemStack stack, Level level, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {

        if (user instanceof Player player) {
            if (!level.isClientSide) {
                if (EnchantmentHelper.getLoyalty(stack) > 0) {
                    if(this.getUseDuration(stack) - remainingUseTicks >= 10) {
                        stack.hurtAndBreak(1, player, (player1) -> {player1.broadcastBreakEvent(user.getUsedItemHand());});
                        ThrownTrident throwntrident = new ThrownTrident(level, player, stack);
                        throwntrident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                        throwntrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

                        level.addFreshEntity(throwntrident);
                        level.playSound((Player)null, throwntrident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                    ci.cancel();
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "releaseUsing")
    private void riptide(ItemStack stack, Level level, LivingEntity user, int remainingUseTicks, CallbackInfo info) {

        if (user instanceof Player player) {
            if (this.getUseDuration(stack) - remainingUseTicks >= 10) {
                if (player.isInWaterOrRain()) {
                    if (EnchantmentHelper.getRiptide(stack) > 0) player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,100,0));
                }
            }
        }

    }

}