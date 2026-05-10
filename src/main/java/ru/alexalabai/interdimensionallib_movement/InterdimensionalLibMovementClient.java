package ru.alexalabai.interdimensionallib_movement;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import ru.alexalabai.interdimensionallib_movement.common.INTERDIM_MOVE_ClientCommandHandler;
import ru.alexalabai.interdimensionallib_movement.common.INTERDIM_MOVE_ClientGameOptions;
import ru.alexalabai.interdimensionallib_movement.config.INTERDIM_MOVE_ModClientConfig;
import ru.alexalabai.interdimensionallib_movement.entity.INTERDIM_MOVE_Entities;
import ru.alexalabai.interdimensionallib_movement.packets.INTERDIM_MOVE_ClientPackets;
import ru.alexalabai.interdimensionallib_movement.packets.all.MovementStatePayload;
import ru.alexalabai.interdimensionallib_movement.packets.all.GRIP;

public class InterdimensionalLibMovementClient implements ClientModInitializer {
    public static boolean activelyCrawlingForceFromCommand = false;
    public static boolean activelyCrawling = false;
    public static boolean activelySitting = false;
    private static boolean activelyCrawlingJustChanged = false;
    private static boolean crawlKeyPressedLast=false;
    public static boolean isSittingAllowedOnServer=true;
    public static boolean isCrawlingAllowedOnServer=true;
    @Override
    public void onInitializeClient() {
        INTERDIM_MOVE_ModClientConfig.INSTANCE = INTERDIM_MOVE_ModClientConfig.load();
        InterdimensionalLibMovement.LOGGER.info("[INTERDIM_MOVEMENT_CLIENT]: Registered client config");
        INTERDIM_MOVE_ClientGameOptions.regAll();
        INTERDIM_MOVE_ClientGameOptions.crawlToggle.setValue(INTERDIM_MOVE_ModClientConfig.INSTANCE.crawlToggleable);
        INTERDIM_MOVE_ClientGameOptions.sitToggle.setValue(INTERDIM_MOVE_ModClientConfig.INSTANCE.sitOnBlockClick);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access)-> INTERDIM_MOVE_ClientCommandHandler.regAll(dispatcher));
        INTERDIM_MOVE_Entities.regRenderers();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            if (isCrawlingAllowedOnServer) {
                boolean crawlIsToggleable = INTERDIM_MOVE_ClientGameOptions.crawlToggle.getValue();

                if (activelyCrawlingJustChanged) {
                    activelySitting = false;
                    activelyCrawlingForceFromCommand = false;
                    ClientPlayNetworking.send(new MovementStatePayload(activelyCrawling ? 2 : 0));
                    activelyCrawlingJustChanged = false;
                } else {
                    if (crawlIsToggleable) {
                        if (INTERDIM_MOVE_ClientGameOptions.crawlKey.wasPressed()) activelyCrawling = !activelyCrawling;
                    } else if (!activelyCrawlingForceFromCommand) {
                        if (INTERDIM_MOVE_ClientGameOptions.crawlKey.isPressed()) activelyCrawling = true;
                        else if (activelyCrawling) activelyCrawling = false;
                    }
                    if (crawlKeyPressedLast != activelyCrawling) activelyCrawlingJustChanged = true;
                    crawlKeyPressedLast = activelyCrawling;
                }
            } else if (INTERDIM_MOVE_ClientGameOptions.crawlKey.wasPressed()) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("text.interdimensionallib.crawling_disallowed"));
            }

            if(INTERDIM_MOVE_ClientGameOptions.sitKey.wasPressed()) {
                if(isSittingAllowedOnServer) {
                    activelySitting = !activelySitting;
                    activelyCrawling = false;
                    ClientPlayNetworking.send(new MovementStatePayload(activelySitting ? 1 : 0));
                } else MinecraftClient.getInstance().player.sendMessage(Text.translatable("text.interdimensionallib.sitting_disallowed"));
            }
        });
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client)-> {
            ClientPlayNetworking.send(new GRIP(1));
            ClientPlayNetworking.send(new GRIP(2));
            ClientPlayNetworking.send(new GRIP(3));
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client)->{
            activelyCrawling = false;
            activelySitting = false;
            activelyCrawlingJustChanged = false;
            crawlKeyPressedLast=false;
            isSittingAllowedOnServer=true;
            isCrawlingAllowedOnServer=true;
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(client->{
            INTERDIM_MOVE_ModClientConfig.INSTANCE.save();
        });
        INTERDIM_MOVE_ClientPackets.regAll();
    }

    public static boolean isLocalPlayer(PlayerEntity player) {
        return MinecraftClient.getInstance().player!=null
                && MinecraftClient.getInstance().player.getUuid().equals(player.getUuid());
    }
}
