package dev.reyaan.wherearemytms.fabric.item;

import com.cobblemon.mod.common.api.moves.*;
import com.cobblemon.mod.common.api.pokemon.moves.LearnsetQuery;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasePokemonTM extends Item {
    public String title;
    public String usesKey;
    public boolean oneTimeUse;

    public BasePokemonTM(Settings settings, String title, String usesKey, boolean oneTimeUse) {
        super(settings);
        this.title = title;
        this.usesKey = usesKey;
        this.oneTimeUse = oneTimeUse;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user == null || user.world.isClient()) {
            return ActionResult.SUCCESS;
        } else {
            // User checks (null in-case of cross-compat)
            if (hand == user.getActiveHand()) {

                // Interacted entity is Pokemon and user is owner
                if (entity instanceof PokemonEntity pokemonEntity && pokemonEntity.isOwner(user)) {

                    // TM has move & type data attached
                    NbtCompound nbtCompound = stack.getOrCreateNbt();
                    if (nbtCompound.contains("move")) {

                        Pokemon pokemon = pokemonEntity.getPokemon();
                        MoveTemplate moveTemplate = Moves.INSTANCE.getByName(nbtCompound.getString("move"));
                        if (moveTemplate != null) {

                            // Pokemon can learn move
                            if (teach(user, pokemon, moveTemplate)) {
                                user.incrementStat(Stats.USED.getOrCreateStat(this));
                                // HM
                                if (((BasePokemonTM) stack.getItem()).oneTimeUse && !user.getAbilities().creativeMode) {
                                    stack.decrement(1);
                                }
                                return ActionResult.CONSUME;
                            }
                        }

                        return ActionResult.FAIL;
                    }
                }
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }


    protected boolean teach(PlayerEntity user, Pokemon pokemon, MoveTemplate moveTemplate) {
        MoveSet moves = pokemon.getMoveSet();
        BenchedMoves benchedMoves = pokemon.getBenchedMoves();

        // Move is already a part of moveset
        if (moveInMoveSets(moveTemplate, moves, benchedMoves)) {
            user.sendMessage(create_response(
                    "response.wherearemytms.already_learned",
                    pokemon.getDisplayName(),
                    moveTemplate.getDisplayName(),
                    Formatting.RED)
            );
            return false;
        }

        // Move is not part of learnset
        if (!LearnsetQuery.Companion.getTM_MOVE().canLearn(moveTemplate, pokemon.getForm().getMoves()) &&
                !LearnsetQuery.Companion.getEGG_MOVE().canLearn(moveTemplate, pokemon.getForm().getMoves())) {
            user.sendMessage(create_response(
                    "response.wherearemytms.cannot_learn",
                    pokemon.getDisplayName(),
                    moveTemplate.getDisplayName(),
                    Formatting.RED)
            );
            return false;
        }

        // Add to moves or benched moves
        if (moves.hasSpace()) {
            moves.add(moveTemplate.create());
        } else {
            benchedMoves.add(new BenchedMove(moveTemplate, 0));
        }

        user.sendMessage(create_response(
                "response.wherearemytms.success",
                pokemon.getDisplayName(),
                moveTemplate.getDisplayName(),
                Formatting.GOLD)
        );
        return true;
    }

    private static Text create_response(String key, MutableText pokemon, MutableText move, Formatting color) {
        return Text.translatable(key, pokemon.getString(), move.getString())
                .formatted(color);
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


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();

        if (nbtCompound.contains("type") && nbtCompound.contains("hue")) {
            tooltip.add(Text.literal(
                            nbtCompound.getString("type"))
                    .setStyle(Style.EMPTY.withColor(nbtCompound.getInt("hue")))
            );
        } else {
            tooltip.add(Text.translatable(this.usesKey).formatted(Formatting.GOLD));
            tooltip.add(Text.translatable("description.wherearemytms.requires_TE_drive").formatted(Formatting.GRAY));
        }
    }

    public static boolean isTMBlank(ItemStack stack) {
        return !stack.getOrCreateNbt().contains("move");
    }

    public static boolean isTMBlank(NbtCompound nbtCompound) {
        return !nbtCompound.contains("move");
    }
}
