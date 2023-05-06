package dev.reyaan.wherearemytms.fabric.block;


import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import com.cobblemon.mod.common.client.battle.ClientBattlePokemon;
import com.cobblemon.mod.common.client.gui.battle.widgets.BattleOptionTile;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.reyaan.wherearemytms.fabric.WhereAreMyTMs;
import dev.reyaan.wherearemytms.fabric.WhereAreMyTMsClient;
import dev.reyaan.wherearemytms.fabric.item.BasePokemonTM;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static dev.reyaan.wherearemytms.fabric.WhereAreMyTMs.TE_DRIVE_CLOSE_PACKET_ID;

public class TEDriveScreenHandler extends ScreenHandler {
    public TEDriveScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(WhereAreMyTMs.TE_DRIVE_SCREEN_HANDLER, syncId);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }


    @Override
    public void close(PlayerEntity player) {
//        if (this.selectedMove != null) {
//            ItemStack stack = player.getStackInHand(player.getActiveHand());
//            if (stack.getItem() instanceof BasePokemonTM tm) {
//                NbtCompound nbtCompound = stack.getOrCreateNbt();
//
//                nbtCompound.putString("move", this.selectedMove.getName());
//                ElementalType type = this.selectedMove.getElementalType();
//                int hue = type.getHue();
//                nbtCompound.putString("type", type.getDisplayName().getString());
//                nbtCompound.putInt("hue", hue);
//
//                // Update
//                stack.setNbt(nbtCompound);
//                stack.setCustomName(Text.translatable(tm.title, this.selectedMove.getDisplayName())
//                        .setStyle(Style.EMPTY
//                                .withColor(hue)
//                                .withItalic(false)));
//                stack.setNbt(nbtCompound);
//                player.setStackInHand(player.getActiveHand(), stack);
//            }
//        }
        super.close(player);
    }
}