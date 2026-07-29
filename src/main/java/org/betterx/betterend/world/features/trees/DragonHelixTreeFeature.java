package org.betterx.betterend.world.features.trees;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.blocks.BaseVineBlock;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;

/** A compact dragon-wood pine with a spiralling lucernia canopy and hanging bulb lights. */
public class DragonHelixTreeFeature extends DefaultFeature {
    private static final float LIGHT_CHANCE = 0.6F;
    private static final double HELIX_TURN = 1.15;
    private static final double HELIX_REACH = 1.8;

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var random = context.random();
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        BlockState below = world.getBlockState(pos.below());
        if (!below.is(BlockTags.NYLIUM) && !below.is(EndBlocks.END_MOSS) && !below.is(CommonBlockTags.END_STONES)) {
            return false;
        }

        BlockState log = EndBlocks.DRAGON_TREE.getLog().defaultBlockState();
        BlockState leaf = EndBlocks.LUCERNIA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
        int height = MHelper.randRange(11, 17, random);
        int maxRadius = MHelper.randRange(3, 4, random);
        int foliageStart = MHelper.randRange(2, 3, random);
        int topY = pos.getY() + height;
        double helixPhase = random.nextDouble() * Math.PI * 2;
        MutableBlockPos mutable = new MutableBlockPos();

        for (int i = 0; i <= height; i++) {
            mutable.set(pos.getX(), pos.getY() + i, pos.getZ());
            if (world.getBlockState(mutable).canBeReplaced()) {
                BlocksHelper.setWithoutUpdate(world, mutable, log);
            }
        }

        List<BlockPos> skirt = new ArrayList<>();
        int lowestRingY = pos.getY() + foliageStart;
        for (int y = lowestRingY; y <= topY; y++) {
            int radius = Math.min(maxRadius, (topY - y + 1) / 2);
            double helixAngle = helixPhase + (y - lowestRingY) * HELIX_TURN;
            placeLeafRing(world, pos, y, radius, helixAngle, leaf, y == lowestRingY ? skirt : null);
        }
        for (int y = 1; y <= 2; y++) {
            mutable.set(pos.getX(), topY + y, pos.getZ());
            if (world.getBlockState(mutable).canBeReplaced()) {
                BlocksHelper.setWithoutUpdate(world, mutable, leaf);
            }
        }

        for (BlockPos leafPos : skirt) {
            if (random.nextFloat() < LIGHT_CHANCE && world.getBlockState(leafPos.below()).canBeReplaced()) {
                hangBulbVine(world, leafPos, MHelper.randRange(1, 3, random));
            }
        }
        return true;
    }

    private void placeLeafRing(
            WorldGenLevel world,
            BlockPos center,
            int y,
            int baseRadius,
            double helixAngle,
            BlockState leaf,
            List<BlockPos> rim
    ) {
        MutableBlockPos pos = new MutableBlockPos();
        int scan = baseRadius + (int) Math.ceil(HELIX_REACH);
        for (int dx = -scan; dx <= scan; dx++) {
            for (int dz = -scan; dz <= scan; dz++) {
                if (dx == 0 && dz == 0) continue;
                int distance = dx * dx + dz * dz;
                double alignment = Math.max(0, Math.cos(Math.atan2(dz, dx) - helixAngle));
                double effectiveRadius = baseRadius + alignment * HELIX_REACH;
                if (distance > effectiveRadius * effectiveRadius) continue;
                pos.set(center.getX() + dx, y, center.getZ() + dz);
                if (world.getBlockState(pos).canBeReplaced()) {
                    BlocksHelper.setWithoutUpdate(world, pos, leaf);
                }
                if (rim != null && distance >= (effectiveRadius - 1) * (effectiveRadius - 1)) {
                    rim.add(pos.immutable());
                }
            }
        }
    }

    private void hangBulbVine(WorldGenLevel world, BlockPos leafPos, int length) {
        MutableBlockPos pos = new MutableBlockPos();
        for (int part = 1; part <= length; part++) {
            pos.set(leafPos).move(0, -part, 0);
            if (!world.getBlockState(pos).canBeReplaced()) break;
            TripleShape shape = part == length ? TripleShape.BOTTOM : part == 1 ? TripleShape.TOP : TripleShape.MIDDLE;
            BlocksHelper.setWithoutUpdate(
                    world,
                    pos,
                    EndBlocks.BULB_VINE.defaultBlockState().setValue(BaseVineBlock.SHAPE, shape)
            );
        }
    }
}
