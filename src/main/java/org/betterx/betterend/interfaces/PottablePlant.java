package org.betterx.betterend.interfaces;

import net.minecraft.world.level.block.Block;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface PottablePlant {
    boolean canPlantOn(Block block);

    default boolean canBePotted() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    default String getPottedState() {
        return "";
    }
}

