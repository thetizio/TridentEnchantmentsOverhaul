package com.tizio.tridentenchantmentsoverhaul;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TridentEnchantmentsOverhaul.MODID)
public class TridentEnchantmentsOverhaul {

    public static final String MODID = "tridentenchantmentsoverhaul";

    public TridentEnchantmentsOverhaul(FMLJavaModLoadingContext context) {

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }
}