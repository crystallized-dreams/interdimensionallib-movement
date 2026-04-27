package ru.alexalabai.interdimensionallib_movement.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import ru.alexalabai.interdimensionallib_movement.DIMLIBMClient;
import ru.alexalabai.interdimensionallib_movement.packets.all.ResponseCrawlingAllowedPayload;
import ru.alexalabai.interdimensionallib_movement.packets.all.ResponseSittingAllowedPayload;

public class ModClientPackets {
    public static void regAll() {
        ClientPlayNetworking.registerGlobalReceiver(ResponseSittingAllowedPayload.ID, (payload, ctx)->
                ctx.client().execute(()-> DIMLIBMClient.isSittingAllowedOnServer=payload.allowed()));
        ClientPlayNetworking.registerGlobalReceiver(ResponseCrawlingAllowedPayload.ID, (payload, ctx)->
                ctx.client().execute(()-> DIMLIBMClient.isCrawlingAllowedOnServer=payload.allowed()));
    }
}
