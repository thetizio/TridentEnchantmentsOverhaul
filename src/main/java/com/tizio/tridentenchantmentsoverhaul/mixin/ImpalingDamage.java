package com.tizio.tridentenchantmentsoverhaul.mixin;

import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.TridentImpalerEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentImpalerEnchantment.class)
public class ImpalingDamage {

    @Inject(at = @At("HEAD"), method = "getDamageBonus", cancellable = true)
    public void getAttackDamage(int level, MobType group, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue((float)level);
    }

}