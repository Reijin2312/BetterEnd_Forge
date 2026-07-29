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

public class FlowerIsletsBiome extends EndBiome.Config {
    public FlowerIsletsBiome() {
        super(EndBiomes.FLOWER_ISLETS.location());
    }

    @Override
    protected boolean hasCaves() {
        return false;
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder.fogColor(215, 165, 240)
               .fogDensity(1.2F)
               .particles(EndParticles.TENANEA_PETAL, 0.0007F)
               .music(EndSounds.MUSIC_OPENSPACE)
               .waterAndFogColor(150, 210, 230)
               .plantsColor(140, 200, 120)
               .genChance(0.4F)
               // Bridge spawning is intentionally disabled; keep the implementation for later use.
               // .structure(EndStructures.END_BRIDGE)
               .feature(EndFeatures.SMALL_END_ISLAND)
               .feature(EndFeatures.BLOOMING_COOKSONIA)
               .feature(EndFeatures.SALTEAGO)
               .feature(EndFeatures.VAIOLUSH_FERN)
               .feature(EndFeatures.AERIDIUM)
               .feature(EndFeatures.LUTEBUS)
               .feature(EndFeatures.LAMELLARIUM)
               .feature(EndFeatures.FRACTURN)
               .feature(EndFeatures.GLOW_PILLAR)
               .feature(EndFeatures.AMARANITA_PATCH)
               .feature(EndFeatures.UMBRELLA_MOSS)
               .feature(EndFeatures.CREEPING_MOSS_RARE)
               .feature(EndFeatures.TWISTED_UMBRELLA_MOSS_RARE)
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
