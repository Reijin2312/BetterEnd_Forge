package org.betterx.betterend.blocks.basis;

import org.betterx.bclib.blocks.WallMushroomBlock;
import org.betterx.betterend.interfaces.survives.SurvivesOnEndStone;

import net.minecraft.world.level.block.state.BlockState;

public class EndWallMushroom extends WallMushroomBlock implements SurvivesOnEndStone {

    public EndWallMushroom(int light) {
        super(light);
    }

    @Override
    public boolean isTerrain(BlockState state) {
        return SurvivesOnEndStone.super.isTerrain(state);
    }
}
