package dev.reyaan.wherearemytms.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

@Environment(EnvType.CLIENT)
public class RenderUtils {
    public static void s_blitk(MatrixStack matrices, Identifier identifier, float x, float y, int width, int height) {
        blitk(
                matrices,
                identifier,
                x,
                y,
                height,
                width,
                0,
                0,
                width,
                height,
                0,
                1,
                1,
                1,
                1F,
                true,
                1F
        );
    }

    public static void s_blitk(MatrixStack matrices, Identifier identifier, float x, float y, int width, int height, int vOffset, Color rgb, float opacity) {
        blitk(
                matrices,
                identifier,
                x,
                y,
                height,
                width,
                0,
                vOffset,
                width,
                height,
                0,
                rgb.getRed()/255F,
                rgb.getGreen()/255F,
                rgb.getBlue()/255F,
                opacity,
                true,
                1F
        );
    }
}
