package org.betterx.betterend.world.biome.air;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.registry.EndParticles;
import org.betterx.betterend.registry.EndStructures;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.world.entity.EntityType;

public class BiomeIceStarfield extends EndBiome.Config {
    public BiomeIceStarfield() {
        super("ice_starfield");
    }

    @Override
    protected boolean hasCaves() {
        return false;
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder.structure(EndStructures.GIANT_ICE_STAR)
               .fogColor(224, 245, 254)
               .temperature(0F)
               .fogDensity(2.2F)
               .foliageColor(193, 244, 244)
               .genChance(0.25F)
               .particles(EndParticles.SNOWFLAKE, 0.002F)
               .feature(EndFeatures.ICE_STAR)
               .feature(EndFeatures.ICE_STAR_SMALL)
               .spawn(EntityType.ENDERMAN, 20, 1, 4)
               .endVoidBiome();
    }
}
