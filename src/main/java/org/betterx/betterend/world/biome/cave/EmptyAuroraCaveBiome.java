package org.betterx.betterend.world.biome.cave;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder.BiomeSupplier;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeSettings;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.bclib.util.WeightedList;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.registry.EndParticles;
import org.betterx.betterend.world.biome.EndBiome;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.List;
import java.util.Optional;

public class EmptyAuroraCaveBiome extends EndCaveBiome.Config {
    public static final Codec<EmptyAuroraCaveBiome.Biome> CODEC = EndCaveBiome.simpleCaveBiomeCodec(EmptyAuroraCaveBiome.Biome::new);
    public static final KeyDispatchDataCodec<EmptyAuroraCaveBiome.Biome> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

    public static class Biome extends EndCaveBiome {
        public Biome(ResourceKey<net.minecraft.world.level.biome.Biome> biomeID, BCLBiomeSettings settings) {
            super(biomeID, settings);
            this.addFloorFeature(EndFeatures.BIG_AURORA_CRYSTAL.configuredFeature, 1);

            this.addCeilFeature(EndFeatures.END_STONE_STALACTITE.configuredFeature, 1);
        }

        @Override
        public KeyDispatchDataCodec<? extends BCLBiome> codec() {
            return EmptyAuroraCaveBiome.KEY_CODEC;
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
        public float getFloorDensity() {
            return 0.01F;
        }

        @Override
        public float getCeilDensity() {
            return 0.1F;
        }
    }

    public EmptyAuroraCaveBiome() {
        super("empty_aurora_cave");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        super.addCustomBuildData(builder);
        builder.fogColor(150, 30, 68)
               .fogDensity(2.0F)
               .plantsColor(108, 25, 46)
               .waterAndFogColor(186, 77, 237)
               .particles(EndParticles.GLOWING_SPHERE, 0.001F);
    }

    @Override
    public BiomeSupplier<EndBiome> getSupplier() {
        return EmptyAuroraCaveBiome.Biome::new;
    }
}
