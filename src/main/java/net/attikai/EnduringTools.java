package net.attikai;

import net.attikai.config.ModConfig;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnduringTools implements ModInitializer {
	public static ModConfig config = new ModConfig();
	public static final String MOD_ID = "enduring-tools";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		config.load();
	}
}