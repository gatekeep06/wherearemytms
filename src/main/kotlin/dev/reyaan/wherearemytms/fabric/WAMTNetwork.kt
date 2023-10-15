package dev.reyaan.wherearemytms.fabric

import com.cobblemon.mod.common.api.moves.Move
import com.cobblemon.mod.common.api.moves.Move.Companion.loadFromNBT
import dev.reyaan.wherearemytms.fabric.WAMT.TM_MACHINE_MOVE_SELECT_PACKET_ID
import dev.reyaan.wherearemytms.fabric.item.MoveTransferItem
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.Block
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting


object WAMTNetwork {
    fun serverListeners() {
        ServerPlayNetworking.registerGlobalReceiver(
            TM_MACHINE_MOVE_SELECT_PACKET_ID
        ) { server: MinecraftServer, player: ServerPlayerEntity, handler: ServerPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender ->
            val nbtCompound = buf.readNbt()
            if (nbtCompound != null) {
                val move = Move.loadFromNBT(nbtCompound)
                val stack = player.getStackInHand(player.activeHand)
                if (stack.item is MoveTransferItem) {

                    val stackNbt = stack.getOrCreateNbt()
                    stackNbt.putString("move", move.name)

                    // Update
                    stack.nbt = stackNbt

                    // Set (colored) name
                    stack.setCustomName(
                        Text.translatable((stack.item as MoveTransferItem).titleKey)
                            .setStyle(
                                Style.EMPTY
                                    .withColor(Formatting.WHITE)
                                    .withItalic(false)
                            )
                            .append(move.displayName)
                            .setStyle(
                                Style.EMPTY
                                    .withColor(move.type.hue)
                                    .withItalic(false)
                            )
                    )
                    player.setStackInHand(player.activeHand, stack)
                }
            }
        }
    }
}