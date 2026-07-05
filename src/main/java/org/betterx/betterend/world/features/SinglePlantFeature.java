package org.betterx.betterend.world.features;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.blocks.BaseCropBlock;
import org.betterx.bclib.blocks.BaseDoublePlantBlock;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.betterend.blocks.basis.EndPlantWithAgeBlock;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class SinglePlantFeature extends ScatterFeature<SinglePlantFeatureConfig> {
    private static final ThreadLocal<BlockState> PLANT = new ThreadLocal<>();

    public SinglePlantFeature() {
        super(SinglePlantFeatureConfig.CODEC);
    }

    @Override
    protected void clearPlacementCache() {
        PLANT.remove();
    }

    @Override
    protected BlockPos getCenterGround(SinglePlantFeatureConfig cfg, WorldGenLevel world, BlockPos pos) {
        if (world.isEmptyBlock(pos) && world.getBlockState(pos.below()).is(CommonBlockTags.END_STONES)) {
            return pos;
        }
        return cfg.rawHeightmap
                ? DefaultFeature.getPosOnSurfaceWG(world, pos)
                : DefaultFeature.getPosOnSurface(world, pos);
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
        BlockState plant = cfg.getPlantState(random, blockPos);
        PLANT.set(plant);
        //noinspection deprecation
        boolean canSurvive = plant.getBlock().canSurvive(plant, world, blockPos);
        if (!canSurvive) {
            clearPlacementCache();
        }
        return canSurvive;
    }

    @Override
    public void generate(SinglePlantFeatureConfig cfg, WorldGenLevel world, RandomSource random, BlockPos blockPos) {
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
        } else if (plant.getBlock() instanceof BaseCropBlock && plant.hasProperty(BaseCropBlock.AGE)) {
            BlockState state = plant.setValue(BaseCropBlock.AGE, 3);
            BlocksHelper.setWithoutUpdate(world, blockPos, state);
        } else if (plant.getBlock() instanceof EndPlantWithAgeBlock && plant.hasProperty(EndPlantWithAgeBlock.AGE)) {
            int age = random.nextInt(4);
            BlockState state = plant.setValue(EndPlantWithAgeBlock.AGE, age);
            BlocksHelper.setWithoutUpdate(world, blockPos, state);
        } else {
            BlocksHelper.setWithoutUpdate(world, blockPos, plant);
        }
    }
}
