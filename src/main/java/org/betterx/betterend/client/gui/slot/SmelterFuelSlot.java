package org.betterx.betterend.client.gui.slot;

import org.betterx.betterend.client.gui.EndStoneSmelterMenu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SmelterFuelSlot extends Slot {

    private final EndStoneSmelterMenu handler;

    public SmelterFuelSlot(EndStoneSmelterMenu handler, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.handler = handler;
    }

    public boolean mayPlace(ItemStack stack) {
        return this.handler.isFuel(stack) || FurnaceFuelSlot.isBucket(stack);
    }

    public int getMaxStackSize(ItemStack stack) {
        return FurnaceFuelSlot.isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }
}
