package dev.reyaan.wherearemytms.fabric;


import com.cobblemon.mod.common.api.pokemon.Natures;
import dev.reyaan.wherearemytms.fabric.block.TEDriveBlock;
import dev.reyaan.wherearemytms.fabric.block.TEDriveBlockEntity;
import dev.reyaan.wherearemytms.fabric.block.TEDriveScreenHandler;
import dev.reyaan.wherearemytms.fabric.item.BasePokemonTM;
import dev.reyaan.wherearemytms.fabric.item.PokemonHM;
import dev.reyaan.wherearemytms.fabric.item.PokemonTM;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static dev.reyaan.wherearemytms.fabric.item.BasePokemonTM.isTMBlank;


public class WhereAreMyTMs implements ModInitializer {
    public static String MOD_NAME = "Where Are My TMs?";
    public static String MOD_ID = "wherearemytms";
    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static Item BLANK_HM = new PokemonHM();
    public static Item BLANK_TM = new PokemonTM();
    public static List<Item> machines = new ArrayList<>();

    public static final Block TE_DRIVE = new TEDriveBlock(FabricBlockSettings.of(Material.METAL).strength(1.0f));

    public static final BlockEntityType<TEDriveBlockEntity> TE_DRIVE_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            id("te_drive_block_entity"),
            FabricBlockEntityTypeBuilder.create(TEDriveBlockEntity::new, TE_DRIVE).build()
    );

    public static final ScreenHandlerType<TEDriveScreenHandler> TE_DRIVE_SCREEN_HANDLER = new ScreenHandlerType<>(TEDriveScreenHandler::new);

    public static Identifier TE_DRIVE_CLOSE_PACKET_ID = id("packet.wherearemytms.close");

    @Override
    public void onInitialize() {
        LOGGER.info("Hello, TMs!");
        registerBlankDiscs();
        registerTEDrive();
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

    public static void registerTEDrive() {
        Registry.register(Registry.BLOCK, id("te_drive"), TE_DRIVE);
        Registry.register(Registry.ITEM, id("te_drive"), new BlockItem(TE_DRIVE, new FabricItemSettings()));
        Registry.register(Registry.SCREEN_HANDLER, id("te_drive_screen_handler"), TE_DRIVE_SCREEN_HANDLER);
    }

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(TE_DRIVE_CLOSE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            player.closeHandledScreen();
        });
    }

}
