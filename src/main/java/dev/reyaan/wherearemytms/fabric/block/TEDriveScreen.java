package dev.reyaan.wherearemytms.fabric.block;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.CobblemonResources;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import com.cobblemon.mod.common.client.battle.ClientBattlePokemon;
import com.cobblemon.mod.common.client.render.models.blockbench.PoseableEntityState;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.cobblemon.mod.common.pokemon.activestate.PokemonState;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.reyaan.wherearemytms.fabric.WhereAreMyTMsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.drawPortraitPokemon;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;
import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;
import static dev.reyaan.wherearemytms.fabric.WhereAreMyTMs.TE_DRIVE_CLOSE_PACKET_ID;
import static dev.reyaan.wherearemytms.fabric.WhereAreMyTMs.id;
import static dev.reyaan.wherearemytms.fabric.block.RenderUtils.s_blitk;

@Environment(EnvType.CLIENT)
public class TEDriveScreen extends HandledScreen<TEDriveScreenHandler> {
    public static final Identifier battleInfoBase = id("textures/gui/battle_info_base.png");
    public static final Identifier battleInfoUnderlay = id("textures/gui/battle_info_underlay.png");

    public static final Identifier DEFAULT_LARGE = new Identifier("uniform");

    //    private static final int HORIZONTAL_INSET = width/2;
//    private static final int VERTICAL_INSET = 10;
//    private static final int HORIZONTAL_SPACING = 15;
//    private static final int VERTICAL_SPACING = 40;
    private static final int INFO_OFFSET_X = 7;

    private static final int TILE_WIDTH = 131;
    private static final int TILE_HEIGHT = 40;
    private static final int PORTRAIT_DIAMETER = 28;
    private static final int PORTRAIT_OFFSET_X = 5;
    private static final int PORTRAIT_OFFSET_Y = 8;

    List<MoveOptionWidget> optionWidgets = new ArrayList<>();


    public TEDriveScreen(TEDriveScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.optionWidgets.clear();

        Pokemon pokemon = WhereAreMyTMsClient.te_drive_pokemon;
        if (pokemon != null) {
            drawMainPokemon(
                    matrices,
                    width / 2.0F - TILE_WIDTH/2.0F,
                    height / 2.0F + TILE_HEIGHT,
                    pokemon.getSpecies(),
                    pokemon.getLevel(),
                    pokemon.getAspects(),
                    pokemon.getDisplayName(),
                    pokemon.getGender(),
                    pokemon.getState()
            );

            drawMoveSet(matrices,
                    width / 2.0F,
                    height / 2.0F,
                    pokemon.getMoveSet(),
                    mouseX,
                    mouseY
            );
        }
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }


    private void drawMoveSet(
            MatrixStack matrices,
            float originX,
            float originY,
            MoveSet moveSet,
            int mouseX,
            int mouseY
    ) {
        Move move = moveSet.get(0);
        if (move != null) {
            MoveOptionWidget widget = new MoveOptionWidget(move.getTemplate(), originX, originY);
            widget.hovered = widget.isHovered(mouseX, mouseY);
            widget.render(matrices);
            this.optionWidgets.add(widget);
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (MoveOptionWidget widget : optionWidgets) {
            if (widget.isHovered(mouseX, mouseY)) {
                this.getScreenHandler().selectedMove = widget.moveTemplate;
                PacketByteBuf buf = PacketByteBufs.create();
                ClientPlayNetworking.send(TE_DRIVE_CLOSE_PACKET_ID, buf);
                return true;
            }
        }
        return false;
    }

    private void drawMainPokemon(
            MatrixStack matrices,
            float x,
            float y,
            Species species,
            int level,
            Set<String> aspects,
            MutableText displayName,
            Gender gender,
            PokemonState state
    ) {
        // Underlay
        float portraitStartX = x + (PORTRAIT_OFFSET_X);
        s_blitk(
                matrices,
                battleInfoUnderlay,
                portraitStartX,
                y + PORTRAIT_OFFSET_Y,
                PORTRAIT_DIAMETER,
                PORTRAIT_DIAMETER
        );

        // Portrait
        DrawableHelper.enableScissor(
                Math.round(portraitStartX),
                Math.round(y + PORTRAIT_OFFSET_Y),
                Math.round(portraitStartX + PORTRAIT_DIAMETER),
                Math.round(y + PORTRAIT_DIAMETER + PORTRAIT_OFFSET_Y)
        );
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(
                portraitStartX + PORTRAIT_DIAMETER / 2.0,
                (double) y + PORTRAIT_OFFSET_Y - 5.0,
                0.0
        );
        matrixStack.push();
        drawPortraitPokemon(
                species,
                aspects,
                matrixStack,
                18F,
                false,
                null
        );
        matrixStack.pop();
        DrawableHelper.disableScissor();

        // Base
        s_blitk(
                matrices,
                battleInfoBase,
                x,
                y,
                TILE_WIDTH,
                TILE_HEIGHT
        );

        // Display Name
        float infoBoxX = x + PORTRAIT_DIAMETER + PORTRAIT_OFFSET_X + INFO_OFFSET_X;
        drawScaledText(
                matrices,
                DEFAULT_LARGE,
                displayName.formatted(Formatting.BOLD),
                infoBoxX,
                y + 7,
                1F,
                1F,
                Integer.MAX_VALUE,
                0x00FFFFFF,
                false,
                true
        );

        // Gender
        if (gender != Gender.GENDERLESS) {
            boolean isMale = gender == Gender.MALE;
            drawScaledText(
                    matrices,
                    DEFAULT_LARGE,
                    Text.literal(isMale ? "♂" : "♀").formatted(Formatting.BOLD),
                    infoBoxX + 53,
                    y + 7,
                    1F,
                    1F,
                    Integer.MAX_VALUE,
                    isMale ? 0x32CBFF : 0xFC5454,
                    false,
                    true
            );
        }

        // Lv. Text
        drawScaledText(
                matrices,
                DEFAULT_LARGE,
                lang("ui.lv").formatted(Formatting.BOLD),
                infoBoxX + 59,
                y + 7,
                1F,
                1F,
                Integer.MAX_VALUE,
                0x00FFFFFF,
                false,
                true
        );

        // Level Number
        drawScaledText(
                matrices,
                DEFAULT_LARGE,
                Text.literal(String.valueOf(level)).formatted(Formatting.BOLD),
                infoBoxX + 72,
                y + 7,
                1F,
                1F,
                Integer.MAX_VALUE,
                0x00FFFFFF,
                false,
                true
        );
    }
}
