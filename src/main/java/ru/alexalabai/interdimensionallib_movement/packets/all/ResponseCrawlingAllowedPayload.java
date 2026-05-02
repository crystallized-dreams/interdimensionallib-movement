package ru.alexalabai.interdimensionallib_movement.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.alexalabai.interdimensionallib_movement.packets.INTERDIM_MOVE_ServerPackets;

public record ResponseCrawlingAllowedPayload(boolean allowed) implements CustomPayload {
    public static final Id<ResponseCrawlingAllowedPayload> ID = new Id<>(INTERDIM_MOVE_ServerPackets.RESPONSE_CRAWLING_ALLOWED_PACKET);
    public static final PacketCodec<RegistryByteBuf, ResponseCrawlingAllowedPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, ResponseCrawlingAllowedPayload::allowed,
            ResponseCrawlingAllowedPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
