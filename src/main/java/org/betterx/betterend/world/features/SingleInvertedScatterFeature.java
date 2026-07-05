package org.betterx.betterend.world.features;

import org.betterx.bclib.blocks.BaseAttachedBlock;
import org.betterx.bclib.util.BlocksHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SingleInvertedScatterFeature extends InvertedScatterFeature<SinglePlantFeatureConfig> {
    private static final ThreadLocal<BlockState> BLOCK = new ThreadLocal<>();

    public SingleInvertedScatterFeature() {
        super(SinglePlantFeatureConfig.CODEC);
    }

    @Override
    public boolean canGenerate(
            SinglePlantFeatureConfig cfg,
            WorldGenLevel world,
            RandomSource random,
            BlockPos center,
            BlockPos blockPos,
            float radius
    ) {
        if (!world.isEmptyBlock(blockPos)) {
            BLOCK.remove();
            return false;
        }
        BlockState block = cfg.getPlantState(random, blockPos);
        BlockState state = block;
        if (block.getBlock() instanceof BaseAttachedBlock && state.hasProperty(BlockStateProperties.FACING)) {
            state = state.setValue(BlockStateProperties.FACING, Direction.DOWN);
        }
        BLOCK.set(block);
        boolean canSurvive = state.canSurvive(world, blockPos);
        if (!canSurvive) {
            BLOCK.remove();
        }
        return canSurvive;
    }

    @Override
    public void generate(SinglePlantFeatureConfig cfg, WorldGenLevel world, RandomSource random, BlockPos blockPos) {
        BlockState block = BLOCK.get();
        BLOCK.remove();
        if (block == null) {
            return;
        }

        BlockState state = block;
        if (block.getBlock() instanceof BaseAttachedBlock && state.hasProperty(BlockStateProperties.FACING)) {
            state = state.setValue(BlockStateProperties.FACING, Direction.DOWN);
        }
        BlocksHelper.setWithoutUpdate(world, blockPos, state);
    }
}
