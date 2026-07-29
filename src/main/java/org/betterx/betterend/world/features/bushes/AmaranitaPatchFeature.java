package org.betterx.betterend.world.features.bushes;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.blocks.BaseAttachedBlock;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/** Places clusters of small, fully shaped amaranita mushrooms. */
public class AmaranitaPatchFeature extends DefaultFeature {
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource random = context.random();
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        boolean placed = false;
        int count = MHelper.randRange(4, 8, random);
        for (int i = 0; i < count; i++) {
            int x = MHelper.randRange(-4, 4, random);
            int z = MHelper.randRange(-4, 4, random);
            if (x * x + z * z > 16) continue;
            BlockPos surface = getPosOnSurfaceWG(world, origin.offset(x, 0, z));
            if (isGround(world.getBlockState(surface.below()))
                    && world.getBlockState(surface).canBeReplaced()
                    && growMushroom(world, surface, MHelper.randRange(2, 6, random), random)) {
                placed = true;
            }
        }
        return placed;
    }

    private static boolean isGround(BlockState state) {
        return state.is(BlockTags.NYLIUM) || state.is(EndBlocks.END_MOSS) || state.is(CommonBlockTags.END_STONES);
    }

    private static boolean growMushroom(WorldGenLevel world, BlockPos base, int height, RandomSource random) {
        MutableBlockPos pos = new MutableBlockPos();
        for (int y = 0; y <= height; y++) {
            if (!world.getBlockState(pos.set(base).move(0, y, 0)).canBeReplaced()) return false;
        }
        for (int y = 0; y < height; y++) {
            BlockState stem = y == 0 || y == height - 1
                    ? EndBlocks.AMARANITA_HYPHAE.defaultBlockState()
                    : EndBlocks.AMARANITA_STEM.defaultBlockState();
            BlocksHelper.setWithoutUpdate(world, pos.set(base).move(0, y, 0), stem);
        }

        BlockPos cap = base.above(height);
        boolean glowing = random.nextInt(3) == 0;
        Direction lanternSide = BlocksHelper.HORIZONTAL[random.nextInt(4)];
        for (Direction direction : BlocksHelper.HORIZONTAL) {
            pos.set(cap).move(Direction.DOWN).move(direction);
            if (!world.getBlockState(pos).canBeReplaced()) continue;
            if (glowing && direction == lanternSide) {
                BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.AMARANITA_LANTERN);
                pos.move(Direction.DOWN);
                if (world.getBlockState(pos).canBeReplaced()) {
                    BlocksHelper.setWithoutUpdate(
                            world,
                            pos,
                            EndBlocks.AMARANITA_FUR.defaultBlockState().setValue(BaseAttachedBlock.FACING, Direction.DOWN)
                    );
                }
            } else {
                BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.AMARANITA_HYMENOPHORE);
            }
        }
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                pos.set(cap).move(x, 0, z);
                if (world.getBlockState(pos).canBeReplaced()) {
                    BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.AMARANITA_CAP);
                }
            }
        }
        for (Direction direction : BlocksHelper.HORIZONTAL) {
            pos.set(cap).move(Direction.UP).move(direction);
            if (world.getBlockState(pos).canBeReplaced()) {
                BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.AMARANITA_CAP);
            }
        }
        pos.set(cap).move(Direction.UP);
        if (world.getBlockState(pos).canBeReplaced()) {
            BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.AMARANITA_CAP);
        }
        return true;
    }
}
