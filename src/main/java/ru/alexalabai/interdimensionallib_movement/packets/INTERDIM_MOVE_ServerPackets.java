package ru.alexalabai.interdimensionallib_movement.packets;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import ru.alexalabai.interdimensionallib_movement.InterdimensionalLibMovement;
import ru.alexalabai.interdimensionallib_movement.common.types.MovementState;
import ru.alexalabai.interdimensionallib_movement.config.INTERDIM_MOVE_ModConfig;
import ru.alexalabai.interdimensionallib_movement.entity.INTERDIM_MOVE_Entities;
import ru.alexalabai.interdimensionallib_movement.entity.SeatEntity;
import ru.alexalabai.interdimensionallib_movement.packets.all.*;

public class INTERDIM_MOVE_ServerPackets {
    public static final Identifier GRIP_PACKET = Identifier.of(InterdimensionalLibMovement.MOD_ID, "grip");
    public static final Identifier MOVEMENT_STATE_PACKET = Identifier.of(InterdimensionalLibMovement.MOD_ID, "movement_state");
    public static final Identifier RESPONSE_MOVEMENT_STATE_PACKET = Identifier.of(InterdimensionalLibMovement.MOD_ID, "response_movement_state");
    public static final Identifier RESPONSE_SITTING_ALLOWED_PACKET = Identifier.of(InterdimensionalLibMovement.MOD_ID, "response_can_sit");
    public static final Identifier RESPONSE_CRAWLING_ALLOWED_PACKET = Identifier.of(InterdimensionalLibMovement.MOD_ID, "response_can_crawl");

    public static void regAll() {
        PayloadTypeRegistry.playC2S().register(GRIP.ID, GRIP.CODEC);
        PayloadTypeRegistry.playC2S().register(MovementStatePayload.ID, MovementStatePayload.CODEC);
        InterdimensionalLibMovement.LOGGER.info("[INTERDIM]: Registered client packet payloads (C2S)");
        PayloadTypeRegistry.playS2C().register(ResponseMovementStatePayload.ID, ResponseMovementStatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ResponseSittingAllowedPayload.ID, ResponseSittingAllowedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ResponseCrawlingAllowedPayload.ID, ResponseCrawlingAllowedPayload.CODEC);
        InterdimensionalLibMovement.LOGGER.info("[INTERDIM]: Registered client packet payloads (S2C)");
        ServerPlayNetworking.registerGlobalReceiver(GRIP.ID, (payload, ctx)-> {
            switch (payload.id()) {
                case 1:
                    MovementState state=InterdimensionalLibMovement.MOVEMENT_STATES.get(ctx.player().getUuid());
                    ctx.responseSender().sendPacket(new ResponseMovementStatePayload((state==MovementState.NONE)?0:(state==MovementState.CRAWLING)?2:1));
                    break;
                case 2:
                    ctx.responseSender().sendPacket(new ResponseSittingAllowedPayload(INTERDIM_MOVE_ModConfig.INSTANCE.canPlayersSit));
                    break;
                case 3:
                    ctx.responseSender().sendPacket(new ResponseSittingAllowedPayload(INTERDIM_MOVE_ModConfig.INSTANCE.canPlayersCrawl));
                    break;
            }

        });
        ServerPlayNetworking.registerGlobalReceiver(MovementStatePayload.ID, (payload, ctx)->
                ctx.player().server.execute(()->{
                    if(!INTERDIM_MOVE_ModConfig.INSTANCE.canPlayersSit) return;
                    InterdimensionalLibMovement.setState(ctx.player().getUuid(), payload.state());
                    if(payload.state()==1 && ctx.player().isOnGround()) {
                        SeatEntity ent= INTERDIM_MOVE_Entities.SEAT_ENTITY.create(ctx.player().getWorld());
                        if(ent==null) {
                            InterdimensionalLibMovement.LOGGER.error("[INTERDIM_MOVEMENT]: Somehow creating seat entity at {} {} {} produced null",
                                    ctx.player().getX(),ctx.player().getY(),ctx.player().getZ());
                            return;
                        }
                        ent.setPosition(ctx.player().getPos());
                        ent.setYaw(ctx.player().getYaw());
                        ctx.player().getServerWorld().spawnEntity(ent);
                        ctx.player().startRiding(ent);
                    } else if(payload.state()==0 && ctx.player().hasVehicle()) ctx.player().stopRiding();
                }));
        InterdimensionalLibMovement.LOGGER.info("[INTERDIM_MOVEMENT]: Registered server packet receivers");
    }
}
