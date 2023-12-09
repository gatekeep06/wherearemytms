package dev.reyaan.wherearemytms.fabric.util

import com.cobblemon.mod.common.api.text.font
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.MutableText
import net.minecraft.util.Identifier
import org.joml.Matrix4f

//В чем же фикс? Просто скопировал+вставил фунцкии, которые он не мог найти в файл, который он точно сможет. Профит

fun blitk(
    matrixStack: MatrixStack,
    texture: Identifier? = null,
    x: Number,
    y: Number,
    height: Number = 0,
    width: Number = 0,
    uOffset: Number = 0,
    vOffset: Number = 0,
    textureWidth: Number = width,
    textureHeight: Number = height,
    blitOffset: Number = 0,
    red: Number = 1,
    green: Number = 1,
    blue: Number = 1,
    alpha: Number = 1F,
    blend: Boolean = true,
    scale: Float = 1F
) {
    RenderSystem.setShader { GameRenderer.getPositionTexProgram() }
    texture?.run { RenderSystem.setShaderTexture(0, this) }
    if (blend) {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
    }
    RenderSystem.setShaderColor(red.toFloat(), green.toFloat(), blue.toFloat(), alpha.toFloat())
    matrixStack.push()
    matrixStack.scale(scale, scale, 1F)
    drawRectangle(
        matrixStack.peek().positionMatrix,
        x.toFloat(), y.toFloat(), x.toFloat() + width.toFloat(), y.toFloat() + height.toFloat(),
        blitOffset.toFloat(),
        uOffset.toFloat() / textureWidth.toFloat(), (uOffset.toFloat() + width.toFloat()) / textureWidth.toFloat(),
        vOffset.toFloat() / textureHeight.toFloat(), (vOffset.toFloat() + height.toFloat()) / textureHeight.toFloat()
    )
    matrixStack.pop()
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
}

fun drawRectangle(
    matrix: Matrix4f,
    x: Float,
    y: Float,
    endX: Float,
    endY: Float,
    blitOffset: Float,
    minU: Float,
    maxU: Float,
    minV: Float,
    maxV: Float
) {
    val bufferbuilder = Tessellator.getInstance().buffer
    bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
    bufferbuilder.vertex(matrix, x, endY, blitOffset).texture(minU, maxV).next()
    bufferbuilder.vertex(matrix, endX, endY, blitOffset).texture(maxU, maxV).next()
    bufferbuilder.vertex(matrix, endX, y, blitOffset).texture(maxU, minV).next()
    bufferbuilder.vertex(matrix, x, y, blitOffset).texture(minU, minV).next()
    // TODO: Figure out if this is correct replacement.
    // OLD: BufferRenderer.draw(bufferbuilder)
    BufferRenderer.drawWithGlobalProgram(bufferbuilder.end())
}

fun drawText(
    context: DrawContext,
    font: Identifier? = null,
    text: MutableText,
    x: Number,
    y: Number,
    centered: Boolean = false,
    colour: Int,
    shadow: Boolean = true,
    pMouseX: Number? = null,
    pMouseY: Number? = null
): Boolean {
    val comp = if (font == null) text else text.setStyle(text.style.withFont(font))
    val textRenderer = MinecraftClient.getInstance().textRenderer
    var x = x
    val width = textRenderer.getWidth(comp)
    if (centered) {
        x = x.toDouble() - width / 2
    }
    context.drawText(textRenderer, comp, x.toInt(), y.toInt(), colour, shadow)
    var isHovered = false
    if (pMouseY != null && pMouseX != null) {
        if (pMouseX.toInt() >= x.toInt() && pMouseX.toInt() <= x.toInt() + width &&
            pMouseY.toInt() >= y.toInt() && pMouseY.toInt() <= y.toInt() + textRenderer.fontHeight
        ) {
            isHovered = true
        }
    }
    return isHovered
}

fun drawScaledText(
    context: DrawContext,
    font: Identifier? = null,
    text: MutableText,
    x: Number,
    y: Number,
    scale: Float = 1F,
    opacity: Number = 1F,
    maxCharacterWidth: Int = Int.MAX_VALUE,
    colour: Int = 0x00FFFFFF + ((opacity.toFloat() * 255).toInt() shl 24),
    centered: Boolean = false,
    shadow: Boolean = false,
    pMouseX: Int? = null,
    pMouseY: Int? = null
) {
    if (opacity.toFloat() < 0.05F) {
        return
    }

    val textWidth = MinecraftClient.getInstance().textRenderer.getWidth(if (font != null) text.font(font) else text)
    val extraScale = if (textWidth < maxCharacterWidth) 1F else (maxCharacterWidth / textWidth.toFloat())
    val fontHeight = if (font == null) 5 else 6
    val matrices = context.matrices
    matrices.push()
    matrices.scale(scale * extraScale, scale * extraScale, 1F)
    val isHovered = drawText(
        context = context,
        font = font,
        text = text,
        x = x.toFloat() / (scale * extraScale),
        y = y.toFloat() / (scale * extraScale) + (1 - extraScale) * fontHeight * scale,
        centered = centered,
        colour = colour,
        shadow = shadow,
        pMouseX = pMouseX?.toFloat()?.div((scale * extraScale)),
        pMouseY = pMouseY?.toFloat()?.div(scale * extraScale)?.plus((1 - extraScale) * fontHeight * scale)
    )
    matrices.pop()
    // Draw tooltip that was created with onHover and is attached to the MutableText
    if (isHovered) {
        context.drawHoverEvent(MinecraftClient.getInstance().textRenderer, text.style, pMouseX!!, pMouseY!!)
    }
}

