package ru.alexalabai.interdimensionallib_movement.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.alexalabai.interdimensionallib_movement.InterdimensionalLibMovement;
import ru.alexalabai.interdimensionallib_movement.entity.renderer.SeatEntityRenderer;

public class INTERDIM_MOVE_Entities {
    public static final EntityType<SeatEntity> SEAT_ENTITY=reg("seat",
            EntityType.Builder.create(SeatEntity::new, SpawnGroup.MISC)
                    .disableSummon()
                    .passengerAttachments(0f)
                    .build());

    public static void regAll() {
        InterdimensionalLibMovement.LOGGER.info("Registered entities");
    }

    @Environment(EnvType.CLIENT)
    public static void regRenderers() {
        EntityRendererRegistry.register(SEAT_ENTITY, SeatEntityRenderer::new);
        InterdimensionalLibMovement.LOGGER.info("Registered entity renderers");
    }

    private static <T extends Entity> EntityType<T> reg(String name, EntityType<T> entityType) {
        return Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(InterdimensionalLibMovement.MOD_ID, name),
                entityType
        );
    }
}
