package org.betterx.betterend.world.features;

import org.betterx.bclib.blocks.BaseDoublePlantBlock;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class DoublePlantFeature extends ScatterFeature<DoublePlantFeatureConfig> {
    private static final ThreadLocal<BlockState> PLANT = new ThreadLocal<>();

    public DoublePlantFeature() {
        super(DoublePlantFeatureConfig.CODEC);

    }

    @Override
    protected void clearPlacementCache() {
        PLANT.remove();
    }

    @Override
    public boolean canGenerate(
            DoublePlantFeatureConfig cfg,
            WorldGenLevel world,
            RandomSource random,
            BlockPos center,
            BlockPos blockPos,
            float radius
    ) {
        float d = MHelper.length(
                center.getX() - blockPos.getX(),
                center.getZ() - blockPos.getZ()
        ) / radius * 0.6F + random.nextFloat() * 0.4F;
        BlockState plant = d < 0.5F ? cfg.getLargePlantState(random, blockPos) : cfg.getSmallPlantState(random, blockPos);
        PLANT.set(plant);
        //noinspection deprecation
        boolean canSurvive = plant.getBlock().canSurvive(plant, world, blockPos);
        if (!canSurvive) {
            clearPlacementCache();
        }
        return canSurvive;
    }

    @Override
    public void generate(
            DoublePlantFeatureConfig cfg,
            WorldGenLevel world,
            RandomSource random,
            BlockPos blockPos
    ) {
        BlockState plant = PLANT.get();
        clearPlacementCache();
        if (plant == null) {
            return;
        }

        if (plant.getBlock() instanceof BaseDoublePlantBlock
                && plant.hasProperty(BaseDoublePlantBlock.ROTATION)
                && plant.hasProperty(BaseDoublePlantBlock.TOP)) {
            int rot = random.nextInt(4);
            BlockState state = plant.setValue(BaseDoublePlantBlock.ROTATION, rot);
            BlocksHelper.setWithoutUpdate(world, blockPos, state);
            BlocksHelper.setWithoutUpdate(world, blockPos.above(), state.setValue(BaseDoublePlantBlock.TOP, true));
        } else {
            BlocksHelper.setWithoutUpdate(world, blockPos, plant);
        }
    }
}
