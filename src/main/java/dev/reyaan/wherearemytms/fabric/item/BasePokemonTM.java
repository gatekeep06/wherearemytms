package dev.reyaan.wherearemytms.fabric.item;

import com.cobblemon.mod.common.api.moves.*;
import com.cobblemon.mod.common.api.pokemon.moves.LearnsetQuery;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class BasePokemonTM extends Item {
    public boolean oneTimeUse;

    public BasePokemonTM(Settings settings, boolean oneTimeUse) {
        super(settings);
        this.oneTimeUse = oneTimeUse;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        // User checks (null in-case of cross-compat)
        if (user != null && hand == user.getActiveHand()) {

            // Interacted entity is Pokemon and user is owner
            if (entity instanceof PokemonEntity pokemonEntity && pokemonEntity.isOwner(user)) {

                // TM has move & type data attached
                NbtCompound nbtCompound = stack.getOrCreateNbt();
                if (nbtCompound.contains("template")) {

                    Pokemon pokemon = pokemonEntity.getPokemon();
                    System.out.println("NBT TM: " + nbtCompound);
                    MoveTemplate moveTemplate = Moves.INSTANCE.getByName(nbtCompound.getString("template"));
                    System.out.println("Move TM exists" + nbtCompound + " -> " + moveTemplate);

                    // Pokemon can learn move
                    if (teach(pokemon, moveTemplate)) {
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        // HM
                        if (((BasePokemonTM) stack.getItem()).oneTimeUse && !user.getAbilities().creativeMode) {
                            stack.decrement(1);
                        }
                        return ActionResult.CONSUME;
                    }

                    return ActionResult.FAIL;
                }
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }


    protected boolean teach(Pokemon pokemon, MoveTemplate moveTemplate) {
        MoveSet moves = pokemon.getMoveSet();
        BenchedMoves benchedMoves = pokemon.getBenchedMoves();

        // Move is not part of learnset
        if (!LearnsetQuery.Companion.getTM_MOVE().canLearn(moveTemplate, pokemon.getForm().getMoves()) &&
                !LearnsetQuery.Companion.getEGG_MOVE().canLearn(moveTemplate, pokemon.getForm().getMoves())) {
            System.out.println("Cannot teach because it cannot learn!");
            return false;
        }

        // Move is already a part of moveset
        if (moveInMoveSets(moveTemplate, moves, benchedMoves)) {
            System.out.println("Cannot teach because it already knows!");
            return false;
        }

        // Add to moves or benched moves
        if (moves.hasSpace()) {
            moves.add(moveTemplate.create());
        } else {
            benchedMoves.add(new BenchedMove(moveTemplate, 0));
        }

        System.out.println("Taught move!");

        return true;
    }


    public static boolean moveInMoveSets(MoveTemplate moveTemplate, MoveSet moves, BenchedMoves benchedMoves) {
        for (Move it : moves) {
            if (it.getTemplate() == moveTemplate) {
                return true;
            }
        }

        for (BenchedMove it : benchedMoves) {
            if (it.getMoveTemplate() == moveTemplate) {
                return true;
            }
        }

        return false;
    }

}
