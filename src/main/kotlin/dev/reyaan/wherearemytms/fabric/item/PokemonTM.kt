package dev.reyaan.wherearemytms.fabric.item

class PokemonTM(settings: Settings, override val breaksAfterUse: Boolean): MoveTransferItem(settings) {
    override val titleKey: String = "title.wherearemytms.hm"
}