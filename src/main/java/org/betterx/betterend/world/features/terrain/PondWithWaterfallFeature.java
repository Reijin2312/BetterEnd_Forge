package org.betterx.betterend.world.features.terrain;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import org.betterx.betterend.noise.OpenSimplexNoise;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

/** Carves a source-water pond and a real flowing outlet into a generated small island. */
public class PondWithWaterfallFeature extends DefaultFeature {
    private static final BlockState END_STONE = Blocks.END_STONE.defaultBlockState();
    private static final BlockState END_MOSS = EndBlocks.END_MOSS.defaultBlockState();
    private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(6114);
    private static final int MIN_POND = 3;
    private static final int MAX_POND = 10;
    private static final int DRY_RIM = 3;
    private static final int MAX_PLATEAU_PROBE = 16;
    private static final int PLATEAU_FLAT_TOLERANCE = 2;
    private static final int MAX_FLATNESS_VARIANCE = 3;
    private static final int MIN_DEPTH = 4;
    private static final int MAX_DEPTH = 7;
    private static final int FALLBACK_DEPTH = 2;

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        int centerX = origin.getX() + 8;
        int centerZ = origin.getZ() + 8;
        int topY = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, centerX, centerZ) - 1;
        if (topY <= world.getMinBuildHeight() + 5) return false;

        int minHeight = topY;
        int maxHeight = topY;
        int[][] offsets = {{4, 0}, {-4, 0}, {0, 4}, {0, -4}};
        for (int[] offset : offsets) {
            int height = world.getHeight(
                    Heightmap.Types.WORLD_SURFACE_WG,
                    centerX + offset[0],
                    centerZ + offset[1]
            ) - 1;
            minHeight = Math.min(minHeight, height);
            maxHeight = Math.max(maxHeight, height);
        }
        if (maxHeight - minHeight > MAX_FLATNESS_VARIANCE) return false;

        int radius = Math.min(MAX_POND, probeFlatPlateau(world, centerX, centerZ, topY) - DRY_RIM);
        if (radius < MIN_POND) return false;
        int requestedDepth = MHelper.randRange(MIN_DEPTH, MAX_DEPTH, random);
        int allowedDepth = probeSolidThickness(world, centerX, topY, centerZ) - 3;
        int depth = allowedDepth >= requestedDepth
                ? requestedDepth
                : allowedDepth >= MIN_DEPTH ? allowedDepth : FALLBACK_DEPTH;
        int waterLevel = topY - 1;
        MutableBlockPos pos = new MutableBlockPos();

        for (int dx = -radius; dx <= radius; dx++) {
            int x = centerX + dx;
            for (int dz = -radius; dz <= radius; dz++) {
                int z = centerZ + dz;
                double distance = Math.sqrt(dx * dx + dz * dz);
                double edge = radius + NOISE.eval(x * 0.2, z * 0.2) * 1.5;
                if (distance > edge) continue;
                int columnTop = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
                if (Math.abs(columnTop - topY) > PLATEAU_FLAT_TOLERANCE) continue;
                int localDepth = (int) Math.round(depth * (edge - distance) / edge);
                if (localDepth < 1) continue;
                int floorY = topY - localDepth;
                BlocksHelper.setWithoutUpdate(
                        world,
                        pos.set(x, floorY, z),
                        localDepth == 1 ? END_MOSS : END_STONE
                );
                BlocksHelper.setWithoutUpdate(world, pos.set(x, floorY - 1, z), END_STONE);
                for (int y = floorY + 1; y <= waterLevel; y++) {
                    BlockPos waterPos = new BlockPos(x, y, z);
                    BlocksHelper.setWithoutUpdate(world, waterPos, WATER);
                    world.getChunk(x >> 4, z >> 4).markPosForPostprocessing(waterPos);
                    world.scheduleTick(waterPos, Fluids.WATER, 0);
                }
                for (int y = waterLevel + 1; y <= topY; y++) {
                    BlocksHelper.setWithoutUpdate(world, pos.set(x, y, z), AIR);
                }
            }
        }

        Direction[] directions = shuffledHorizontals(random);
        int waterfalls = 1 + random.nextInt(2);
        int made = 0;
        for (Direction direction : directions) {
            if (made >= waterfalls) break;
            if (spillWaterfall(world, direction, centerX, centerZ, radius, waterLevel, topY)) made++;
        }
        return true;
    }

    private int probeFlatPlateau(WorldGenLevel world, int centerX, int centerZ, int topY) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int radius = 1; radius <= MAX_PLATEAU_PROBE; radius++) {
            for (int[] direction : directions) {
                int height = world.getHeight(
                        Heightmap.Types.WORLD_SURFACE_WG,
                        centerX + direction[0] * radius,
                        centerZ + direction[1] * radius
                ) - 1;
                if (Math.abs(height - topY) > PLATEAU_FLAT_TOLERANCE) return radius - 1;
            }
        }
        return MAX_PLATEAU_PROBE;
    }

    private int probeSolidThickness(WorldGenLevel world, int x, int topY, int z) {
        MutableBlockPos pos = new MutableBlockPos();
        int count = 0;
        for (int offset = 0; offset < 24; offset++) {
            int y = topY - offset;
            if (y <= world.getMinBuildHeight()) break;
            BlockState state = world.getBlockState(pos.set(x, y, z));
            if (state.isAir() || !state.getFluidState().isEmpty()) break;
            count++;
        }
        return count;
    }

    private boolean spillWaterfall(
            WorldGenLevel world,
            Direction direction,
            int centerX,
            int centerZ,
            int radius,
            int waterLevel,
            int topY
    ) {
        MutableBlockPos pos = new MutableBlockPos();
        boolean carved = false;
        for (int distance = Math.max(1, radius - 2); distance <= radius + DRY_RIM + 3; distance++) {
            int x = centerX + direction.getStepX() * distance;
            int z = centerZ + direction.getStepZ() * distance;
            for (int y = waterLevel - 1; y <= topY + 1; y++) {
                BlockState state = world.getBlockState(pos.set(x, y, z));
                if (state.isAir() || !state.getFluidState().isEmpty()) continue;
                BlocksHelper.setWithoutUpdate(world, pos, AIR);
                carved = true;
            }
        }
        return carved;
    }

    private static Direction[] shuffledHorizontals(RandomSource random) {
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        for (int i = directions.length - 1; i > 0; i--) {
            int swap = random.nextInt(i + 1);
            Direction value = directions[i];
            directions[i] = directions[swap];
            directions[swap] = value;
        }
        return directions;
    }
}
