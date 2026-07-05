package org.betterx.betterend.world.features;

import org.betterx.bclib.blocks.BaseVineBlock;
import org.betterx.bclib.blocks.BlockProperties;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.util.BlocksHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class VineFeature extends InvertedScatterFeature<VineFeatureConfig> {
    private static final ThreadLocal<BlockState> PLANT = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> VINE = ThreadLocal.withInitial(() -> false);

    public VineFeature() {
        super(VineFeatureConfig.CODEC);
    }

    @Override
    public boolean canGenerate(
            VineFeatureConfig cfg,
            WorldGenLevel world,
            RandomSource random,
            BlockPos center,
            BlockPos blockPos,
            float radius
    ) {
        BlockState plant = cfg.getPlantState(random, blockPos);
        PLANT.set(plant);

        BlockState state = world.getBlockState(blockPos);
        boolean canGenerate = state.canBeReplaced() && canPlaceBlock(plant, state, world, blockPos);
        if (!canGenerate) {
            clearCachedState();
        }
        return canGenerate;
    }

    @Override
    public void generate(VineFeatureConfig cfg, WorldGenLevel world, RandomSource random, BlockPos blockPos) {
        BlockState plant = PLANT.get();
        if (plant == null) {
            clearCachedState();
            return;
        }

        int h = BlocksHelper.downRay(world, blockPos, random.nextInt(cfg.maxLength)) - 1;
        if (h > 2) {
            BlockState top = getTopState(plant);
            BlockState middle = getMiddleState(plant);
            BlockState bottom = getBottomState(plant);
            BlocksHelper.setWithoutUpdate(world, blockPos, top);
            for (int i = 1; i < h; i++) {
                BlocksHelper.setWithoutUpdate(world, blockPos.below(i), middle);
            }
            BlocksHelper.setWithoutUpdate(world, blockPos.below(h), bottom);
        }
        clearCachedState();
    }

    private boolean canPlaceBlock(BlockState plant, BlockState state, WorldGenLevel world, BlockPos blockPos) {
        if (plant == null) return false;
        if (plant.getBlock() instanceof BaseVineBlock vineBlock) {
            VINE.set(true);
            return vineBlock.canGenerate(state, world, blockPos);
        } else {
            VINE.set(false);
            return plant.getBlock().canSurvive(state, world, blockPos);
        }
    }

    private BlockState getTopState(BlockState plant) {
        BlockState state = plant;
        return VINE.get() && state.hasProperty(BlockProperties.TRIPLE_SHAPE)
                ? state.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP)
                : state;
    }

    private BlockState getMiddleState(BlockState plant) {
        BlockState state = plant;
        return VINE.get() && state.hasProperty(BlockProperties.TRIPLE_SHAPE)
                ? state.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE)
                : state;
    }

    private BlockState getBottomState(BlockState plant) {
        BlockState state = plant;
        return VINE.get() && state.hasProperty(BlockProperties.TRIPLE_SHAPE)
                ? state.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM)
                : state;
    }

    private void clearCachedState() {
        PLANT.remove();
        VINE.remove();
    }
}
