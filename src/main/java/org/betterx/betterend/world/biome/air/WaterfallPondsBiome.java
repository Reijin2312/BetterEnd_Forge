package org.betterx.betterend.world.biome.air;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.EndBiomes;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.registry.EndParticles;
import org.betterx.betterend.registry.EndSounds;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WaterfallPondsBiome extends EndBiome.Config {
    public WaterfallPondsBiome() {
        super(EndBiomes.WATERFALL_PONDS.location());
    }

    @Override
    protected boolean hasCaves() {
        return false;
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder.fogColor(120, 200, 230)
               .fogDensity(1.1F)
               .particles(EndParticles.FIREFLY, 0.0008F)
               .music(EndSounds.MUSIC_WATER)
               .waterAndFogColor(80, 210, 220)
               .plantsColor(110, 195, 150)
               .genChance(0.4F)
               // Bridge spawning is intentionally disabled; keep the implementation for later use.
               // .structure(EndStructures.END_BRIDGE)
               .feature(EndFeatures.SMALL_END_ISLAND)
               .feature(EndFeatures.POND_WITH_WATERFALL)
               .feature(EndFeatures.DRAGON_HELIX_TREE)
               .feature(EndFeatures.BLOOMING_COOKSONIA)
               .feature(EndFeatures.SALTEAGO)
               .feature(EndFeatures.GLOBULAGUS)
               .feature(EndFeatures.UMBRELLA_MOSS)
               .feature(EndFeatures.CREEPING_MOSS)
               .feature(EndFeatures.END_LILY)
               .feature(EndFeatures.POND_ANEMONE)
               .feature(EndFeatures.BUBBLE_CORAL)
               .feature(EndFeatures.CHARNIA_CYAN)
               .feature(EndFeatures.CHARNIA_GREEN)
               .spawn(EntityType.ENDERMAN, 3, 1, 2)
               .endVoidBiome();
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.END_MOSS.defaultBlockState();
            }
        };
    }
}
