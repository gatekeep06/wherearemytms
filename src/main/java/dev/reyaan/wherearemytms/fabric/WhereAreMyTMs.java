package dev.reyaan.wherearemytms.fabric;


import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import dev.reyaan.wherearemytms.fabric.block.TMMachineBlock;
import dev.reyaan.wherearemytms.fabric.block.TMMachineBlockEntity;
import dev.reyaan.wherearemytms.fabric.block.TMMachineScreenHandler;
import dev.reyaan.wherearemytms.fabric.item.BasePokemonTM;
import dev.reyaan.wherearemytms.fabric.item.PokemonHM;
import dev.reyaan.wherearemytms.fabric.item.PokemonTM;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WhereAreMyTMs implements ModInitializer {
    public static String MOD_NAME = "WhereAreMyTMs";
    public static String MOD_ID = "wherearemytms";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static Item BLANK_HM = new PokemonHM();
    public static Item BLANK_TM = new PokemonTM();
    public static List<Item> machines = new ArrayList<>();

    public static final Block TM_MACHINE = new TMMachineBlock(FabricBlockSettings.of(Material.METAL).strength(1.0f));

    public static final BlockEntityType<TMMachineBlockEntity> TM_MACHINE_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            id("tm_machine_block_entity"),
            FabricBlockEntityTypeBuilder.create(TMMachineBlockEntity::new, TM_MACHINE).build()
    );

    public static final ScreenHandlerType<TMMachineScreenHandler> TM_MACHINE_SCREEN_HANDLER = new ScreenHandlerType<>(TMMachineScreenHandler::new);

    public static Identifier TM_MACHINE_CLOSE_PACKET_ID = id("packet.wherearemytms.close");

    public static Map<String, String> config;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello, TMs!");
        createConfig();
        config = readConfig();


        registerBlankDiscs();
        registerTMMachine();
        registerPackets();
    }

    public static void registerBlankDiscs() {
        machine(BLANK_HM, "blank_hm");
        machine(BLANK_TM, "blank_tm");
    }

    private static void machine(Item item, String path) {
        Registry.register(Registry.ITEM, id(path), item);
        machines.add(item);
    }

    public static void registerTMMachine() {
        Registry.register(Registry.BLOCK, id("tm_machine"), TM_MACHINE);
        Registry.register(Registry.ITEM, id("tm_machine"), new BlockItem(TM_MACHINE, new FabricItemSettings()));
        Registry.register(Registry.SCREEN_HANDLER, id("tm_machine_screen_handler"), TM_MACHINE_SCREEN_HANDLER);
    }

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(TM_MACHINE_CLOSE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            NbtCompound nbtCompound = buf.readNbt();
            if (nbtCompound != null) {
                Move move = Move.Companion.loadFromNBT(nbtCompound);
                ItemStack stack = player.getStackInHand(player.getActiveHand());
                if (stack.getItem() instanceof BasePokemonTM tm) {

                    // Create
                    NbtCompound stackNbt = stack.getOrCreateNbt();
                    stackNbt.putString("move", move.getName());
                    ElementalType type = move.getType();
                    int hue = type.getHue();
                    stackNbt.putString("type", type.getDisplayName().getString());
                    stackNbt.putInt("hue", hue);

                    // Update
                    stack.setNbt(stackNbt);
                    stack.setCustomName(Text.translatable(tm.title, move.getDisplayName())
                            .setStyle(Style.EMPTY
                                    .withColor(hue)
                                    .withItalic(false)));
                    player.setStackInHand(player.getActiveHand(), stack);
                }
            }

            player.closeHandledScreen();
        });
    }

    public boolean createConfig() {
        File configFolder = new File(System.getProperty("user.dir") + "/config/" + MOD_ID);
        if (!configFolder.exists()) {
            boolean bl = configFolder.mkdir();
            LOGGER.info("Config directory not found, created successfully: " + bl);
            if (!bl) return false;
        }

        File configFile = new File(configFolder, MOD_NAME + "Config.json");
        if (!configFile.exists()) {
            try {
                boolean bl = configFile.createNewFile();
                LOGGER.info("Config file not found, created successfully: " + bl);
                if (!bl) return false;

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Writer writer = Files.newBufferedWriter(configFile.toPath());
                gson.toJson(createConfigMap(), writer);
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public HashMap<String, String> createConfigMap() {
        HashMap<String, String> map = new HashMap<>() {};
        map.put("allow_egg_moves", "false");
        map.put("allow_tutor_moves", "false");
        return map;
    }

    public HashMap<String, String> readConfig() {
        File configFolder = new File(System.getProperty("user.dir") + "/config/" + MOD_ID);
        File configFile = new File(configFolder, MOD_NAME + "Config.json");
        Gson gson = new Gson();
        try {
            String json = new String(Files.readAllBytes(configFile.toPath()));
            System.out.println("JSON READ: " + json);
            return gson.fromJson(json, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
