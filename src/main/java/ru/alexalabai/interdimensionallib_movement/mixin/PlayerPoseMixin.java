package ru.alexalabai.interdimensionallib_movement.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.alexalabai.interdimensionallib_movement.InterdimensionalLibMovement;
import ru.alexalabai.interdimensionallib_movement.InterdimensionalLibMovementClient;
import ru.alexalabai.interdimensionallib_movement.common.types.MovementState;
import ru.alexalabai.interdimensionallib_movement.config.INTERDIM_MOVE_ModConfig;

import java.util.UUID;

@SuppressWarnings("unused")
@Mixin(PlayerEntity.class)
public class PlayerPoseMixin {
    //Client pose handling is stupid and needs moral support to work for some reason
    @Inject(method="updatePose",at=@At(value="HEAD"),cancellable=true)
    void updatePose$interdim(CallbackInfo info) {
        var player=(PlayerEntity)(Object)this;
        if(player.getWorld().isClient()) handleClientPlayerState$interdim(player,info);
        else handleServerPlayerState$interdim(player,info);
    }

    @Unique
    void handleServerPlayerState$interdim(PlayerEntity player, CallbackInfo info) {
        UUID uuid=player.getUuid();
        if(!InterdimensionalLibMovement.MOVEMENT_STATES.containsKey(uuid)) return;
        switch(InterdimensionalLibMovement.MOVEMENT_STATES.get(uuid)) {
            case MovementState.CRAWLING:
                if(!INTERDIM_MOVE_ModConfig.INSTANCE.canPlayersCrawl) return;
                player.setPose(EntityPose.SWIMMING);
                info.cancel();
                break;
            case MovementState.SITTING:
                break;
            default:
                player.setPose(EntityPose.STANDING);
                InterdimensionalLibMovement.MOVEMENT_STATES.remove(uuid);
                break;
        }
    }

    @Unique
    void handleClientPlayerState$interdim(PlayerEntity player, CallbackInfo info) {
        if(!InterdimensionalLibMovementClient.isLocalPlayer(player)) return;
        if(InterdimensionalLibMovementClient.activelyCrawling&&InterdimensionalLibMovementClient.isCrawlingAllowedOnServer) {
            player.setPose(EntityPose.SWIMMING);
            info.cancel();
        }
    }
}
