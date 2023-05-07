package dev.reyaan.wherearemytms.fabric.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class PokemonTM extends BasePokemonTM {
    public PokemonTM() {
        super(new Item.Settings().maxCount(1).group(ItemGroup.MISC),
                "title.wherearemytms.tm",
                "description.wherearemytms.blank_tm_uses",
                false);
    }
}