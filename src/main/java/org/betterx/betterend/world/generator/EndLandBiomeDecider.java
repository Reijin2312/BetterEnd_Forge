package org.betterx.betterend.world.generator;

import org.betterx.bclib.api.v2.generator.BCLBiomeSource;
import org.betterx.bclib.api.v2.generator.BCLibEndBiomeSource;
import org.betterx.bclib.api.v2.generator.BiomeDecider;
import org.betterx.bclib.api.v2.generator.config.BCLEndBiomeSourceConfig;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

public class EndLandBiomeDecider extends BiomeDecider {
    public EndLandBiomeDecider() {
        this(null);
    }

    protected EndLandBiomeDecider(HolderGetter<Biome> biomeRegistry) {
        super(biomeRegistry, (biome) -> false);
    }

    @Override
    public boolean canProvideFor(BiomeSource source) {
        if (source instanceof BCLibEndBiomeSource endSource) {
            return endSource.getTogetherConfig().generatorVersion == BCLEndBiomeSourceConfig.EndBiomeGeneratorType.PAULEVS;
        }
        return false;
    }

    @Override
    public BiomeDecider createInstance(BCLBiomeSource biomeSource) {
        //TODO: 1.19.4: This ok?
        return new EndLandBiomeDecider(/*biomeSource.getBiomeRegistry()*/);
    }

    @Override
    public BiomeAPI.BiomeType suggestType(
            BiomeAPI.BiomeType originalType,
            BiomeAPI.BiomeType suggestedType,
            double density,
            int maxHeight,
            int blockX,
            int blockY,
            int blockZ,
            int quarterX,
            int quarterY,
            int quarterZ
    ) {
        if (TerrainGenerator.isLand(quarterX, quarterZ, maxHeight)) {
            return suggestedType.equals(BiomeAPI.BiomeType.END_CENTER)
                    ? suggestedType
                    : BiomeAPI.BiomeType.END_LAND;
        } else {
            return suggestedType.equals(BiomeAPI.BiomeType.END_CENTER)
                    ? BiomeAPI.BiomeType.END_BARRENS
                    : BiomeAPI.BiomeType.END_VOID;
        }
    }

    @Override
    public boolean canProvideBiome(BiomeAPI.BiomeType suggestedType) {
        return false;
    }
}
