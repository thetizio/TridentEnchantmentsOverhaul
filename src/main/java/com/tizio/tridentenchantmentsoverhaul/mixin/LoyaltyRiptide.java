package com.tizio.tridentenchantmentsoverhaul.mixin;

import com.tizio.tridentenchantmentsoverhaul.config.Config;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public abstract class LoyaltyRiptide extends Item implements ProjectileItem {

	public LoyaltyRiptide(Item.Settings settings) {
		super(settings);
	}

	@Inject(at = @At("HEAD"), method = "onStoppedUsing", cancellable = true)
	private void loyalty(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {

		if (user instanceof PlayerEntity playerEntity) {
			if (!world.isClient) {
				int loyaltyLevel = EnchantmentHelper.getLevel((RegistryEntry<net.minecraft.enchantment.Enchantment>) world.getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.LOYALTY),stack);
				if (loyaltyLevel > 0) {
					if(this.getMaxUseTime(stack, user) - remainingUseTicks >= 10) {
						stack.damage(1, playerEntity, LivingEntity.getSlotForHand(user.getActiveHand()));
						TridentEntity tridentEntity = new TridentEntity(world, playerEntity, stack);
						tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F,
							2.5F+Config.loyaltyThrow,
							1.0F-Config.loyaltyInaccuracyDecrease*loyaltyLevel>0? 1.0F-Config.loyaltyInaccuracyDecrease*loyaltyLevel : 0);
						if (Config.loyaltyInventory || playerEntity.isInCreativeMode()) {
							tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
						}

						world.spawnEntity(tridentEntity);
						RegistryEntry<SoundEvent> registryEntry = (RegistryEntry)EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.TRIDENT_SOUND).orElse(SoundEvents.ITEM_TRIDENT_THROW);
						world.playSoundFromEntity((PlayerEntity) null, tridentEntity, (SoundEvent) registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
						if (!Config.loyaltyInventory && !playerEntity.isInCreativeMode()) {
							playerEntity.getInventory().removeOne(stack);
						}
					}
					info.cancel();
				}
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "onStoppedUsing")
	private void riptide(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {

		if (Config.riptideSlowFalling) {
			if (user instanceof PlayerEntity playerEntity) {
				if (this.getMaxUseTime(stack, user) - remainingUseTicks >= 10) {
					if (playerEntity.isTouchingWaterOrRain()) {
						if (EnchantmentHelper.getTridentSpinAttackStrength(stack, playerEntity) > 0)
							playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, Config.riptideDuration*20, 0));
					}
				}
			}
		}

	}

}