package com.tizio.tridentenchantmentsoverhaul;

import com.tizio.tridentenchantmentsoverhaul.config.Config;
import net.fabricmc.api.ModInitializer;

public class TridentEnchantmentsOverhaul implements ModInitializer {

	@Override
	public void onInitialize() {

		Config.initialize();
	}

}