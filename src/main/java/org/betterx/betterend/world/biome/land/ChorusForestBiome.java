package org.betterx.betterend.world.biome.land;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;

public class ChorusForestBiome extends EndBiome.Config {
    public ChorusForestBiome() {
        super(EndBiomes.CHORUS_FOREST.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .fogColor(87, 26, 87)
                .fogDensity(1.5F)
                .plantsColor(122, 45, 122)
                .waterAndFogColor(73, 30, 73)
                .particles(ParticleTypes.PORTAL, 0.01F)
                .loop(EndSounds.AMBIENT_CHORUS_FOREST)
                .music(EndSounds.MUSIC_DARK)
                .feature(EndFeatures.VIOLECITE_LAYER)
                .feature(EndFeatures.END_LAKE_RARE)
                .feature(EndFeatures.PYTHADENDRON_TREE)
                .feature(EndFeatures.PYTHADENDRON_BUSH)
                .feature(Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT)
                .feature(EndFeatures.PURPLE_POLYPORE)
                .feature(EndFeatures.CHORUS_GRASS)
                .feature(EndFeatures.CHORUS_MUSHROOM)
                .feature(EndFeatures.TAIL_MOSS)
                .feature(EndFeatures.TAIL_MOSS_WOOD)
                .feature(EndFeatures.CHARNIA_PURPLE)
                .feature(EndFeatures.CHARNIA_RED_RARE)
                .structure(BiomeTags.HAS_END_CITY)
                .structure(EndStructures.ETERNAL_PORTAL)
                .spawn(EndEntities.END_SLIME, 5, 1, 2)
                .spawn(EntityType.ENDERMAN, 50, 1, 4);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.CHORUS_NYLIUM.defaultBlockState();
            }
        };
    }
}
