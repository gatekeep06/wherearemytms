package dev.reyaan.wherearemytms.fabric.item

import com.cobblemon.mod.common.api.moves.*
import com.cobblemon.mod.common.api.moves.Moves.getByName
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import dev.reyaan.wherearemytms.fabric.WAMT
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.stat.Stats
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.world.World

class MoveTransferItem(settings: Settings, val breaksAfterUse: Boolean, val titleKey: String): Item(settings) {
    protected fun teach(user: PlayerEntity, pokemon: Pokemon, moveTemplate: MoveTemplate): Boolean {
        val moves = pokemon.moveSet
        val benchedMoves = pokemon.benchedMoves

        // Move is already a part of moveset
        if (moveInMoveSets(moveTemplate, moves, benchedMoves)) {
            user.sendMessage(
                createResponse(
                    "response.wherearemytms.already_learned",
                    pokemon.displayName,
                    moveTemplate.displayName,
                    Formatting.RED
                )
            )
            return false
        }

        // Move is not part of learnset
        val learnset = pokemon.form.moves
        if (WAMT.config.allowEggMoves) {
            if (learnset.eggMoves.contains(moveTemplate)) {
                addMove(user, pokemon, moveTemplate, moves, benchedMoves)
                return true
            }
        }

        if (WAMT.config.allowTutorMoves) {
            if (learnset.tutorMoves.contains(moveTemplate)) {
                addMove(user, pokemon, moveTemplate, moves, benchedMoves)
                return true
            }
        }

        if (learnset.tmMoves.contains(moveTemplate)) {
            addMove(user, pokemon, moveTemplate, moves, benchedMoves)
            return true
        }

        user.sendMessage(
            createResponse(
                "response.wherearemytms.cannot_learn",
                pokemon.displayName,
                moveTemplate.displayName,
                Formatting.RED
            )
        )
        return false
    }

    protected fun moveInMoveSets(moveTemplate: MoveTemplate, moves: MoveSet, benchedMoves: BenchedMoves): Boolean {
        return moves.any { it.template == moveTemplate } || benchedMoves.any { it.moveTemplate == moveTemplate }
    }

    override fun useOnEntity(stack: ItemStack, user: PlayerEntity?, entity: LivingEntity, hand: Hand): ActionResult {
        if (user == null || user.world.isClient()) {
            return ActionResult.SUCCESS
        } else {
            // User checks (null in-case of cross-compat)
            if (hand == user.activeHand) {

                // Interacted entity is Pokemon and user is owner
                if (entity is PokemonEntity && entity.isOwner(user)) {

                    // TM has move & type data attached
                    val nbtCompound = stack.getOrCreateNbt()
                    if (nbtCompound.contains("move")) {
                        val moveTemplate = Moves.getByName(nbtCompound.getString("move"))

                        if (moveTemplate != null) {
                            // Pokemon can learn move
                            if (teach(user, entity.pokemon, moveTemplate)) {
                                user.incrementStat(Stats.USED.getOrCreateStat(this))

                                // Uses
                                if (!user.abilities.creativeMode && breaksAfterUse) {
                                    stack.decrement(1)
                                }
                                return ActionResult.CONSUME
                            }
                        }
                        return ActionResult.FAIL
                    }
                }
            }
        }
        return super.useOnEntity(stack, user, entity, hand)
    }


    private fun createResponse(key: String, pokemon: MutableText, move: MutableText, color: Formatting): Text? {
        return Text.translatable(key, pokemon.string, move.string)
            .formatted(color)
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text?>, context: TooltipContext?) {
        val nbtCompound = stack.getOrCreateNbt()
        if (nbtCompound.contains("move")) {
            val move = nbtCompound.getString("move")
            val template = getByName(move)
            if (template != null) {
                tooltip.add(
                    template.elementalType.displayName.setStyle(Style.EMPTY.withColor(Formatting.GRAY))
                )
            } else {
                tooltip.add(
                    Text.of("Move ($move) was not found?")
                )
            }
        } else {
            if (breaksAfterUse) {
                tooltip.add(Text.translatable("description.wherearemytms.breaks_after_use").formatted(Formatting.GOLD))
            }

            tooltip.add(
                Text.translatable("description.wherearemytms.requires_printing")
                    .formatted(Formatting.GRAY)
            )
        }
    }

    fun addMove(
        user: PlayerEntity,
        pokemon: Pokemon,
        moveTemplate: MoveTemplate,
        moves: MoveSet,
        benchedMoves: BenchedMoves,
    ) {
        if (moves.hasSpace()) {
            moves.add(moveTemplate.create())
        } else {
            benchedMoves.add(BenchedMove(moveTemplate, 0))
        }

        user.sendMessage(
            createResponse(
                "response.wherearemytms.success",
                pokemon.displayName,
                moveTemplate.displayName,
                Formatting.GOLD
            )
        )
    }
}