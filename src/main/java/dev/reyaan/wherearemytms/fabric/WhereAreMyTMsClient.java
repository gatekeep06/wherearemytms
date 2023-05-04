package dev.reyaan.wherearemytms.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;

import static dev.reyaan.wherearemytms.fabric.WhereAreMyTMs.BLANK_TM;
import static dev.reyaan.wherearemytms.fabric.WhereAreMyTMs.id;

@Environment(EnvType.CLIENT)
public class WhereAreMyTMsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(BLANK_TM, id("fire"), (itemStack, clientWorld, livingEntity, i) -> {
            NbtCompound nbtCompound = itemStack.getOrCreateNbt();
            if (!nbtCompound.contains("type")) {
                return 0.0F;
            }
            return Objects.equals(nbtCompound.getString("type"), "fire") ? 1.0F : 0.0F;
        });

    }
}
