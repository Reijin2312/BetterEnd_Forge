package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.BasePlantBlock;
import org.betterx.betterend.interfaces.PottablePlant;

import net.minecraft.world.level.block.Block;

public abstract class EndPlantBlock extends BasePlantBlock implements PottablePlant {
    protected EndPlantBlock(Properties props) {
        super(props);
    }


    @Override
    public boolean canPlantOn(Block block) {
        return isTerrain(block.defaultBlockState());
    }

    @Override
    public boolean canBePotted() {
        return getStateDefinition().getProperties().isEmpty();
    }
}
