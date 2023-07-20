package dev.reyaan.wherearemytms.fabric.block.tmmachine


import com.cobblemon.mod.common.client.CobblemonClient
import dev.reyaan.wherearemytms.fabric.client.screen.TMMachineScreen
import dev.reyaan.wherearemytms.fabric.item.MoveTransferItem
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.network.listener.ServerPlayPacketListener
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World


class TMMachineBlock(settings: Settings): HorizontalFacingBlock(settings), BlockEntityProvider {
    init {
        defaultState = defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    companion object {
        val NORTH_BOX: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.125, 1.0, 1.375, 1.0)
        )

        val EAST_BOX: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.875, 1.375, 1.0)
        )

        val SOUTH_BOX: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.375, 0.875)
        )

        val WEST_BOX: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.125, 0.0, 0.0, 1.0, 1.375, 1.0)
        )
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return super.getPlacementState(ctx)!!.with(Properties.HORIZONTAL_FACING, ctx.playerFacing.opposite)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult,
    ): ActionResult {
        val activeHand = player.activeHand
        if (world.isClient && hand == activeHand) {
            val blockEntity = world.getBlockEntity(pos)
            val stack = player.getStackInHand(activeHand)
            if (blockEntity is TMMachineBlockEntity && stack.item is MoveTransferItem) {
                if (MoveTransferItem.isBlank(stack)) {
                    MinecraftClient.getInstance().setScreen(TMMachineScreen(CobblemonClient.storage.myParty.slots))
                    return ActionResult.CONSUME
                }
            }
        }
        return ActionResult.PASS
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return TMMachineBlockEntity(pos, state)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape {
        return when (state.get(Properties.HORIZONTAL_FACING)) {
            Direction.NORTH -> NORTH_BOX
            Direction.SOUTH -> SOUTH_BOX
            Direction.WEST -> WEST_BOX
            Direction.EAST -> EAST_BOX
            else -> NORTH_BOX
        }
    }
}

