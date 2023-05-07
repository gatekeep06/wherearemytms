package dev.reyaan.wherearemytms.fabric.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static dev.reyaan.wherearemytms.fabric.WhereAreMyTMs.id;

@Environment(EnvType.CLIENT)
public class NextButtonWidget {
    public static final Identifier backTexture = id("textures/gui/battle_back.png");
    public static final Identifier forwardTexture = id("textures/gui/battle_forward.png");

    public static final int TILE_WIDTH = 58;
    public static final int TILE_HEIGHT = 34;

    public boolean back;
    float x;
    float y;
    boolean hovered;

    public NextButtonWidget(boolean back, float x, float y) {
        this.back = back;
        this.x = x - TILE_WIDTH/2.0F;
        this.y = y - TILE_HEIGHT/2.0F;
    }

    public void render(MatrixStack matrices) {
        blitk(
                matrices,
                back ? backTexture : forwardTexture,
                x,
                y,
                TILE_HEIGHT,
                TILE_WIDTH,
                0,
                hovered ? TILE_HEIGHT : 0,
                TILE_WIDTH,
                TILE_HEIGHT*2,
                0,
                1.0F,
                1.0F,
                1.0F,
                1.0F,
                true,
                1F
        );
    }

    boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + TILE_WIDTH && mouseY >= y && mouseY <= y + TILE_HEIGHT;
    }
}
