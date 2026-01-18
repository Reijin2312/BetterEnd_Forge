package org.betterx.betterend.blocks;

import org.betterx.bclib.behaviours.interfaces.BehaviourWaterPlantSeed;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.blocks.UnderwaterPlantWithAgeBlock;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class EndLilySeedBlock extends UnderwaterPlantWithAgeBlock implements BehaviourWaterPlantSeed {
    @Override
    public void grow(WorldGenLevel world, RandomSource random, BlockPos pos) {
        if (canGrow(world, pos)) {
            BlocksHelper.setWithoutUpdate(
                    world,
                    pos,
                    EndBlocks.END_LILY.defaultBlockState().setValue(EndLilyBlock.SHAPE, TripleShape.BOTTOM)
            );
            BlockPos up = pos.above();
            while (world.getFluidState(up).isSource()) {
                BlocksHelper.setWithoutUpdate(
                        world,
                        up,
                        EndBlocks.END_LILY.defaultBlockState().setValue(EndLilyBlock.SHAPE, TripleShape.MIDDLE)
                );
                up = up.above();
            }
            BlocksHelper.setWithoutUpdate(
                    world,
                    up,
                    EndBlocks.END_LILY.defaultBlockState().setValue(EndLilyBlock.SHAPE, TripleShape.TOP)
            );
        }
    }

    private boolean canGrow(WorldGenLevel world, BlockPos pos) {
        BlockPos up = pos.above();
        while (world.getBlockState(up).getFluidState().getType().equals(Fluids.WATER.getSource())) {
            up = up.above();
        }
        return world.isEmptyBlock(up);
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(CommonBlockTags.END_STONES);
    }
}
