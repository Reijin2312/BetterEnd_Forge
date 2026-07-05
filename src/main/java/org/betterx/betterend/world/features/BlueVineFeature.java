package org.betterx.betterend.world.features;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.blocks.basis.EndPlantWithAgeBlock;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class BlueVineFeature extends ScatterFeature<ScatterFeatureConfig> {
    private static final ThreadLocal<Boolean> SMALL = ThreadLocal.withInitial(() -> false);

    public BlueVineFeature() {
        super(ScatterFeatureConfig.CODEC);
    }

    @Override
    protected void clearPlacementCache() {
        SMALL.remove();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canGenerate(
            ScatterFeatureConfig cfg,
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
        SMALL.set(d > 0.5F);
        boolean canSurvive = EndBlocks.BLUE_VINE_SEED.canSurvive(DefaultFeature.AIR, world, blockPos);
        if (!canSurvive) {
            SMALL.remove();
        }
        return canSurvive;
    }

    @Override
    public void generate(ScatterFeatureConfig cfg, WorldGenLevel world, RandomSource random, BlockPos blockPos) {
        boolean small = SMALL.get();
        clearPlacementCache();
        if (small) {
            BlocksHelper.setWithoutUpdate(
                    world,
                    blockPos,
                    EndBlocks.BLUE_VINE_SEED.defaultBlockState().setValue(EndPlantWithAgeBlock.AGE, random.nextInt(4))
            );
        } else {
            EndPlantWithAgeBlock seed = ((EndPlantWithAgeBlock) EndBlocks.BLUE_VINE_SEED);
            seed.growAdult(world, random, blockPos);
        }
    }
}
