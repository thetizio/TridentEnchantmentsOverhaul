package com.tizio.tridentenchantmentsoverhaul.mixin;

import com.tizio.tridentenchantmentsoverhaul.config.Config;
import net.minecraft.enchantment.ImpalingEnchantment;
import net.minecraft.entity.EntityGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ImpalingEnchantment.class)
public class ImpalingDamage {

    @Inject(at = @At("HEAD"), method = "getAttackDamage", cancellable = true)
    public void getAttackDamage(int level, EntityGroup group, CallbackInfoReturnable<Float> cir) {
        if (Config.impalingUniversal){
            cir.setReturnValue(Config.impalingDamage*level);
        } else cir.setReturnValue(group == EntityGroup.AQUATIC ? Config.impalingDamage*level : 0.0F);
    }

}