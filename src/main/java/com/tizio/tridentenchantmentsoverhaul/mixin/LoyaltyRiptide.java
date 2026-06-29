package com.tizio.tridentenchantmentsoverhaul.mixin;

import com.tizio.tridentenchantmentsoverhaul.Config;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.TridentItem.class)
public abstract class LoyaltyRiptide extends Item implements ProjectileItem {

    public LoyaltyRiptide(Properties properties) {
        super(properties);
    }

    @Inject(at = @At("HEAD"), method = "releaseUsing", cancellable = true)
    private void loyalty(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft, CallbackInfoReturnable<Boolean> ci) {

        if (entityLiving instanceof Player player) {
            if (level instanceof ServerLevel) {
                int loyaltyLevel = EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().getOrThrow(Enchantments.LOYALTY),stack);
                if (loyaltyLevel > 0) {
                    if(this.getUseDuration(stack, player) - timeLeft >= 10) {
                        stack.hurtAndBreak(1, player, entityLiving.getUsedItemHand());
                        ThrownTrident throwntrident = new ThrownTrident(level, player, stack);
                        throwntrident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,
                                2.5F+Config.LOYALTY_THROW.get().floatValue()*loyaltyLevel,
                                1.0F-Config.LOYALTY_INACCURACY_DECREASE.get().floatValue()*loyaltyLevel > 0 ? 1.0F-Config.LOYALTY_INACCURACY_DECREASE.get().floatValue()*loyaltyLevel : 0);
                        if (Config.LOYALTY_LOYAL.get() || player.hasInfiniteMaterials()) {
                            throwntrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(throwntrident);
                        Holder<SoundEvent> holder = (Holder)EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
                        level.playSound((Player)null, throwntrident, (SoundEvent)holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        if (!Config.LOYALTY_LOYAL.get() && !player.hasInfiniteMaterials()) {
                            player.getInventory().removeItem(stack);
                        }
                    }
                    ci.cancel();
                }
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;push(DDD)V"), method = "releaseUsing")
    private void riptide(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft, CallbackInfoReturnable<Boolean> ci) {

        if (Config.RIPTIDE_SLOW_FALL.get()) {
            entityLiving.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, Config.RIPTIDE_DURATION.get()*20, 0));
        }

    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"), method = "releaseUsing")
    private boolean riptideLaunch(Player player){
        if (Config.RIPTIDE_UNIVERSAL.get()) return true;
        else return player.isInWaterOrRain();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"), method="use")
    private boolean riptideUsage(Player player){
        if (Config.RIPTIDE_UNIVERSAL.get()) return true;
        else return player.isInWaterOrRain();
    }

}