package dev.reyaan.wherearemytms.fabric.block;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.reyaan.wherearemytms.fabric.item.BasePokemonTM;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static dev.reyaan.wherearemytms.fabric.item.BasePokemonTM.isTMBlank;

public class TEDriveBlock extends HorizontalFacingBlock implements BlockEntityProvider, NamedScreenHandlerFactory {
    public TEDriveBlock(Settings settings) {
        super(settings);
    }


    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Hand activeHand = player.getActiveHand();
        if (hand == activeHand) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            ItemStack stack = player.getStackInHand(activeHand);
            if (blockEntity instanceof TEDriveBlockEntity && stack.getItem() instanceof BasePokemonTM) {
                if (isTMBlank(stack)) {
                    player.openHandledScreen(this);
                    return ActionResult.CONSUME;
                }
            }
        }
        return ActionResult.PASS;
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TEDriveBlockEntity(pos, state);
    }

    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TEDriveScreenHandler(syncId, playerInventory);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("title.wherearemytms.te_drive").formatted(Formatting.GRAY);
    }
}
