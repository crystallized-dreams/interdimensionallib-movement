package ru.alexalabai.interdimensionallib_movement.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.alexalabai.interdimensionallib_movement.config.INTERDIM_MOVE_ModConfig;
import ru.alexalabai.interdimensionallib_movement.packets.all.ResponseCrawlingAllowedPayload;
import ru.alexalabai.interdimensionallib_movement.packets.all.ResponseSittingAllowedPayload;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class INTERDIM_MOVE_ServerCommandHandler {
    public static void regAll(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("interdim").requires(src->src.hasPermissionLevel(1))
                .then(literal("movement")
                        .then(literal("canCrawl").then(argument("value",BoolArgumentType.bool()).executes(ctx->{
                            INTERDIM_MOVE_ModConfig.INSTANCE.canPlayersCrawl=BoolArgumentType.getBool(ctx,"value");
                            for(ServerPlayerEntity player : ctx.getSource().getServer().getPlayerManager().getPlayerList())
                                ServerPlayNetworking.send(player, new ResponseCrawlingAllowedPayload(INTERDIM_MOVE_ModConfig.INSTANCE.canPlayersCrawl));
                            return 1;
                        })))
                        .then(literal("canSit").then(argument("value",BoolArgumentType.bool()).executes(ctx->{
                            INTERDIM_MOVE_ModConfig.INSTANCE.canPlayersSit=BoolArgumentType.getBool(ctx,"value");
                            for(ServerPlayerEntity player : ctx.getSource().getServer().getPlayerManager().getPlayerList())
                                ServerPlayNetworking.send(player, new ResponseSittingAllowedPayload(INTERDIM_MOVE_ModConfig.INSTANCE.canPlayersSit));
                            return 1;
                        })))
                )
        );
    }
}
