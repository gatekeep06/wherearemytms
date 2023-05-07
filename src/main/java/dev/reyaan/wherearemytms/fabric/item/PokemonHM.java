package dev.reyaan.wherearemytms.fabric.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;


public class PokemonHM extends BasePokemonTM {
    public PokemonHM() {
        super(new Item.Settings().maxCount(1).group(ItemGroup.MISC),
                "title.wherearemytms.hm",
                "description.wherearemytms.blank_hm_uses",
                true);
    }
}
