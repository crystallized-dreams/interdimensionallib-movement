package ru.alexalabai.interdimensionallib_movement;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexalabai.interdimensionallib_movement.config.ModConfig;
import ru.alexalabai.interdimensionallib_movement.entity.ModEntities;
import ru.alexalabai.interdimensionallib_movement.packets.ModPackets;

public class InterdimensionalLibMovement implements ModInitializer {
	public static final String MOD_ID = "interdimensionallib-movement";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfig.INSTANCE=ModConfig.load();
		LOGGER.info("[INTERDIM_MOVEMENT]: Registered server config");
		ModPackets.regAll();
		ModEntities.regAll();
		ServerLifecycleEvents.SERVER_STOPPED.register(s->{
			ModConfig.INSTANCE.save();
		});
		LOGGER.info("[INTERDIM_MOVEMENT]: Server initialized");
	}
}