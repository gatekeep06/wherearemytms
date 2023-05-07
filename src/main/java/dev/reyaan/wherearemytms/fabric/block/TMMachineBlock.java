package dev.reyaan.wherearemytms.fabric.block;

import com.cobblemon.mod.common.Cobblemon;
import dev.reyaan.wherearemytms.fabric.item.BasePokemonTM;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static dev.reyaan.wherearemytms.fabric.item.BasePokemonTM.isTMBlank;

public class TMMachineBlock extends HorizontalFacingBlock implements BlockEntityProvider, NamedScreenHandlerFactory {

    public TMMachineBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return Objects.requireNonNull(super.getPlacementState(ctx)).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Hand activeHand = player.getActiveHand();
        if (hand == activeHand) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            ItemStack stack = player.getStackInHand(activeHand);
            if (blockEntity instanceof TMMachineBlockEntity && stack.getItem() instanceof BasePokemonTM) {
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
        return new TMMachineBlockEntity(pos, state);
    }

    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TMMachineScreenHandler(syncId, playerInventory);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.wherearemytms.tm_machine").formatted(Formatting.GRAY);
    }
}
