package org.betterx.betterend.world.biome.cave;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder.BiomeSupplier;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeSettings;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.bclib.util.WeightedList;
import org.betterx.betterend.noise.OpenSimplexNoise;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.world.biome.EndBiome;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.List;
import java.util.Optional;

public class JadeCaveBiome extends EndCaveBiome.Config {
    public static final Codec<JadeCaveBiome.Biome> CODEC = EndCaveBiome.simpleCaveBiomeCodec(JadeCaveBiome.Biome::new);
    public static final KeyDispatchDataCodec<JadeCaveBiome.Biome> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

    public static class Biome extends EndCaveBiome {
        private static final OpenSimplexNoise WALL_NOISE = new OpenSimplexNoise("jade_cave".hashCode());
        private static final OpenSimplexNoise DEPTH_NOISE = new OpenSimplexNoise("depth_noise".hashCode());
        private static final BlockState[] JADE = new BlockState[3];


        public Biome(ResourceKey<net.minecraft.world.level.biome.Biome> biomeID, BCLBiomeSettings settings) {
            super(biomeID, settings);
        }

        @Override
        public KeyDispatchDataCodec<? extends BCLBiome> codec() {
            return JadeCaveBiome.KEY_CODEC;
        }

        protected Biome(
                float terrainHeight,
                float fogDensity,
                float genChance,
                int edgeSize,
                boolean vertical,
                Optional<ResourceLocation> edge,
                ResourceLocation biomeID,
                Optional<List<Climate.ParameterPoint>> parameterPoints,
                Optional<ResourceLocation> biomeParent,
                Optional<String> intendedType,
                boolean hasCaves,
                SurfaceMaterialProvider surface,
                WeightedList<Holder<ConfiguredFeature<?, ?>>> floorFeatures,
                WeightedList<Holder<ConfiguredFeature<?, ?>>> ceilFeatures
        ) {
            super(
                    terrainHeight,
                    fogDensity,
                    genChance,
                    edgeSize,
                    vertical,
                    edge,
                    biomeID,
                    parameterPoints,
                    biomeParent,
                    intendedType,
                    hasCaves,
                    surface,
                    floorFeatures,
                    ceilFeatures
            );
        }

        @Override
        public BlockState getWall(BlockPos pos) {
            double depth = DEPTH_NOISE.eval(pos.getX() * 0.02, pos.getZ() * 0.02) * 0.2 + 0.5;
            int index = Mth.floor((pos.getY() + WALL_NOISE.eval(
                    pos.getX() * 0.2,
                    pos.getZ() * 0.2
            ) * 1.5) * depth + 0.5);
            index = Mth.abs(index) % 3;
            return JADE[index];
        }

        static {
            JADE[0] = EndBlocks.VIRID_JADESTONE.stone.defaultBlockState();
            JADE[1] = EndBlocks.AZURE_JADESTONE.stone.defaultBlockState();
            JADE[2] = EndBlocks.SANDY_JADESTONE.stone.defaultBlockState();
        }
    }

    public JadeCaveBiome() {
        super("jade_cave");
    }


    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        super.addCustomBuildData(builder);
        builder.fogColor(118, 150, 112)
               .fogDensity(2.0F)
               .waterAndFogColor(95, 223, 255);
    }

    @Override
    public BiomeSupplier<EndBiome> getSupplier() {
        return JadeCaveBiome.Biome::new;
    }


}
