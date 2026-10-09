package org.betterx.betterend.world.structures.features;

import org.betterx.worlds.together.tag.v3.CommonBiomeTags;
import org.betterx.betterend.registry.EndStructures;
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
import java.util.List;
import java.util.Optional;

/** A ruined bridge spanning real End terrain, deterministic New Dawn islands, or both. */
public class EndBridgeStructure extends FeatureBaseStructure {
    private static final int SCAN_DIRECTIONS = 16;
    private static final int SCAN_MIN_RADIUS = 16;
    private static final int SCAN_MAX_RADIUS = 96;
    private static final int SCAN_RADIUS_STEP = 8;
    private static final int TERRAIN_MIN_Y = 30;
    private static final int TERRAIN_MAX_Y = 90;
    private static final int VOID_MARGIN = 5;
    private static final int MIN_SPAN = 24;
    private static final int MAX_SPAN = 96;
    private static final int MAX_HEIGHT_DELTA = 12;
    private static final double MIN_DIRECTION_SEPARATION = Math.PI / 2.0;

    public EndBridgeStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public StructureType<EndBridgeStructure> type() {
        return EndStructures.END_BRIDGE.structureType;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
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
                boolean island = biome.is(CommonBiomeTags.IS_SMALL_END_ISLAND);
                boolean land = biome.is(CommonBiomeTags.IS_END_HIGHLAND)
                        || biome.is(CommonBiomeTags.IS_END_MIDLAND);
                if (island || land) {
                    anchors.add(new Anchor(x, y, z, angle, island));
                    break;
                }
            }
        }

        if (anchors.size() < 2) {
            return;
        }

        anchors.sort((a, b) -> Integer.compare(a.distanceTo(originX, originZ), b.distanceTo(originX, originZ)));
        Anchor start = anchors.get(0);
        Anchor end = null;
        for (int i = 1; i < anchors.size(); i++) {
            Anchor candidate = anchors.get(i);
            if (!start.island && !candidate.island) continue;
            if (Math.abs(start.y - candidate.y) > MAX_HEIGHT_DELTA) continue;
            if (angularSeparation(start.angle, candidate.angle) < MIN_DIRECTION_SEPARATION) continue;
            double span = Math.sqrt(distanceSq(start.x, start.z, candidate.x, candidate.z));
            if (span >= MIN_SPAN && span <= MAX_SPAN) {
                end = candidate;
                break;
            }
        }
        if (end != null) {
            pieces.addPiece(new EndBridgePiece(
                    new BlockPos(start.x, start.y, start.z),
                    new BlockPos(end.x, end.y, end.z),
                    context.random().nextLong()
            ));
        }
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
        return y > minBuildHeight + VOID_MARGIN && y >= TERRAIN_MIN_Y && y <= TERRAIN_MAX_Y;
    }

    private static long distanceSq(int x1, int z1, int x2, int z2) {
        long dx = x1 - x2;
        long dz = z1 - z2;
        return dx * dx + dz * dz;
    }

    private static double angularSeparation(double first, double second) {
        double difference = Math.abs(first - second) % (2.0 * Math.PI);
        return difference > Math.PI ? 2.0 * Math.PI - difference : difference;
    }

    private record Anchor(int x, int y, int z, double angle, boolean island) {
        int distanceTo(int originX, int originZ) {
            return (int) distanceSq(x, z, originX, originZ);
        }
    }
}
