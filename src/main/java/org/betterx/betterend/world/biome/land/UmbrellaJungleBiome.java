package org.betterx.betterend.world.biome.land;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.betterend.registry.*;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

public class UmbrellaJungleBiome extends EndBiome.Config {
    public UmbrellaJungleBiome() {
        super(EndBiomes.UMBRELLA_JUNGLE.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .fogColor(87, 223, 221)
                .waterAndFogColor(119, 198, 253)
                .foliageColor(27, 183, 194)
                .fogDensity(2.3F)
                .particles(EndParticles.JUNGLE_SPORE, 0.001F)
                .music(EndSounds.MUSIC_FOREST)
                .loop(EndSounds.AMBIENT_UMBRELLA_JUNGLE)
                .feature(EndFeatures.END_LAKE)
                .feature(EndFeatures.UMBRELLA_TREE)
                .feature(EndFeatures.JELLYSHROOM)
                .feature(EndFeatures.TWISTED_UMBRELLA_MOSS)
                .feature(EndFeatures.SMALL_JELLYSHROOM_FLOOR)
                .feature(EndFeatures.JUNGLE_GRASS)
                .feature(EndFeatures.CYAN_MOSS)
                .feature(EndFeatures.CYAN_MOSS_WOOD)
                .feature(EndFeatures.JUNGLE_FERN_WOOD)
                .feature(EndFeatures.SMALL_JELLYSHROOM_WALL)
                .feature(EndFeatures.SMALL_JELLYSHROOM_WOOD)
                .feature(EndFeatures.SMALL_JELLYSHROOM_CEIL)
                .feature(EndFeatures.JUNGLE_VINE)
                .feature(EndFeatures.CHARNIA_CYAN)
                .feature(EndFeatures.CHARNIA_GREEN)
                .feature(EndFeatures.CHARNIA_LIGHT_BLUE)
                .feature(EndFeatures.CHARNIA_RED_RARE)
                .structure(BiomeTags.HAS_END_CITY)
                .structure(EndStructures.ETERNAL_PORTAL)
                .spawn(EntityType.ENDERMAN, 50, 1, 2);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.JUNGLE_MOSS.defaultBlockState();
            }
        };
    }
}