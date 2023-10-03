package dev.reyaan.wherearemytms.fabric.client.screen

import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.api.moves.Move
import com.cobblemon.mod.common.api.text.bold
import com.cobblemon.mod.common.api.text.text
import com.cobblemon.mod.common.client.gui.summary.widgets.ModelWidget
import com.cobblemon.mod.common.client.render.drawScaledText
import com.cobblemon.mod.common.pokemon.Gender
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.lang
import dev.reyaan.wherearemytms.fabric.WAMT.TM_MACHINE_MOVE_SELECT_PACKET_ID
import dev.reyaan.wherearemytms.fabric.WAMT.id
import dev.reyaan.wherearemytms.fabric.client.widgets.MoveOptionWidget
import dev.reyaan.wherearemytms.fabric.client.widgets.SlotChangeWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.Identifier


@Environment(EnvType.CLIENT)
class TMMachineScreen(private val party: List<Pokemon?>):
    Screen(Text.literal("TM Machine")) {

    companion object {
        const val BASE_WIDTH = 189
        const val BASE_HEIGHT = 136
        private const val PORTRAIT_SIZE = 66

        val MAIN_BACKGROUND: Identifier = id("textures/gui/summary_base.png")
        private val PORTRAIT_BACKGROUND = id("textures/gui/portrait_background.png")

        val DEFAULT_LARGE = Identifier("uniform")
    }

    private var modelWidget: ModelWidget? = null

    private var backButton: SlotChangeWidget? = null
    private var nextButton: SlotChangeWidget? = null

    private val moveOptions: ArrayList<MoveOptionWidget> = ArrayList()

    private fun getNonNullPartyMembers(party: List<Pokemon?>): List<Pokemon> {
        val nonNullParty = ArrayList<Pokemon>()
        party.forEach { if (it != null) nonNullParty.add(it) }
        return nonNullParty
    }

    private var currentSlot = 0;
    private lateinit var nonNullParty: List<Pokemon>

    private fun refresh(x: Int, y: Int) {
        val pokemon = nonNullParty[currentSlot]
        modelWidget?.pokemon = pokemon.asRenderablePokemon()
        addMoves(pokemon, x, y)
    }

    private fun addMoves(pokemon: Pokemon, x: Int, y: Int) {
        moveOptions.clear()
        val tmMoves = pokemon.species.moves.tmMoves

        pokemon.moveSet.forEachIndexed {i, move ->
            moveOptions.add(
                MoveOptionWidget(
                    pX = x + 88,
                    pY = y + 18 + (27 * i),
                    move = move,
                    disabled = move.template !in tmMoves
                ) {
                    useMoveOption(move)
                }
            )
        }
    }

    fun useMoveOption(move: Move) {
        val buf = PacketByteBufs.create()
        val nbtCompound = NbtCompound()
        move.saveToNBT(nbtCompound)
        buf.writeNbt(nbtCompound)
        ClientPlayNetworking.send(TM_MACHINE_MOVE_SELECT_PACKET_ID, buf)
        super.close()
    }



    override fun init() {
        clearChildren()
        super.init()

        nonNullParty = getNonNullPartyMembers(party)

        if (nonNullParty.isEmpty()) {
            MinecraftClient.getInstance().player?.sendMessage(
                Text.of("You should probably get a Pokemon first..")
            )
            super.close()
        } else {

            val x = (width - BASE_WIDTH) / 2
            val y = (height - BASE_HEIGHT) / 2

            backButton = SlotChangeWidget(
                    pX = x + 10,
                    pY = y + 105,
                    resource = "battle_back"
                ) {
                    currentSlot = maxOf(currentSlot - 1, 0)
                    refresh(x, y)
                }


            nextButton = SlotChangeWidget(
                    pX = x + 12,
                    pY = y - 33,
                    resource = "battle_forward"
                ) {
                    currentSlot = minOf(currentSlot + 1, nonNullParty.size - 1)
                    refresh(x, y)
                }


            val pokemon = nonNullParty[currentSlot]

            modelWidget = ModelWidget(
                pX = x + 5,
                pY = y + 24,
                pWidth = PORTRAIT_SIZE,
                pHeight = PORTRAIT_SIZE,
                pokemon = pokemon.asRenderablePokemon(),
                baseScale = 2F,
                rotationY = 325F,
                offsetY = -10.0
            )

            addMoves(pokemon, x, y)
        }
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)

        val x = (width - BASE_WIDTH) / 2
        val y = (height - BASE_HEIGHT) / 2

        // portrait background
        blitk(
            matrixStack = matrices,
            texture = PORTRAIT_BACKGROUND,
            x = x + 5,
            y = y + 27,
            width = PORTRAIT_SIZE,
            height = PORTRAIT_SIZE
        )

        // pokemon model
        modelWidget?.render(matrices, mouseX, mouseY, delta)

        // actual background
        blitk(
            matrixStack = matrices,
            texture = MAIN_BACKGROUND,
            x = x,
            y = y,
            width = BASE_WIDTH,
            height = BASE_HEIGHT
        )


        val pokemon = nonNullParty[currentSlot]
        // Draw labels
        drawScaledText(
            matrixStack = matrices,
            font = DEFAULT_LARGE,
            text = pokemon.displayName.bold(),
            x = x + 5,
            y = y + 9,
            shadow = true
        )

        val gender = pokemon.gender
        if (gender != Gender.GENDERLESS) {
            val isMale = gender == Gender.MALE
            val textSymbol = if (isMale) "♂".text().bold() else "♀".text().bold()
            drawScaledText(
                matrixStack = matrices,
                font = DEFAULT_LARGE,
                text = textSymbol,
                x = x + 5,
                y = y + 15,
                colour = if (isMale) 0x32CBFF else 0xFC5454,
                shadow = true,
                scale=1.25F
            )
        }

        drawScaledText(
            matrixStack = matrices,
            font = DEFAULT_LARGE,
            text = lang("ui.lv").bold(),
            x = x + 61.5,
            y = y + 7.5,
            shadow = true,
            scale=0.6F
        )

        drawScaledText(
            matrixStack = matrices,
            font = DEFAULT_LARGE,
            text = pokemon.level.toString().text().bold(),
            x = x + 61.5,
            y = y + 10.5,
            shadow = true
        )

        moveOptions.forEach {
            it.render(matrices, mouseX, mouseY, delta)
        }

        backButton?.render(matrices, mouseX, mouseY, delta)
        nextButton?.render(matrices, mouseX, mouseY, delta)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        backButton?.let {
            if (it.isHovered(mouseX, mouseY)) {
                it.onPress()
                return true
            }
        }

        nextButton?.let {
            if (it.isHovered(mouseX, mouseY)) {
                it.onPress()
                return true
            }
        }

        moveOptions.forEach {
            if (it.isHovered(mouseX, mouseY)) {
                it.onPress()
                return true
            }
        }
        return false
    }
}

