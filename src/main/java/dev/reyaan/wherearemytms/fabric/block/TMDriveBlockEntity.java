package dev.reyaan.wherearemytms.fabric.block;


import dev.reyaan.wherearemytms.fabric.WhereAreMyTMs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TMDriveBlockEntity extends BlockEntity {
    public TMDriveBlockEntity(BlockPos pos, BlockState state) {
        super(WhereAreMyTMs.TM_DRIVE_BLOCK_ENTITY, pos, state);
    }
}