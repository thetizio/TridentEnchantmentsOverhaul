package com.tizio.tridentenchantmentsoverhaul.mixin;

import com.tizio.tridentenchantmentsoverhaul.Config;
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
        if (Config.IMPALING_UNIVERSAL.get()) cir.setReturnValue(Config.IMPALING_DAMAGE.get().floatValue()*level);
        else cir.setReturnValue(group == MobType.WATER ? Config.IMPALING_DAMAGE.get().floatValue()*level : 0.0F);
    }

}