package org.betterx.betterend.world.features.terrain;

import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.blocks.BaseVineBlock;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.blocks.StalactiteBlock;
import org.betterx.bclib.sdf.PosInfo;
import org.betterx.bclib.sdf.SDF;
import org.betterx.bclib.sdf.operator.SDFCoordModify;
import org.betterx.bclib.sdf.operator.SDFRadialNoiseMap;
import org.betterx.bclib.sdf.operator.SDFScale3D;
import org.betterx.bclib.sdf.operator.SDFSmoothUnion;
import org.betterx.bclib.sdf.operator.SDFTranslate;
import org.betterx.bclib.sdf.primitive.SDFCappedCone;
import org.betterx.betterend.noise.OpenSimplexNoise;
import org.betterx.betterend.registry.EndBiomes;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.world.features.terrain.SmallEndIslandLayout.Geometry;
import org.betterx.betterend.world.features.terrain.SmallEndIslandLayout.Island;
import org.betterx.betterend.world.generator.TerrainGenerator;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.EnumSet;

/** Legacy feature-pipeline port of New Dawn's structure-based small End islands. */
public class SmallEndIslandFeature extends DefaultFeature {
    private static final BlockState END_STONE = Blocks.END_STONE.defaultBlockState();
    private static final BlockState END_MOSS = EndBlocks.END_MOSS.defaultBlockState();
    private static final BlockState SANGNUM = EndBlocks.SANGNUM.defaultBlockState();
    private static final BlockState PALLIDIUM_FULL = EndBlocks.PALLIDIUM_FULL.defaultBlockState();
    private static final BlockState PALLIDIUM_HEAVY = EndBlocks.PALLIDIUM_HEAVY.defaultBlockState();
    private static final BlockState PALLIDIUM_THIN = EndBlocks.PALLIDIUM_THIN.defaultBlockState();
    private static final BlockState PALLIDIUM_TINY = EndBlocks.PALLIDIUM_TINY.defaultBlockState();
    private static final BlockState UMBRALITH = EndBlocks.UMBRALITH.stone.defaultBlockState();
    private static final OpenSimplexNoise UNDERSIDE_PATCH = new OpenSimplexNoise(0x5A9C_1DE5L);
    private static final double PATCH_FREQ = 0.14;
    private static final double PATCH_THRESHOLD = 0.05;

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        Island baseIsland = SmallEndIslandLayout.forChunk(world.getSeed(), new ChunkPos(context.origin()));
        if (baseIsland == null) {
            return false;
        }

        BlockPos center = new BlockPos(baseIsland.centerX(), baseIsland.centerY(), baseIsland.centerZ());
        boolean flowerIslets = world.getBiome(center).is(EndBiomes.FLOWER_ISLETS);
        Island island = baseIsland.withVariant(flowerIslets);
        Geometry geometry = island.geometry();
        if (!canGenerate(island)) {
            return false;
        }

