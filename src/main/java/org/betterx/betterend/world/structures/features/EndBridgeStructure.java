package org.betterx.betterend.world.structures.features;

import org.betterx.worlds.together.tag.v3.CommonBiomeTags;
import org.betterx.betterend.registry.EndBiomes;
import org.betterx.betterend.registry.EndStructures;
import org.betterx.betterend.world.features.terrain.SmallEndIslandLayout;
import org.betterx.betterend.world.features.terrain.SmallEndIslandLayout.Island;
import org.betterx.betterend.world.features.terrain.SmallEndIslandFeature;
import org.betterx.betterend.world.structures.piece.EndBridgePiece;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** A ruined bridge spanning real End terrain, deterministic New Dawn islands, or both. */
public class EndBridgeStructure extends FeatureBaseStructure {
    // Keep bridge code and data available, but do not schedule bridge generation for now.
    private static final boolean GENERATION_ENABLED = false;
    private static final int SEARCH_CHUNKS = 6;
    private static final int SCAN_DIRECTIONS = 16;
    private static final int SCAN_MIN_RADIUS = 16;
    private static final int SCAN_MAX_RADIUS = 96;
    private static final int SCAN_RADIUS_STEP = 8;
    private static final int TERRAIN_MIN_Y = 30;
    private static final int TERRAIN_MAX_Y = 90;
    private static final int MIN_SPAN = 16;
    private static final int MAX_SPAN = 96;
    private static final int MAX_HEIGHT_DELTA = 12;

    public EndBridgeStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public StructureType<EndBridgeStructure> type() {
        return EndStructures.END_BRIDGE.structureType;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        if (!GENERATION_ENABLED) {
            return Optional.empty();
        }
        ChunkPos chunkPos = context.chunkPos();
        BlockPos origin = new BlockPos(chunkPos.getBlockX(8), 0, chunkPos.getBlockZ(8));
        return Optional.of(new GenerationStub(origin, pieces -> generatePieces(pieces, context)));
    }

    @Override
    protected void generatePieces(StructurePiecesBuilder pieces, GenerationContext context) {
        ChunkPos origin = context.chunkPos();
        ChunkGenerator generator = context.chunkGenerator();
        RandomState randomState = context.randomState();
        LevelHeightAccessor level = context.heightAccessor();
        List<Anchor> anchors = new ArrayList<>();

        for (int dx = -SEARCH_CHUNKS; dx <= SEARCH_CHUNKS; dx++) {
            for (int dz = -SEARCH_CHUNKS; dz <= SEARCH_CHUNKS; dz++) {
                Island island = SmallEndIslandLayout.forChunk(context.seed(), origin.x + dx, origin.z + dz);
                if (island == null) {
                    continue;
                }

                Holder<Biome> biome = getNoiseBiome(
                        generator,
                        randomState,
                        island.centerX() >> 2,
                        island.centerY() >> 2,
                        island.centerZ() >> 2
                );
                if (context.validBiome().test(biome)) {
                    Island candidate = island.withVariant(biome.is(EndBiomes.FLOWER_ISLETS));
                    if (SmallEndIslandFeature.canGenerate(candidate)) {
                        anchors.add(Anchor.smallIsland(candidate));
                    }
                }
            }
        }

        int originX = origin.getBlockX(8);
        int originZ = origin.getBlockZ(8);
        for (int direction = 0; direction < SCAN_DIRECTIONS; direction++) {
            double angle = 2.0 * Math.PI * direction / SCAN_DIRECTIONS;
            double dx = Math.cos(angle);
            double dz = Math.sin(angle);
            for (int radius = SCAN_MIN_RADIUS; radius <= SCAN_MAX_RADIUS; radius += SCAN_RADIUS_STEP) {
                int x = originX + (int) Math.round(dx * radius);
                int z = originZ + (int) Math.round(dz * radius);
                int y = getBaseHeight(generator, randomState, level, x, z);
                if (!hasTerrain(y, level.getMinBuildHeight())) {
                    continue;
                }

                Holder<Biome> biome = getNoiseBiome(generator, randomState, x >> 2, y >> 2, z >> 2);
                if (biome.is(CommonBiomeTags.IS_SMALL_END_ISLAND)
                        || biome.is(CommonBiomeTags.IS_END_HIGHLAND)
                        || biome.is(CommonBiomeTags.IS_END_MIDLAND)) {
                    anchors.add(Anchor.terrain(x, y, z));
                    break;
                }
            }
        }

        if (anchors.size() < 2) {
            return;
        }

        Bridge bridge = findBestBridge(anchors, generator, randomState, level, originX, originZ);
        if (bridge != null) {
            pieces.addPiece(new EndBridgePiece(bridge.start(), bridge.end(), context.random().nextLong()));
        }
    }

