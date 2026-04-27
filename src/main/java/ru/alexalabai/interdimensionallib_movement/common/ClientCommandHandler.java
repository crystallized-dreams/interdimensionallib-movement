package ru.alexalabai.interdimensionallib_movement.common;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.alexalabai.interdimensionallib_movement.DIMLIBMClient;
import ru.alexalabai.interdimensionallib_movement.packets.all.MovementStatePayload;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class ClientCommandHandler {
    public static void regAll(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("crawl").executes(ctx->{
            if(!DIMLIBMClient.isCrawlingAllowedOnServer) {
                ctx.getSource().getPlayer().sendMessage(Text.translatable("text.interdimensionallib.crawling_disallowed"));
                return 0;
            }
            DIMLIBMClient.activelySitting=false;
            DIMLIBMClient.activelyCrawlingForceFromCommand=true;
            DIMLIBMClient.activelyCrawling=!DIMLIBMClient.activelyCrawling;
            return 1;
        }));
        dispatcher.register(literal("sit").executes(ctx->{
            if(DIMLIBMClient.isSittingAllowedOnServer) {
                DIMLIBMClient.activelySitting = !DIMLIBMClient.activelySitting;
                DIMLIBMClient.activelyCrawling = false;
                ClientPlayNetworking.send(new MovementStatePayload(DIMLIBMClient.activelySitting ? 1 : 0));
            } else MinecraftClient.getInstance().player.sendMessage(Text.translatable("text.interdimensionallib.sitting_disallowed"));
            return 1;
        }));
    }
}
