package org.betterx.betterend.world.biome.cave;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder.BiomeSupplier;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeSettings;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.bclib.util.WeightedList;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBiomes;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.registry.EndSounds;
import org.betterx.betterend.world.biome.EndBiome;
import org.betterx.betterend.world.features.terrain.caves.CaveChunkPopulatorFeature;
import org.betterx.betterend.world.features.terrain.caves.CaveChunkPopulatorFeatureConfig;

import com.mojang.datafixers.util.Function14;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.List;
import java.util.Optional;

public class EndCaveBiome extends EndBiome {
    public static final Codec<EndCaveBiome> CODEC = simpleCaveBiomeCodec(EndCaveBiome::new);

    public static <T extends EndCaveBiome> Codec<T> simpleCaveBiomeCodec(Function14<Float, Float, Float, Integer, Boolean, Optional<ResourceLocation>, ResourceLocation, Optional<List<Climate.ParameterPoint>>, Optional<ResourceLocation>, Optional<String>, Boolean, SurfaceMaterialProvider, WeightedList<Holder<ConfiguredFeature<?, ?>>>, WeightedList<Holder<ConfiguredFeature<?, ?>>>, T> builder) {
        return RecordCodecBuilder.create(instance ->
                codecWithSettings(
                        instance,
                        Codec.BOOL.fieldOf("has_caves").orElse(true).forGetter(EndBiome::hasCaves),
                        SurfaceMaterialProvider.CODEC.fieldOf("surface")
                                                     .orElse(new DefaultSurfaceMaterialProvider())
                                                     .forGetter(o -> o.surfMatProv),
                        WeightedList.listCodec(ConfiguredFeature.CODEC, "configured_features", "configured_feature")
                                    .fieldOf("floor_features")
                                    .forGetter(o -> (WeightedList) ((EndCaveBiome) o).floorFeatures),
                        WeightedList.listCodec(ConfiguredFeature.CODEC, "configured_features", "configured_feature")
                                    .fieldOf("ceil_features")
                                    .forGetter(o -> (WeightedList) ((EndCaveBiome) o).ceilFeatures)
                ).apply(instance, builder)
        );
    }

    public static final KeyDispatchDataCodec<EndCaveBiome> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

    @Override
    public KeyDispatchDataCodec<? extends BCLBiome> codec() {
        return KEY_CODEC;
    }

    protected EndCaveBiome(
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
                surface
        );
        this.floorFeatures.addAll((WeightedList) floorFeatures);
        this.ceilFeatures.addAll((WeightedList) ceilFeatures);
    }

    public static abstract class Config extends EndBiome.Config {
        protected Config(String name) {
            super(name);
        }

        @Override
        protected void addCustomBuildData(BCLBiomeBuilder builder) {
            builder.type(EndBiomes.END_CAVE);
            BCLFeature<CaveChunkPopulatorFeature, CaveChunkPopulatorFeatureConfig> feature = BCLFeatureBuilder
                    .start(
                            BetterEnd.makeID(ID.getPath() + "_cave_populator"),
                            EndFeatures.CAVE_CHUNK_POPULATOR
                    )
                    .configuration(new CaveChunkPopulatorFeatureConfig(ID))
                    .build()
                    .place()
                    .decoration(GenerationStep.Decoration.UNDERGROUND_DECORATION)
                    .count(1)
                    .onlyInBiome()
                    .build();

            builder.feature(feature)
                   .intendedType(EndBiomes.END_CAVE)
                   .music(EndSounds.MUSIC_CAVES)
                   .loop(EndSounds.AMBIENT_CAVES);
        }

        @Override
        protected boolean hasCaves() {
            return false;
        }

        @Override
        protected boolean hasReturnGateway() {
            return false;
        }

        @Override
        public BiomeSupplier<EndBiome> getSupplier() {
            return EndCaveBiome::new;
        }
    }

    private final WeightedList<Holder<? extends ConfiguredFeature<?, ?>>> floorFeatures = new WeightedList<>();
    private final WeightedList<Holder<? extends ConfiguredFeature<?, ?>>> ceilFeatures = new WeightedList<>();

    public EndCaveBiome(ResourceKey<Biome> biomeID, BCLBiomeSettings settings) {
        super(biomeID, settings);
    }

    public void addFloorFeature(Holder<? extends ConfiguredFeature<?, ?>> feature, float weight) {
        floorFeatures.add(feature, weight);
    }

    public void addCeilFeature(Holder<? extends ConfiguredFeature<?, ?>> feature, float weight) {
        ceilFeatures.add(feature, weight);
    }

    public Holder<? extends ConfiguredFeature<?, ?>> getFloorFeature(RandomSource random) {
        return floorFeatures.isEmpty() ? null : floorFeatures.get(random);
    }

    public Holder<? extends ConfiguredFeature<?, ?>> getCeilFeature(RandomSource random) {
        return ceilFeatures.isEmpty() ? null : ceilFeatures.get(random);
    }

    public float getFloorDensity() {
        return 0;
    }

    public float getCeilDensity() {
        return 0;
    }

    public BlockState getCeil(BlockPos pos) {
        return null;
    }

    public BlockState getWall(BlockPos pos) {
        return null;
    }

    public static EndCaveBiome create(EndBiome.Config biomeConfig) {
        return (EndCaveBiome) EndBiome.create(biomeConfig, EndBiomes.END_CAVE);
    }
}
