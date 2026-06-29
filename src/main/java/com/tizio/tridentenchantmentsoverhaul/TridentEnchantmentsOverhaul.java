package com.tizio.tridentenchantmentsoverhaul;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

@Mod(TridentEnchantmentsOverhaul.MODID)
public class TridentEnchantmentsOverhaul {

    public static final String MODID = "tridentenchantmentsoverhaul";

    public TridentEnchantmentsOverhaul(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

}