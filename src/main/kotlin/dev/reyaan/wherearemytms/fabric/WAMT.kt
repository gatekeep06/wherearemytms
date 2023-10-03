package dev.reyaan.wherearemytms.fabric

import dev.reyaan.wherearemytms.fabric.block.tmmachine.TMMachineBlock
import dev.reyaan.wherearemytms.fabric.block.tmmachine.TMMachineBlockEntity
import dev.reyaan.wherearemytms.fabric.config.WAMTConfigHandler
import dev.reyaan.wherearemytms.fabric.config.WAMTConfigObject
import dev.reyaan.wherearemytms.fabric.item.MoveTransferItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import java.io.File

object WAMT: ModInitializer {
    const val MOD_ID = "wherearemytms"

    fun id(path: String): Identifier {
        return Identifier(MOD_ID, path)
    }

    var POKEMON_HM = MoveTransferItem(Item.Settings().maxCount(16).group(ItemGroup.MISC), true, "title.wherearemytms.hm")
    var POKEMON_TM = MoveTransferItem(Item.Settings().maxCount(16).group(ItemGroup.MISC), false, "title.wherearemytms.tm")

    val TM_MACHINE: Block = TMMachineBlock(FabricBlockSettings.of(Material.METAL).strength(3.0f).hardness(4.0f).nonOpaque().luminance{ if (it.get(TMMachineBlock.HALF) == DoubleBlockHalf.LOWER) 5 else 0 })

    val TM_MACHINE_BLOCK_ENTITY: BlockEntityType<TMMachineBlockEntity> = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        id("tm_machine_block_entity"),
        FabricBlockEntityTypeBuilder.create({ pos: BlockPos, state: BlockState ->
            TMMachineBlockEntity(
                pos,
                state
            )
        }, TM_MACHINE).build()
    )

    val TM_MACHINE_MOVE_SELECT_PACKET_ID = id("packet.wherearemytms.close")


    lateinit var config: WAMTConfigObject

    override fun onInitialize() {
        config = WAMTConfigHandler(File(FabricLoader.getInstance().configDir.toString() + "/wherearemytms.json")).init()

        registerTMs()
        registerMachine()

        WAMTNetwork.serverListeners()
    }

    fun registerMachine() {
        Registry.register(Registry.BLOCK, id("tm_machine"), TM_MACHINE)
        Registry.register(
            Registry.ITEM,
            id("tm_machine"),
            BlockItem(TM_MACHINE, FabricItemSettings().group(ItemGroup.DECORATIONS))
        )
    }

    fun registerTMs() {
        Registry.register(Registry.ITEM, id("blank_hm"), POKEMON_HM)
        Registry.register(Registry.ITEM, id("blank_tm"), POKEMON_TM)
    }
}