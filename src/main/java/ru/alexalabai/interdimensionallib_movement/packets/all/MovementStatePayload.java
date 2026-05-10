package ru.alexalabai.interdimensionallib_movement.packets.all;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.alexalabai.interdimensionallib_movement.packets.INTERDIM_MOVE_ServerPackets;

public record MovementStatePayload(int state) implements CustomPayload {
    public static final Id<MovementStatePayload> ID = new Id<>(INTERDIM_MOVE_ServerPackets.MOVEMENT_STATE_PACKET);
    public static final PacketCodec<RegistryByteBuf, MovementStatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, MovementStatePayload::state,
            MovementStatePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
