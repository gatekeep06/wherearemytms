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
import net.fabricmc.loader.api.FabricLoader;
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

    public static boolean allow_egg_moves = false;
    public static boolean allow_tutor_moves = false;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello, TMs!");

        File configFile = FabricLoader.getInstance().getConfigDir().resolve(MOD_NAME + "Config.json").toFile();

        if (configFile.exists()) {
            WhereAreMyTMsConfig config = WhereAreMyTMsConfig.fromFile(configFile);
//            allow_egg_moves = config.allow_egg_moves;
            allow_tutor_moves = config.allow_tutor_moves;
        } else {
            new WhereAreMyTMsConfig().toFile(configFile);
        }

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

}
