package dev.reyaan.wherearemytms.fabric.block;


import dev.reyaan.wherearemytms.fabric.WhereAreMyTMs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;


public class TMMachineBlockEntity extends BlockEntity {
    public TMMachineBlockEntity(BlockPos pos, BlockState state) {
        super(WhereAreMyTMs.TM_MACHINE_BLOCK_ENTITY, pos, state);
    }
}