        RandomSource decorationRandom = RandomSource.create(island.geometrySeed() ^ 0x3C6E_F372_FE94_F82BL);
        getSDF(decorationRandom, flowerIslets, geometry).fillRecursive(world, center);
        primeHeightmaps(world, center, Mth.ceil(geometry.radius() * 1.35F));
        return true;
    }

    private static SDF getSDF(RandomSource random, boolean flowerIslets, Geometry geometry) {
        SDF warped = createShape(geometry);
        return flowerIslets
                ? warped.addPostProcess(info -> flowerCoat(info, random))
                : warped.addPostProcess(info -> waterfallCoat(info, random));
    }

    private static SDF createShape(Geometry geometry) {
        SDF cone1 = makeCone(0, 0.4F, 0.2F, -0.3F);
        SDF cone2 = makeCone(0.4F, 0.5F, 0.1F, -0.1F);
        SDF cone3 = makeCone(0.5F, 0.45F, 0.03F, 0.0F);
        SDF cone4 = makeCone(0.45F, 0, 0.02F, 0.03F);

        SDF coneBottom = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone1).setSourceB(cone2);
        SDF coneTop = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone3).setSourceB(cone4);
        SDF noise = new SDFRadialNoiseMap().setSeed(geometry.noiseSeed())
                                             .setRadius(0.5F)
                                             .setIntensity(geometry.noiseIntensity())
                                             .setSource(coneTop);
        SDF island = new SDFSmoothUnion().setRadius(0.01F).setSourceA(noise).setSourceB(coneBottom);
        SDF scaled = new SDFScale3D().setScale(
                geometry.scaleX(),
                geometry.scale(),
                geometry.scaleZ()
        ).setSource(island);

        OpenSimplexNoise warpX = new OpenSimplexNoise(geometry.noiseSeed() ^ 0x51AB_11E5L);
        OpenSimplexNoise warpZ = new OpenSimplexNoise(geometry.noiseSeed() ^ 0x0FF5_E7C0L);
        float warpAmplitude = geometry.radius() * 0.35F;
        return new SDFCoordModify().setFunction(pos -> {
            float wx = (float) warpX.eval(pos.x() * 0.22, pos.z() * 0.22) * warpAmplitude;
            float wz = (float) warpZ.eval(pos.x() * 0.22, pos.z() * 0.22) * warpAmplitude;
            pos.set(pos.x() + wx, pos.y(), pos.z() + wz);
        }).setSource(scaled);
    }

    public static boolean canGenerate(Island island) {
        int radius = Math.round(island.geometry().radius());
        return !overlapsNativeLand(island.centerX(), island.centerZ(), radius);
    }

    public static BlockPos anchorToward(Island source, Island target) {
        return anchorToward(source, target.centerX(), target.centerZ());
    }

    public static BlockPos anchorToward(Island source, int targetX, int targetZ) {
        SDF shape = createShape(source.geometry());
        double dx = targetX - source.centerX();
        double dz = targetZ - source.centerZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 1.0) {
            return new BlockPos(source.centerX(), source.centerY() + 1, source.centerZ());
        }
        double stepX = dx / length;
        double stepZ = dz / length;
        int maxDistance = Mth.ceil(source.geometry().radius() * 1.35F);
        int lastInside = 0;
        for (int distance = 1; distance <= maxDistance; distance++) {
            int localX = (int) Math.round(stepX * distance);
            int localZ = (int) Math.round(stepZ * distance);
            if (shape.getDistance(localX, 0, localZ) < 0) {
                lastInside = distance;
            } else if (lastInside > 0) {
                break;
            }
        }

        int anchorDistance = Math.max(0, lastInside - 2);
        int localX = (int) Math.round(stepX * anchorDistance);
        int localZ = (int) Math.round(stepZ * anchorDistance);
        int surfaceY = 0;
        for (int localY = 5; localY >= -5; localY--) {
            if (shape.getDistance(localX, localY, localZ) < 0) {
                surfaceY = localY;
                break;
            }
        }
        return new BlockPos(
                source.centerX() + localX,
                source.centerY() + surfaceY + 1,
                source.centerZ() + localZ
        );
    }

    private static BlockState waterfallCoat(PosInfo info, RandomSource random) {
        BlockState state = info.getState();
        if (!state.is(Blocks.END_STONE)) {
            return state;
        }
        if (info.getStateUp().isAir()) {
            return END_MOSS;
        }
        if (info.getStateDown().isAir() && inUndersidePatch(info.getPos(), random, 0.55F, 0.05F)) {
            hangStalactite(info, random);
        }
        return state;
    }

    private static BlockState flowerCoat(PosInfo info, RandomSource random) {
        BlockState state = info.getState();
        if (!state.is(Blocks.END_STONE)) {
            return state;
        }
        if (info.getStateUp().isAir()) {
            if (random.nextFloat() < 0.55F) {
                return SANGNUM;
            }
            info.setBlockPos(info.getPos().below(), UMBRALITH);
            float value = random.nextFloat();
            if (value < 0.5F) return PALLIDIUM_FULL;
            if (value < 0.8F) return PALLIDIUM_HEAVY;
            if (value < 0.93F) return PALLIDIUM_THIN;
            return PALLIDIUM_TINY;
        }
        if (info.getStateDown().isAir() && inUndersidePatch(info.getPos(), random, 0.7F, 0.08F)) {
            hangVineStrand(info, random);
        }
        return state;
    }

    private static boolean inUndersidePatch(
            BlockPos pos,
            RandomSource random,
            float inChance,
            float outChance
    ) {
        boolean inPatch = UNDERSIDE_PATCH.eval(pos.getX() * PATCH_FREQ, pos.getZ() * PATCH_FREQ)
                > PATCH_THRESHOLD;
        return random.nextFloat() < (inPatch ? inChance : outChance);
    }

    private static void hangStalactite(PosInfo info, RandomSource random) {
        int length = 2 + random.nextInt(4);
        BlockPos base = info.getPos();
        for (int k = 1; k <= length; k++) {
            int size = Mth.clamp(length - k, 0, 7);
            info.setBlockPos(
                    base.below(k),
                    EndBlocks.END_STONE_STALACTITE.defaultBlockState()
                                                        .setValue(StalactiteBlock.SIZE, size)
                                                        .setValue(StalactiteBlock.IS_FLOOR, false)
            );
        }
    }

    private static void hangVineStrand(PosInfo info, RandomSource random) {
        int length = 3 + random.nextInt(8);
        Block vine = pickVine(random);
        BlockPos base = info.getPos();
        for (int k = 1; k <= length; k++) {
            TripleShape shape = k == length
                    ? TripleShape.BOTTOM
                    : (k == 1 ? TripleShape.TOP : TripleShape.MIDDLE);
            info.setBlockPos(base.below(k), vine.defaultBlockState().setValue(BaseVineBlock.SHAPE, shape));
        }
    }

    private static Block pickVine(RandomSource random) {
        float value = random.nextFloat();
        if (value < 0.5F) return EndBlocks.BULB_VINE;
        if (value < 0.75F) return EndBlocks.TWISTED_VINE;
        return EndBlocks.JUNGLE_VINE;
    }

    private static SDF makeCone(float radiusBottom, float radiusTop, float height, float minY) {
        float halfHeight = height * 0.5F;
        SDF cone = new SDFCappedCone().setHeight(halfHeight)
                                        .setRadius1(radiusBottom)
                                        .setRadius2(radiusTop)
                                        .setBlock(END_STONE);
        return new SDFTranslate().setTranslate(0, minY + halfHeight, 0).setSource(cone);
    }

    private static boolean overlapsNativeLand(int blockX, int blockZ, int radius) {
        int[][] probes = {{0, 0}, {radius, 0}, {-radius, 0}, {0, radius}, {0, -radius}};
        for (int[] probe : probes) {
            if (TerrainGenerator.isLand((blockX + probe[0]) >> 2, (blockZ + probe[1]) >> 2, 128)) {
                return true;
            }
        }
        return false;
    }

    private static void primeHeightmaps(WorldGenLevel world, BlockPos center, int horizontalExtent) {
        int minChunkX = (center.getX() - horizontalExtent) >> 4;
        int maxChunkX = (center.getX() + horizontalExtent) >> 4;
        int minChunkZ = (center.getZ() - horizontalExtent) >> 4;
        int maxChunkZ = (center.getZ() + horizontalExtent) >> 4;
        EnumSet<Heightmap.Types> types = EnumSet.of(
                Heightmap.Types.WORLD_SURFACE_WG,
                Heightmap.Types.OCEAN_FLOOR_WG
        );
        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                ChunkAccess chunk = world.getChunk(chunkX, chunkZ);
                Heightmap.primeHeightmaps(chunk, types);
            }
        }
    }
}