    private static Bridge findBestBridge(
            List<Anchor> anchors,
            ChunkGenerator generator,
            RandomState randomState,
            LevelHeightAccessor level,
            int originX,
            int originZ
    ) {
        List<Bridge> bridges = new ArrayList<>();
        for (int first = 0; first < anchors.size() - 1; first++) {
            for (int second = first + 1; second < anchors.size(); second++) {
                Anchor a = anchors.get(first);
                Anchor b = anchors.get(second);
                BlockPos start = a.positionToward(b);
                BlockPos end = b.positionToward(a);
                double span = Math.sqrt(distanceSq(start.getX(), start.getZ(), end.getX(), end.getZ()));
                if (span < MIN_SPAN || span > MAX_SPAN) continue;
                if (Math.abs(start.getY() - end.getY()) > MAX_HEIGHT_DELTA) continue;

                long midpointDistance = distanceSq(
                        originX,
                        originZ,
                        (start.getX() + end.getX()) / 2,
                        (start.getZ() + end.getZ()) / 2
                );
                bridges.add(new Bridge(start, end, midpointDistance, span));
            }
        }
        bridges.sort(Comparator.comparingLong(Bridge::midpointDistance).thenComparingDouble(Bridge::span));
        for (Bridge bridge : bridges) {
            if (hasVoidGap(generator, randomState, level, bridge.start(), bridge.end())) {
                return bridge;
            }
        }
        return null;
    }

    private static boolean hasVoidGap(
            ChunkGenerator generator,
            RandomState randomState,
            LevelHeightAccessor level,
            BlockPos start,
            BlockPos end
    ) {
        double distance = Math.sqrt(distanceSq(start.getX(), start.getZ(), end.getX(), end.getZ()));
        int samples = Math.max(3, (int) Math.ceil(distance / 8.0));
        int voidSamples = 0;
        for (int sample = 1; sample < samples; sample++) {
            double progress = (double) sample / samples;
            int x = (int) Math.round(start.getX() + (end.getX() - start.getX()) * progress);
            int z = (int) Math.round(start.getZ() + (end.getZ() - start.getZ()) * progress);
            int y = getBaseHeight(generator, randomState, level, x, z);
            if (!hasTerrain(y, level.getMinBuildHeight())) {
                voidSamples++;
            }
        }
        return voidSamples >= 2;
    }

    private static int getBaseHeight(
            ChunkGenerator generator,
            RandomState randomState,
            LevelHeightAccessor level,
            int x,
            int z
    ) {
        return generator.getBaseHeight(x, z, Types.WORLD_SURFACE_WG, level, randomState);
    }

    private static boolean hasTerrain(int y, int minBuildHeight) {
        return y > minBuildHeight + 5 && y >= TERRAIN_MIN_Y && y <= TERRAIN_MAX_Y;
    }

    private static long distanceSq(int x1, int z1, int x2, int z2) {
        long dx = x1 - x2;
        long dz = z1 - z2;
        return dx * dx + dz * dz;
    }

    private record Anchor(int x, int y, int z, Island island) {
        static Anchor smallIsland(Island island) {
            return new Anchor(island.centerX(), island.centerY(), island.centerZ(), island);
        }

        static Anchor terrain(int x, int y, int z) {
            return new Anchor(x, y, z, null);
        }

        BlockPos positionToward(Anchor target) {
            return island == null
                    ? new BlockPos(x, y, z)
                    : SmallEndIslandFeature.anchorToward(island, target.x, target.z);
        }
    }

    private record Bridge(BlockPos start, BlockPos end, long midpointDistance, double span) {
    }
}
