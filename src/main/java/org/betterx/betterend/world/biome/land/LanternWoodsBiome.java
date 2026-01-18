package org.betterx.betterend.world.biome.land;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LanternWoodsBiome extends EndBiome.Config {
    public LanternWoodsBiome() {
        super(EndBiomes.LANTERN_WOODS.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .fogColor(189, 82, 70)
                .fogDensity(1.1F)
                .waterAndFogColor(171, 234, 226)
                .plantsColor(254, 85, 57)
                .music(EndSounds.MUSIC_FOREST)
                .particles(EndParticles.GLOWING_SPHERE, 0.001F)
                .feature(EndFeatures.END_LAKE_NORMAL)
                .feature(EndFeatures.FLAMAEA)
                .feature(EndFeatures.LUCERNIA)
                .feature(EndFeatures.LUCERNIA_BUSH)
                .feature(EndFeatures.FILALUX)
                .feature(EndFeatures.AERIDIUM)
                .feature(EndFeatures.LAMELLARIUM)
                .feature(EndFeatures.BOLUX_MUSHROOM)
                .feature(EndFeatures.AURANT_POLYPORE)
                .feature(EndFeatures.POND_ANEMONE)
                .feature(EndFeatures.CHARNIA_ORANGE)
                .feature(EndFeatures.CHARNIA_RED)
                .feature(EndFeatures.RUSCUS)
                .feature(EndFeatures.RUSCUS_WOOD)
                .structure(BiomeTags.HAS_END_CITY)
                .structure(EndStructures.ETERNAL_PORTAL)
                .spawn(EntityType.ENDERMAN, 50, 1, 2);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.RUTISCUS.defaultBlockState();
            }
        };
    }
}
