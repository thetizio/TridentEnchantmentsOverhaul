package com.tizio.tridentenchantmentsoverhaul;

import com.tizio.tridentenchantmentsoverhaul.config.Config;
import net.fabricmc.api.ModInitializer;

public class TridentEnchantmentsOverhaul implements ModInitializer {

	public static final String MOD_ID = "tridentenchantmentsoverhaul";

	@Override
	public void onInitialize() {

		Config.initialize();
	}
}