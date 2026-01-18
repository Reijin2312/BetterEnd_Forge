package org.betterx.betterend.world.features.terrain.caves;

import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.betterend.world.biome.cave.EndCaveBiome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import org.jetbrains.annotations.Nullable;

public record CaveChunkPopulatorFeatureConfig(ResourceLocation biomeID) implements FeatureConfiguration {
    public static final Codec<CaveChunkPopulatorFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ResourceLocation.CODEC.fieldOf("biome").forGetter(o -> o.biomeID))
            .apply(instance, CaveChunkPopulatorFeatureConfig::new));

    public @Nullable EndCaveBiome getCaveBiome() {
        return (EndCaveBiome) BiomeAPI.getBiome(biomeID);
    }
}
