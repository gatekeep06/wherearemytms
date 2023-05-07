package dev.reyaan.wherearemytms.fabric;

import dev.reyaan.wherearemytms.fabric.block.TMMachineScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static dev.reyaan.wherearemytms.fabric.WhereAreMyTMs.*;

@Environment(EnvType.CLIENT)
public class WhereAreMyTMsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HashMap<String, String> types = new HashMap<>();
        createTypes(types);

        HandledScreens.register(TM_MACHINE_SCREEN_HANDLER, TMMachineScreen::new);
    }

    private static void createTypes(HashMap<String, String> types) {
        types.put("normal", "Normal");
        types.put("fire", "Fire");
        types.put("water", "Water");
        types.put("grass", "Grass");
        types.put("electric", "Electric");
        types.put("ice", "Ice");
        types.put("fighting", "Fighting");
        types.put("poison", "Poison");
        types.put("ground", "Ground");
        types.put("flying", "Flying");
        types.put("psychic", "Psychic");
        types.put("bug", "Bug");
        types.put("rock", "Rock");
        types.put("ghost", "Ghost");
        types.put("dragon", "Dragon");
        types.put("dark", "Dark");
        types.put("steel", "Steel");
        types.put("fairy", "Fairy");

        for (Item item : machines) {
            for (Map.Entry<String, String> entry : types.entrySet()) {
                ModelPredicateProviderRegistry.register(item, id(entry.getKey()), (itemStack, clientWorld, livingEntity, seed) -> {
                    NbtCompound nbtCompound = itemStack.getOrCreateNbt();
                    if (!nbtCompound.contains("type")) {
                        return 0;
                    }
                    return Objects.equals(nbtCompound.getString("type"), entry.getValue()) ? 1 : 0;
                });
            }
        }
    }
}
