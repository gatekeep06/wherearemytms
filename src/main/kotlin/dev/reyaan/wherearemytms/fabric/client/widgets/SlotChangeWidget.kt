package dev.reyaan.wherearemytms.fabric.client.widgets

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.gui.blitk
import dev.reyaan.wherearemytms.fabric.WAMT.id
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.text.Text


class SlotChangeWidget(
    pX: Int,
    pY: Int,
    resource: String,
    onPress: PressAction,
): ButtonWidget(pX, pY, WIDTH, HEIGHT, Text.literal("Change"), onPress, DEFAULT_NARRATION_SUPPLIER) {

    private val buttonResource = id("textures/gui/$resource.png")
    companion object {
        private const val WIDTH = 58
        private const val HEIGHT = 34
    }


    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, pPartialTicks: Float) {
        blitk(
            matrixStack = drawContext.matrices,
            texture = buttonResource,
            x = x,
            y = y,
            width = WIDTH,
            height = HEIGHT,
            uOffset = 0,
            vOffset = if (isHovered(mouseX.toDouble(), mouseY.toDouble())) HEIGHT else 0,
            textureWidth = WIDTH,
            textureHeight = HEIGHT * 2,
        )
    }

    override fun playDownSound(soundManager: SoundManager) {
        soundManager.play(PositionedSoundInstance.master(CobblemonSounds.GUI_CLICK, 0.8f, 0.9f))
    }

    fun isHovered(mouseX: Double, mouseY: Double) = mouseX.toFloat() in (x.toFloat()..(x.toFloat() + WIDTH)) && mouseY.toFloat() in (y.toFloat()..(y.toFloat() + HEIGHT))
}