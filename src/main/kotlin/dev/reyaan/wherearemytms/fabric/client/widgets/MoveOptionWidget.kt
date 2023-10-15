package dev.reyaan.wherearemytms.fabric.client.widgets

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.api.moves.Move
import com.cobblemon.mod.common.api.text.bold
import com.cobblemon.mod.common.api.text.gold
import com.cobblemon.mod.common.api.text.red
import com.cobblemon.mod.common.client.gui.MoveCategoryIcon
import com.cobblemon.mod.common.client.gui.TypeIcon
import com.cobblemon.mod.common.client.render.drawScaledText
import com.cobblemon.mod.common.util.math.toRGB
import dev.reyaan.wherearemytms.fabric.WAMT
import dev.reyaan.wherearemytms.fabric.client.screen.TMMachineScreen
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper


class MoveOptionWidget(
    pX: Int,
    pY: Int,
    val move: Move,
    var disabled: Boolean = false,
    resource: String = "battle_move",
    onPress: PressAction,
): ButtonWidget(pX, pY, WIDTH, HEIGHT, Text.literal(move.name), onPress, DEFAULT_NARRATION_SUPPLIER) {

    private val buttonResource = WAMT.id("textures/gui/$resource.png")
    private val overlayResource = WAMT.id("textures/gui/battle_move_overlay.png")
    companion object {
        private const val WIDTH = 92
        private const val HEIGHT = 24
    }

    val typeIcon = TypeIcon(
        x = x - 2,
        y = y + 3,
        type = move.template.elementalType
    )

    val categoryIcon = MoveCategoryIcon(
        x = x + 76,
        y = y + 13.5,
        category = move.template.damageCategory
    )

    private fun getOffset(): Int {
        return if (hovered) HEIGHT
        else if (disabled) HEIGHT*2
        else 0
    }

    override fun renderButton(drawContext: DrawContext, mouseX: Int, mouseY: Int, pPartialTicks: Float) {
        val rgb = move.template.elementalType.hue.toRGB()
        val matrices = drawContext.matrices

        blitk(
            matrixStack = matrices,
            texture = buttonResource,
            x = x,
            y = y,
            width = WIDTH,
            height = HEIGHT,
            vOffset = getOffset(),
            textureHeight = HEIGHT * 3,
            red = rgb.first,
            green = rgb.second,
            blue = rgb.third,
        )

        blitk(
            matrixStack = drawContext.matrices,
            texture = overlayResource,
            x = x,
            y = y,
            width = WIDTH,
            height = HEIGHT
        )

        var movePPText = Text.literal("${move.currentPp}/${move.maxPp}").bold()

        if (move.currentPp <= MathHelper.floor(move.maxPp / 2F)) {
            movePPText = if (move.currentPp == 0) movePPText.red() else movePPText.gold()
        }

        drawScaledText(
            drawContext,
            font = TMMachineScreen.DEFAULT_LARGE,
            text = movePPText,
            x = x + 60,
            y = y + 13,
            centered = true
        )

        typeIcon.render(drawContext)
        categoryIcon.render(drawContext)

        drawScaledText(
            drawContext,
            font = TMMachineScreen.DEFAULT_LARGE,
            text = move.template.displayName.bold(),
            x = x + 22,
            y = y + 2,
            shadow = true
        )
    }

    override fun playDownSound(soundManager: SoundManager) {
        soundManager.play(PositionedSoundInstance.master(CobblemonSounds.GUI_CLICK, 0.8f, 0.9f))
    }

    override fun isValidClickButton(button: Int): Boolean {
        return !disabled
    }

    fun isHovered(mouseX: Double, mouseY: Double): Boolean {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height
    }
}