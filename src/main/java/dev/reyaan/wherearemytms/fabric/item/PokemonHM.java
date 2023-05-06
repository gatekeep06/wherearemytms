package dev.reyaan.wherearemytms.fabric.item;

import com.cobblemon.mod.common.api.moves.*;
import com.cobblemon.mod.common.api.types.ElementalType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PokemonHM extends BasePokemonTM {
    public PokemonHM() {
        super(new Item.Settings().maxCount(1).group(ItemGroup.MISC),
                "title.wherearemytms.hm",
                "description.wherearemytms.blank_hm_uses",
                true);
    }
}
