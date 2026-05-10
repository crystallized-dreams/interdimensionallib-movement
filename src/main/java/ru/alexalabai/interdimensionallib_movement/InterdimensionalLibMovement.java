package ru.alexalabai.interdimensionallib_movement;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexalabai.interdimensionallib_movement.common.INTERDIM_MOVE_ServerCommandHandler;
import ru.alexalabai.interdimensionallib_movement.common.types.MovementState;
import ru.alexalabai.interdimensionallib_movement.config.INTERDIM_MOVE_ModConfig;
import ru.alexalabai.interdimensionallib_movement.entity.INTERDIM_MOVE_Entities;
import ru.alexalabai.interdimensionallib_movement.packets.INTERDIM_MOVE_ServerPackets;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InterdimensionalLibMovement implements ModInitializer {
	public static final String MOD_ID = "interdimensionallib-movement";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Map<UUID, MovementState> MOVEMENT_STATES = new HashMap<>();
	public static void setState(UUID player, MovementState state) {
		MOVEMENT_STATES.put(player,state);
	}
	public static void setState(UUID player, int stateV) {
		MovementState state=stateV==0?MovementState.NONE:stateV==2?MovementState.CRAWLING:MovementState.SITTING;
		MOVEMENT_STATES.put(player,state);
	}

	@Override
	public void onInitialize() {
		INTERDIM_MOVE_ModConfig.INSTANCE=INTERDIM_MOVE_ModConfig.load();
		LOGGER.info("[INTERDIM_MOVEMENT]: Registered server config");
		INTERDIM_MOVE_ServerPackets.regAll();
		INTERDIM_MOVE_Entities.regAll();
		ServerLifecycleEvents.SERVER_STOPPED.register(s->{
			INTERDIM_MOVE_ModConfig.INSTANCE.save();
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, access, env)->INTERDIM_MOVE_ServerCommandHandler.regAll(dispatcher));
		LOGGER.info("[INTERDIM_MOVEMENT]: Server initialized");
	}
}