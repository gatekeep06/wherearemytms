package dev.reyaan.wherearemytms.fabric.block;

import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.cobblemon.mod.common.pokemon.activestate.PokemonState;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.reyaan.wherearemytms.fabric.WhereAreMyTMsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Set;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.drawPortraitPokemon;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;
import static dev.reyaan.wherearemytms.fabric.WhereAreMyTMs.id;

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

    public static void s_blitk(MatrixStack matrices, Identifier identifier, float x, float y, int width, int height, int uOffset, Color rgb, float opacity) {
        blitk(
                matrices,
                identifier,
                x,
                y,
                height,
                width,
                uOffset,
                0,
                width,
                height,
                0,
                rgb.getRed(),
                rgb.getGreen(),
                rgb.getBlue(),
                opacity,
                true,
                1F
        );
    }
}
