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

public class EmptySmaragdantCaveBiome extends EndCaveBiome.Config {
    public static final Codec<EmptySmaragdantCaveBiome.Biome> CODEC = EndCaveBiome.simpleCaveBiomeCodec(
            EmptySmaragdantCaveBiome.Biome::new);
    public static final KeyDispatchDataCodec<EmptySmaragdantCaveBiome.Biome> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

    public static class Biome extends EndCaveBiome {
        public Biome(ResourceKey<net.minecraft.world.level.biome.Biome> biomeID, BCLBiomeSettings settings) {
            super(biomeID, settings);

            this.addFloorFeature(EndFeatures.SMARAGDANT_CRYSTAL.configuredFeature, 1);
            this.addFloorFeature(EndFeatures.SMARAGDANT_CRYSTAL_SHARD.configuredFeature, 20);

            this.addCeilFeature(EndFeatures.END_STONE_STALACTITE.configuredFeature, 1);
        }

        @Override
        public KeyDispatchDataCodec<? extends BCLBiome> codec() {
            return EmptySmaragdantCaveBiome.KEY_CODEC;
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
            return 0.1F;
        }

        @Override
        public float getCeilDensity() {
            return 0.1F;
        }
    }

    public EmptySmaragdantCaveBiome() {
        super("empty_smaragdant_cave");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        super.addCustomBuildData(builder);
        builder.fogColor(0, 253, 182)
               .fogDensity(2.0F)
               .plantsColor(0, 131, 145)
               .waterAndFogColor(31, 167, 212)
               .particles(EndParticles.SMARAGDANT, 0.001F);
    }

    @Override
    public BiomeSupplier<EndBiome> getSupplier() {
        return EmptySmaragdantCaveBiome.Biome::new;
    }
}
