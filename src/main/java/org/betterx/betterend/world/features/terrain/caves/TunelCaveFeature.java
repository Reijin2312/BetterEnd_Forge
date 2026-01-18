package org.betterx.betterend.world.features.terrain.caves;

import org.betterx.bclib.api.v2.generator.BiomePicker;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.config.Configs;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.noise.OpenSimplexNoise;
import org.betterx.betterend.registry.EndBiomes;
import org.betterx.betterend.world.biome.EndBiome;
import org.betterx.betterend.world.biome.cave.EndCaveBiome;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class TunelCaveFeature extends EndCaveFeature {
    private static int tunnelFloorErrCounter = 0;
    private static int tunnelCeilErrCounter = 0;
    private static int tunnelWallErrCounter = 0;

    private Set<BlockPos> generate(WorldGenLevel world, BlockPos center, RandomSource random) {
        int cx = center.getX() >> 4;
        int cz = center.getZ() >> 4;
        if ((long) cx * (long) cx + (long) cz + (long) cz < 256) {
            return Sets.newHashSet();
        }

        int x1 = cx << 4;
        int z1 = cz << 4;
        int x2 = x1 + 16;
        int z2 = z1 + 16;

        RandomSource rand = new LegacyRandomSource(world.getSeed());
        OpenSimplexNoise noiseH = new OpenSimplexNoise(rand.nextInt());
        OpenSimplexNoise noiseV = new OpenSimplexNoise(rand.nextInt());
        OpenSimplexNoise noiseD = new OpenSimplexNoise(rand.nextInt());

        Set<BlockPos> positions = Sets.newConcurrentHashSet();

        float a = hasCaves(world, new BlockPos(x1, 0, z1)) ? 1F : 0F;
        float b = hasCaves(world, new BlockPos(x2, 0, z1)) ? 1F : 0F;
        float c = hasCaves(world, new BlockPos(x1, 0, z2)) ? 1F : 0F;
        float d = hasCaves(world, new BlockPos(x2, 0, z2)) ? 1F : 0F;

        ChunkAccess chunk = world.getChunk(cx, cz);
        IntStream.range(0, 256).parallel().forEach(index -> {
            MutableBlockPos pos = new MutableBlockPos();
            int x = index & 15;
            int z = index >> 4;
            int wheight = chunk.getHeight(Types.WORLD_SURFACE_WG, x, z);
            float dx = x / 16F;
            float dz = z / 16F;
            pos.setX(x + x1);
            pos.setZ(z + z1);
            float da = Mth.lerp(dx, a, b);
            float db = Mth.lerp(dx, c, d);
            float density = 1 - Mth.lerp(dz, da, db);
            if (density < 0.5) {
                for (int y = 0; y < wheight; y++) {
                    pos.setY(y);
                    float gradient = 1 - Mth.clamp((wheight - y) * 0.1F, 0F, 1F);
                    if (gradient > 0.5) {
                        break;
                    }
                    float val = Mth.abs((float) noiseH.eval(pos.getX() * 0.02, y * 0.01, pos.getZ() * 0.02));
                    float vert = Mth.sin((y + (float) noiseV.eval(
                            pos.getX() * 0.01,
                            pos.getZ() * 0.01
                    ) * 20) * 0.1F) * 0.9F;
                    float dist = (float) noiseD.eval(pos.getX() * 0.1, y * 0.1, pos.getZ() * 0.1) * 0.12F;
                    val = (val + vert * vert + dist) + density + gradient;
                    if (val < 0.15 && world.getBlockState(pos).is(CommonBlockTags.GEN_END_STONES) && noWaterNear(
                            world,
                            pos
                    )) {
                        positions.add(pos.immutable());
                    }
                }
            }
        });
        positions.forEach(bpos -> BlocksHelper.setWithoutUpdate(world, bpos, CAVE_AIR));

        return positions;
    }

    private boolean noWaterNear(WorldGenLevel world, BlockPos pos) {
        BlockPos above1 = pos.above();
        BlockPos above2 = pos.above(2);
        if (!world.getFluidState(above1).isEmpty() || !world.getFluidState(above2).isEmpty()) {
            return false;
        }
        for (Direction dir : BlocksHelper.HORIZONTAL) {
            if (!world.getFluidState(above1.relative(dir)).isEmpty()) {
                return false;
            }
            if (!world.getFluidState(above2.relative(dir)).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
        final RandomSource random = featureConfig.random();
        final BlockPos pos = featureConfig.origin();
        final WorldGenLevel world = featureConfig.level();
        if (pos.getX() * pos.getX() + pos.getZ() * pos.getZ() <= 2500) {
            return false;
        }

        if (biomeMissingCaves(world, pos)) {
            return false;
        }

        Set<BlockPos> caveBlocks = generate(world, pos, random);
        if (caveBlocks.isEmpty()) {
            return false;
        }

        final ChunkGenerator generator = featureConfig.chunkGenerator();
        Map<BiomePicker.ActualBiome, Set<BlockPos>> floorSets = Maps.newHashMap();
        Map<BiomePicker.ActualBiome, Set<BlockPos>> ceilSets = Maps.newHashMap();
        MutableBlockPos mut = new MutableBlockPos();
        Set<BlockPos> remove = Sets.newHashSet();
        caveBlocks.forEach((bpos) -> {
            mut.set(bpos);
            BiomePicker.ActualBiome bio = EndBiomes.getCaveBiome(bpos.getX(), bpos.getZ());
            int height = world.getHeight(Types.WORLD_SURFACE, bpos.getX(), bpos.getZ());
            if (mut.getY() >= height) {
                remove.add(bpos);
            } else if (world.getBlockState(mut).canBeReplaced()) {
                mut.setY(bpos.getY() - 1);
                if (world.getBlockState(mut).is(CommonBlockTags.GEN_END_STONES)) {
                    Set<BlockPos> floorPositions = floorSets.get(bio);
                    if (floorPositions == null) {
                        floorPositions = Sets.newHashSet();
                        floorSets.put(bio, floorPositions);
                    }
                    floorPositions.add(mut.immutable());
                }
                mut.setY(bpos.getY() + 1);
                if (world.getBlockState(mut).is(CommonBlockTags.GEN_END_STONES)) {
                    Set<BlockPos> ceilPositions = ceilSets.get(bio);
                    if (ceilPositions == null) {
                        ceilPositions = Sets.newHashSet();
                        ceilSets.put(bio, ceilPositions);
                    }
                    ceilPositions.add(mut.immutable());
                }
                setBiome(world, bpos, bio);
            }
        });
        caveBlocks.removeAll(remove);

        if (caveBlocks.isEmpty()) {
            return true;
        }

        floorSets.forEach((biome, floorPositions) -> {
            if (biome.bclBiome instanceof EndCaveBiome caveBiome) {
                BlockState surfaceBlock = EndBiome.findTopMaterial(biome.bclBiome);
                placeFloor(world, generator, caveBiome, floorPositions, random, surfaceBlock);
            } else if (Configs.MAIN_CONFIG.verboseLogging() && tunnelFloorErrCounter < 25) {
                tunnelFloorErrCounter++;
                BetterEnd.LOGGER.error(biome.bclBiome.getID() + " is not an EndCaveBiome. Unable to place Tunnel Floor");
            }
        });
        ceilSets.forEach((biome, ceilPositions) -> {
            if (biome.bclBiome instanceof EndCaveBiome caveBiome) {
                placeCeil(world, generator, caveBiome, ceilPositions, random);
            } else if (Configs.MAIN_CONFIG.verboseLogging() && tunnelCeilErrCounter < 25) {
                tunnelCeilErrCounter++;
                BetterEnd.LOGGER.error(biome.bclBiome.getID() + " is not an EndCaveBiome. Unable to place Tunnel Ceiling");
            }
        });
        BiomePicker.ActualBiome biome = EndBiomes.getCaveBiome(pos.getX(), pos.getZ());
        if (biome.bclBiome instanceof EndCaveBiome caveBiome) {
            placeWalls(world, generator, caveBiome, caveBlocks, random);
        } else if (Configs.MAIN_CONFIG.verboseLogging() && tunnelWallErrCounter < 25) {
            tunnelWallErrCounter++;
            BetterEnd.LOGGER.error(biome.bclBiome.getID() + " is not an EndCaveBiome. Unable to place Tunnel Walls");
        }
        fixBlocks(world, caveBlocks);

        return true;
    }

    @Override
    protected Set<BlockPos> generate(WorldGenLevel world, BlockPos center, int radius, RandomSource random) {
        return null;
    }

    @Override
    protected void placeFloor(
            WorldGenLevel world,
            ChunkGenerator generator,
            EndCaveBiome biome,
            Set<BlockPos> floorPositions,
            RandomSource random,
            BlockState surfaceBlock
    ) {
        float density = biome.getFloorDensity() * 0.2F;
        floorPositions.forEach((pos) -> {
            if (!surfaceBlock.is(Blocks.END_STONE)) {
                BlocksHelper.setWithoutUpdate(world, pos, surfaceBlock);
            }
            if (density > 0 && random.nextFloat() <= density) {
                Holder<? extends ConfiguredFeature<?, ?>> feature = biome.getFloorFeature(random);
                if (feature != null && feature.isBound()) {
                    feature.value().place(world, generator, random, pos.above());
                }
            }
        });
    }

    @Override
    protected void placeCeil(
            WorldGenLevel world,
            ChunkGenerator generator,
            EndCaveBiome biome,
            Set<BlockPos> ceilPositions,
            RandomSource random
    ) {
        float density = biome.getCeilDensity() * 0.2F;
        ceilPositions.forEach((pos) -> {
            BlockState ceilBlock = biome.getCeil(pos);
            if (ceilBlock != null) {
                BlocksHelper.setWithoutUpdate(world, pos, ceilBlock);
            }
            if (density > 0 && random.nextFloat() <= density) {
                Holder<? extends ConfiguredFeature<?, ?>> feature = biome.getCeilFeature(random);
                if (feature != null && feature.isBound()) {
                    feature.value().place(world, generator, random, pos.below());
                }
            }
        });
    }

    protected boolean hasCaves(WorldGenLevel world, BlockPos pos) {
        return hasCavesInBiome(world, pos.offset(-8, 0, -8)) && hasCavesInBiome(
                world,
                pos.offset(8, 0, -8)
        ) && hasCavesInBiome(
                world,
                pos.offset(
                        -8,
                        0,
                        8
                )
        ) && hasCavesInBiome(
                world,
                pos.offset(8, 0, 8)
        );
    }

    protected boolean hasCavesInBiome(WorldGenLevel world, BlockPos pos) {
        Holder<Biome> biome = world.getBiome(pos);
        BCLBiome bclBiome = BiomeAPI.getBiome(biome);
        if (bclBiome instanceof EndBiome endBiome)
            return endBiome.hasCaves();
        return true;
    }
}
