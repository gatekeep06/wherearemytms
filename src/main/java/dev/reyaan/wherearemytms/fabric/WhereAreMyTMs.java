package dev.reyaan.wherearemytms.fabric;


import dev.reyaan.wherearemytms.fabric.block.TMDriveBlock;
import dev.reyaan.wherearemytms.fabric.block.TMDriveBlockEntity;
import dev.reyaan.wherearemytms.fabric.item.BasePokemonTM;
import dev.reyaan.wherearemytms.fabric.item.PokemonHM;
import dev.reyaan.wherearemytms.fabric.item.PokemonTM;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WhereAreMyTMs implements ModInitializer {
    public static String MOD_NAME = "Where Are My TMs?";
    public static String MOD_ID = "wherearemytms";
    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);


    public static Item BLANK_HM = new PokemonHM();
    public static Item BLANK_TM = new PokemonTM();

    public static final Block TM_DRIVE = new TMDriveBlock(FabricBlockSettings.of(Material.METAL).strength(1.0f));

    public static final BlockEntityType<TMDriveBlockEntity> TM_DRIVE_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            id("tm_drive_block_entity"),
            FabricBlockEntityTypeBuilder.create(TMDriveBlockEntity::new, TM_DRIVE).build()
    );


    @Override
    public void onInitialize() {
        LOGGER.info("Hello, TMs!");
        registerBlankDiscs();
        registerTMDrive();
    }

    public static void registerBlankDiscs() {
        Registry.register(Registry.ITEM, id("blank_hm"), BLANK_HM);
        Registry.register(Registry.ITEM, id("blank_tm"), BLANK_TM);
    }

    public static void registerTMDrive() {
        Registry.register(Registry.BLOCK, id("tm_drive"), TM_DRIVE);
        Registry.register(Registry.ITEM, id("tm_drive"), new BlockItem(TM_DRIVE, new FabricItemSettings()));
    }

}
