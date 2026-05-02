package ru.alexalabai.interdimensionallib_movement.common;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import ru.alexalabai.interdimensionallib_movement.InterdimensionalLibMovementClient;
import ru.alexalabai.interdimensionallib_movement.packets.all.MovementStatePayload;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class INTERDIM_MOVE_ClientCommandHandler {
    public static void regAll(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("crawl").executes(ctx->{
            if(!InterdimensionalLibMovementClient.isCrawlingAllowedOnServer) {
                ctx.getSource().getPlayer().sendMessage(Text.translatable("text.interdimensionallib.crawling_disallowed"));
                return 0;
            }
            InterdimensionalLibMovementClient.activelyCrawling=!InterdimensionalLibMovementClient.activelyCrawling;
            InterdimensionalLibMovementClient.activelySitting=false;
            InterdimensionalLibMovementClient.activelyCrawlingForceFromCommand=true;
            ClientPlayNetworking.send(new MovementStatePayload(InterdimensionalLibMovementClient.activelyCrawling ? 2 : 0));
            return 1;
        }));
        dispatcher.register(literal("sit").executes(ctx->{
            if(!InterdimensionalLibMovementClient.isSittingAllowedOnServer) {
                ctx.getSource().getPlayer().sendMessage(Text.translatable("text.interdimensionallib.sitting_disallowed"));
                return 0;
            }
            InterdimensionalLibMovementClient.activelyCrawling=false;
            InterdimensionalLibMovementClient.activelySitting=!InterdimensionalLibMovementClient.activelySitting;
            InterdimensionalLibMovementClient.activelyCrawlingForceFromCommand=false;
            ClientPlayNetworking.send(new MovementStatePayload(InterdimensionalLibMovementClient.activelySitting ? 1 : 0));
            return 1;
        }));
    }
}
