package ru.alexalabai.interdimensionallib_movement.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import ru.alexalabai.interdimensionallib_movement.InterdimensionalLibMovementClient;
import ru.alexalabai.interdimensionallib_movement.packets.all.ResponseCrawlingAllowedPayload;
import ru.alexalabai.interdimensionallib_movement.packets.all.ResponseMovementStatePayload;
import ru.alexalabai.interdimensionallib_movement.packets.all.ResponseSittingAllowedPayload;

public class INTERDIM_MOVE_ClientPackets {
    public static void regAll() {
        ClientPlayNetworking.registerGlobalReceiver(ResponseSittingAllowedPayload.ID, (payload, ctx)->
                ctx.client().execute(()-> InterdimensionalLibMovementClient.isSittingAllowedOnServer=payload.allowed()));
        ClientPlayNetworking.registerGlobalReceiver(ResponseCrawlingAllowedPayload.ID, (payload, ctx)->
                ctx.client().execute(()-> InterdimensionalLibMovementClient.isCrawlingAllowedOnServer=payload.allowed()));
        ClientPlayNetworking.registerGlobalReceiver(ResponseMovementStatePayload.ID, (payload, ctx)->
                ctx.client().execute(()->{
                    if(payload.state()==1) InterdimensionalLibMovementClient.activelySitting=true;
                    else if(payload.state()==2) InterdimensionalLibMovementClient.activelyCrawling=true;
                }));
    }
}
