package dev.reyaan.wherearemytms.fabric.client.widgets

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.gui.blitk
import dev.reyaan.wherearemytms.fabric.WAMT.id
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text


class SlotChangeWidget(
    pX: Int,
    pY: Int,
    resource: String,
    onPress: PressAction,
): ButtonWidget(pX, pY, WIDTH, HEIGHT, Text.literal("Change"), onPress) {

    private val buttonResource = id("textures/gui/$resource.png")
    companion object {
        private const val WIDTH = 58
        private const val HEIGHT = 34
//        private const val SCALE = 0.5F
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, pPartialTicks: Float) {
        blitk(
            matrixStack = matrices,
            texture = buttonResource,
            x = x,
            y = y,
            width = WIDTH,
            height = HEIGHT,
            vOffset = if (isHovered(mouseX.toDouble(), mouseY.toDouble())) HEIGHT else 0,
            textureHeight = HEIGHT * 2,
//            scale = SCALE
        )
    }

    override fun playDownSound(soundManager: SoundManager) {
        soundManager.play(PositionedSoundInstance.master(CobblemonSounds.GUI_CLICK.get(), 0.8f, 0.9f))
    }

    fun isHovered(mouseX: Double, mouseY: Double) = mouseX.toFloat() in (x.toFloat()..(x.toFloat() + WIDTH)) && mouseY.toFloat() in (y.toFloat()..(y.toFloat() + HEIGHT))
}