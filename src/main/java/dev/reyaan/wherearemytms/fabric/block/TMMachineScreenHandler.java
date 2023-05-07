package dev.reyaan.wherearemytms.fabric.block;


import dev.reyaan.wherearemytms.fabric.WhereAreMyTMs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class TMMachineScreenHandler extends ScreenHandler {
    public TMMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(WhereAreMyTMs.TM_MACHINE_SCREEN_HANDLER, syncId);
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
        super.close(player);
    }
}