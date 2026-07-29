package org.betterx.betterend.world.features.terrain;

import org.betterx.bclib.util.MHelper;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;

import org.jetbrains.annotations.Nullable;

/** Shared deterministic layout for New Dawn small-island terrain and structures anchored to it. */
public final class SmallEndIslandLayout {
    private static final long LAYOUT_SALT = 0x6A09E667F3BCC909L;
    private static final long X_SALT = 0x9E3779B97F4A7C15L;
    private static final long Z_SALT = 0xC2B2AE3D27D4EB4FL;
    private static final int PLACEMENT_SALT = 448084798;
    private static final int SPACING = 5;
    private static final int SEPARATION = 2;

    private SmallEndIslandLayout() {
    }

    public record Geometry(
            float radius,
            float scaleX,
            float scale,
            float scaleZ,
            float noiseIntensity,
            int noiseSeed
    ) {
    }

    public record Island(int centerX, int centerY, int centerZ, long geometrySeed, Geometry geometry) {
        public Island withVariant(boolean flowerIslets) {
            return geometry == null
                    ? new Island(centerX, centerY, centerZ, geometrySeed, createGeometry(geometrySeed, flowerIslets))
                    : this;
        }

        public int radius() {
            return geometry == null ? 12 : Math.round(geometry.radius());
        }

        public int surfaceY(int x, int z) {
            return centerY;
        }
    }

    @Nullable
    public static Island forChunk(long worldSeed, ChunkPos chunkPos) {
        return forChunk(worldSeed, chunkPos.x, chunkPos.z);
    }

    @Nullable
    public static Island forChunk(long worldSeed, int chunkX, int chunkZ) {
        int regionX = Math.floorDiv(chunkX, SPACING);
        int regionZ = Math.floorDiv(chunkZ, SPACING);
        long regionSeed = mix(worldSeed
                ^ LAYOUT_SALT
                ^ (long) PLACEMENT_SALT
                ^ regionX * X_SALT
                ^ regionZ * Z_SALT);
        RandomSource placementRandom = RandomSource.create(regionSeed);
        int offsetBound = SPACING - SEPARATION;
        int candidateX = regionX * SPACING + placementRandom.nextInt(offsetBound);
        int candidateZ = regionZ * SPACING + placementRandom.nextInt(offsetBound);
        if (chunkX != candidateX || chunkZ != candidateZ || placementRandom.nextFloat() < 0.25F) {
            return null;
        }

        long layoutSeed = mix(worldSeed ^ LAYOUT_SALT ^ chunkX * X_SALT ^ chunkZ * Z_SALT);
        RandomSource random = RandomSource.create(layoutSeed);
        int centerX = (chunkX << 4) + 8;
        int centerZ = (chunkZ << 4) + 8;
        int centerY = MHelper.randRange(48, 68, random);
        long geometrySeed = mix(layoutSeed ^ BlockSeed.pack(centerX, centerY, centerZ));
        return new Island(centerX, centerY, centerZ, geometrySeed, null);
    }

    private static Geometry createGeometry(long seed, boolean flowerIslets) {
        RandomSource random = RandomSource.create(seed);
        float radius = flowerIslets
                ? MHelper.randRange(6, 12, random)
                : MHelper.randRange(12, 17, random);
        float scale = radius / 0.5F;
        float aspect = flowerIslets
                ? MHelper.randRange(0.55F, 0.9F, random)
                : MHelper.randRange(0.8F, 0.95F, random);
        float major = scale;
        float minor = scale * aspect;
        boolean swap = random.nextBoolean();
        float scaleX = swap ? minor : major;
        float scaleZ = swap ? major : minor;
        float noiseIntensity = (flowerIslets ? 1.6F : 0.8F) / scale;
        return new Geometry(radius, scaleX, scale, scaleZ, noiseIntensity, random.nextInt());
    }

    private static long mix(long value) {
        value = (value ^ (value >>> 30)) * 0xBF58476D1CE4E5B9L;
        value = (value ^ (value >>> 27)) * 0x94D049BB133111EBL;
        return value ^ (value >>> 31);
    }

    private static final class BlockSeed {
        private static long pack(int x, int y, int z) {
            return ((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (y & 0xFFFL);
        }
    }
}
