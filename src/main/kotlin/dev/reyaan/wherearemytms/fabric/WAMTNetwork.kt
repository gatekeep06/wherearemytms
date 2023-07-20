package dev.reyaan.wherearemytms.fabric

import com.cobblemon.mod.common.api.moves.Move.Companion.loadFromNBT
import dev.reyaan.wherearemytms.fabric.WAMT.TM_MACHINE_MOVE_SELECT_PACKET_ID
import dev.reyaan.wherearemytms.fabric.item.MoveTransferItem
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Style
import net.minecraft.text.Text


object WAMTNetwork {
    fun serverListeners() {
        ServerPlayNetworking.registerGlobalReceiver(
            TM_MACHINE_MOVE_SELECT_PACKET_ID
        ) { server: MinecraftServer, player: ServerPlayerEntity, handler: ServerPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender ->
            val nbtCompound = buf.readNbt()
            if (nbtCompound != null) {
                val move = loadFromNBT(nbtCompound)
                val stack = player.getStackInHand(player.activeHand)
                if (stack.item is MoveTransferItem) {

                    val stackNbt = stack.getOrCreateNbt()
                    stackNbt.putString("move", move.name)
                    val type = move.type
                    val hue = type.hue
                    stackNbt.putString("type", type.displayName.string)
                    stackNbt.putInt("hue", hue)

                    // Update
                    stack.nbt = stackNbt
                    stack.setCustomName(
                        Text.translatable((stack.item as MoveTransferItem).titleKey, move.displayName)
                            .setStyle(
                                Style.EMPTY
                                    .withColor(hue)
                                    .withItalic(false)
                            )
                    )
                    player.setStackInHand(player.activeHand, stack)
                }
            }
        }
    }
}