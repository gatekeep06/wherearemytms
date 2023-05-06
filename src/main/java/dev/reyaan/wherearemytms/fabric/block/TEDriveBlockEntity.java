package dev.reyaan.wherearemytms.fabric.block;


import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import dev.reyaan.wherearemytms.fabric.WhereAreMyTMs;
import dev.reyaan.wherearemytms.fabric.item.BasePokemonTM;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import static dev.reyaan.wherearemytms.fabric.item.BasePokemonTM.isTMBlank;

public class TEDriveBlockEntity extends BlockEntity {
    public TEDriveBlockEntity(BlockPos pos, BlockState state) {
        super(WhereAreMyTMs.TE_DRIVE_BLOCK_ENTITY, pos, state);
    }
}