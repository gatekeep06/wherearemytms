package dev.reyaan.wherearemytms.fabric.item

class PokemonHM(settings: Settings,
                override val breaksAfterUse: Boolean): MoveTransferItem(settings) {
    override val titleKey: String = "title.wherearemytms.hm"
}