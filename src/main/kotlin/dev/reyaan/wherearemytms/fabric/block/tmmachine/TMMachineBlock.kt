package dev.reyaan.wherearemytms.fabric.block.tmmachine


import com.cobblemon.mod.common.client.CobblemonClient
import dev.reyaan.wherearemytms.fabric.client.screen.TMMachineScreen
import dev.reyaan.wherearemytms.fabric.item.MoveTransferItem
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView


class TMMachineBlock(settings: Settings): HorizontalFacingBlock(settings), BlockEntityProvider {
    init {
        defaultState = defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(HALF, DoubleBlockHalf.LOWER)
    }

    companion object {
        val HALF: EnumProperty<DoubleBlockHalf> = Properties.DOUBLE_BLOCK_HALF

        val NORTH_BOX_BOTTOM: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.125, 1.0, 1.0, 1.0)
        )

        val NORTH_BOX_TOP: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.125, 1.0, 0.375, 1.0)
        )

        val EAST_BOX_BOTTOM: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.875, 1.0, 1.0)
        )

        val EAST_BOX_TOP: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.875, 0.375, 1.0)
        )

        val SOUTH_BOX_BOTTOM: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 0.875)
        )

        val SOUTH_BOX_TOP: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.375, 0.875)
        )

        val WEST_BOX_BOTTOM: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.125, 0.0, 0.0, 1.0, 1.0, 1.0)
        )

        val WEST_BOX_TOP: VoxelShape = VoxelShapes.union(
            VoxelShapes.cuboid(0.125, 0.0, 0.0, 1.0, 0.375, 1.0)
        )
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING).add(HALF)
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity?) {
        super.onBreak(world, pos, state, player)
        val otherPart = world.getBlockState(getPositionOfOtherPart(state, pos))
        if (otherPart.block is TMMachineBlock) {
            world.setBlockState(getPositionOfOtherPart(state, pos), Blocks.AIR.defaultState, 35)
            world.syncWorldEvent(player, 2001, getPositionOfOtherPart(state, pos), Block.getRawIdFromState(otherPart))
        }
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack?) {
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3)
        world.updateNeighbors(pos, Blocks.AIR)
        state.updateNeighbors(world, pos, 3)
    }


    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val abovePosition = ctx.blockPos.up()
        val world = ctx.world
        if (world.getBlockState(abovePosition).canReplace(ctx) && !world.isOutOfHeightLimit(abovePosition)) {
            return defaultState
                .with(FACING, ctx.playerFacing)
                .with(HALF, DoubleBlockHalf.LOWER)
        }

        return null
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
            val basePos = getBasePosition(state, pos)

            world.getBlockEntity(basePos.up())?.markRemoved()

            val blockEntity = world.getBlockEntity(basePos)
            val stack = player.getStackInHand(activeHand)
            if (blockEntity is TMMachineBlockEntity) {
                if (stack.item is MoveTransferItem) {
                    if (!stack.orCreateNbt.contains("move")) {
                        player.sendMessage(
                            Text.translatable("response.wherearemytms.bad_move_caution")
                                .formatted(Formatting.RED)
                        )

                        MinecraftClient.getInstance().setScreen(TMMachineScreen(CobblemonClient.storage.myParty.slots))
                        return ActionResult.CONSUME
                    }
                } else {
                    player.sendMessage(
                        Text.translatable("response.wherearemytms.get_a_bloody_tm_first")
                            .formatted(Formatting.RED)
                    )
                }
            }
        }
        return ActionResult.PASS
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = if (state.get(HALF) == DoubleBlockHalf.LOWER) TMMachineBlockEntity(pos, state) else null


    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape {
        return if (state.get(HALF) == DoubleBlockHalf.LOWER)  {
            // its flipped and i cant be arsed
            when (state.get(FACING)) {
                Direction.SOUTH -> NORTH_BOX_BOTTOM
                Direction.WEST -> EAST_BOX_BOTTOM
                Direction.EAST -> WEST_BOX_BOTTOM
                else -> SOUTH_BOX_BOTTOM
            }
        } else {
            when (state.get(FACING)) {
                Direction.SOUTH -> NORTH_BOX_TOP
                Direction.WEST -> EAST_BOX_TOP
                Direction.EAST -> WEST_BOX_TOP
                else -> SOUTH_BOX_TOP
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val blockPos = pos.down()
        val blockState = world.getBlockState(blockPos)
        return if (state.get(HALF) == DoubleBlockHalf.LOWER) blockState.isSideSolidFullSquare(world, blockPos, Direction.UP) else blockState.isOf(this)
    }

    fun getPositionOfOtherPart(state: BlockState, pos: BlockPos): BlockPos {
        return if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            pos.up()
        } else {
            pos.down()
        }
    }

    fun getBasePosition(state: BlockState, pos: BlockPos): BlockPos {
        return if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            pos
        } else {
            pos.down()
        }
    }

}

