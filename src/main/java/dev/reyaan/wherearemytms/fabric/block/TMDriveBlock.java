package dev.reyaan.wherearemytms.fabric.block;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.reyaan.wherearemytms.fabric.item.BasePokemonTM;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TMDriveBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public TMDriveBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity user, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            // User checks (null in-case of cross-compat)
            if (user != null && hand == user.getActiveHand()) {

                // For now, checking if user is holding TM on interaction
                ItemStack stack = user.getStackInHand(hand);
                if (stack.getItem() instanceof BasePokemonTM) {
                    System.out.println("Hand + Item pass");

                    // Get Pokemon from slot 0
                    Pokemon pokemon = Cobblemon.INSTANCE.getStorage().getParty((ServerPlayerEntity) user).get(0);
                    if (pokemon != null) {
                        System.out.println("Mon exists");

                        // Get move from slot 0
                        Move move = pokemon.getMoveSet().get(0);
                        if (move != null) {
                            // Add template data to TM
                            System.out.println("Move exists");
                            NbtCompound nbtCompound = stack.getOrCreateNbt();
                            nbtCompound.putString("template", move.getName());
                            System.out.println("NBT exists" + nbtCompound + " -> " + move.getName());
                            nbtCompound.putString("type", move.getType().getName());

                            // Update
                            stack.setNbt(nbtCompound);
                            user.setStackInHand(hand, stack);
                        }
                    }
                }
            }


            return super.onUse(state, world, pos, user, hand, hit);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TMDriveBlockEntity(pos, state);
    }
}
