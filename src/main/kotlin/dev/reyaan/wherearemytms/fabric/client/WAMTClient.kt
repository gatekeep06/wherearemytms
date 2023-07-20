package dev.reyaan.wherearemytms.fabric.client

import dev.reyaan.wherearemytms.fabric.WAMT.POKEMON_HM
import dev.reyaan.wherearemytms.fabric.WAMT.POKEMON_TM
import dev.reyaan.wherearemytms.fabric.WAMT.id
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.Text

class WAMTClient: ClientModInitializer {
    private val elementalTypes = listOf("normal", "fire", "water", "grass", "electric", "ice", "fighting", "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon", "dark", "steel", "fairy")

    override fun onInitializeClient() {
//        HandledScreens.register(WhereAreMyTMs.TM_MACHINE_SCREEN_HANDLER, ::TMMachineScreen)

        arrayOf(POKEMON_HM, POKEMON_TM).forEach { item ->
            elementalTypes.forEach { type ->
                ModelPredicateProviderRegistry.register(
                    item,
                    id(type)
                ) { itemStack: ItemStack, _, _, _ ->
                    val nbtCompound = itemStack.getOrCreateNbt()
                    if (!nbtCompound.contains("type")) 0F
                    if (nbtCompound.getString("type") == type) 1F else 0F
                }
            }
        }
    }
}