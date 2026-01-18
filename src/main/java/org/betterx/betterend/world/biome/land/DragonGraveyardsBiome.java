package org.betterx.betterend.world.biome.land;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DragonGraveyardsBiome extends EndBiome.Config {
    public DragonGraveyardsBiome() {
        super("dragon_graveyards");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .genChance(0.1f)
                .fogColor(244, 46, 79)
                .fogDensity(1.3F)
                .particles(EndParticles.FIREFLY, 0.0007F)
                .music(EndSounds.MUSIC_OPENSPACE)
                .loop(EndSounds.AMBIENT_GLOWING_GRASSLANDS)
                .waterAndFogColor(203, 59, 167)
                .plantsColor(244, 46, 79)
                .feature(EndFeatures.OBSIDIAN_PILLAR_BASEMENT)
                .feature(EndFeatures.DRAGON_BONE_BLOCK_ORE)
                .feature(EndFeatures.FALLEN_PILLAR)
                .feature(EndFeatures.OBSIDIAN_BOULDER)
                .feature(EndFeatures.GIGANTIC_AMARANITA)
                .feature(EndFeatures.LARGE_AMARANITA)
                .feature(EndFeatures.SMALL_AMARANITA)
                .feature(EndFeatures.GLOBULAGUS)
                .feature(EndFeatures.CLAWFERN)
                .structure(EndStructures.ETERNAL_PORTAL)
                .spawn(EntityType.ENDERMAN, 50, 1, 2);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.SANGNUM.defaultBlockState();
            }
        };
    }
}
