package dev.reyaan.wherearemytms.fabric.item;

import com.cobblemon.mod.common.api.moves.*;
import com.cobblemon.mod.common.api.pokemon.moves.LearnsetQuery;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;

public class PokemonHM extends BasePokemonTM {
    public PokemonHM() {
        super(new Item.Settings().maxCount(1).group(ItemGroup.MISC).rarity(Rarity.UNCOMMON), true);
    }

}
