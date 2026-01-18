package org.betterx.datagen.betterend.worldgen;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeRegistry;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI.BiomeType;
import org.betterx.bclib.api.v3.datagen.TagDataProvider;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBiomes;
import org.betterx.betterend.registry.EndStructures;
import org.betterx.betterend.world.biome.EndBiome;
import org.betterx.betterend.world.biome.air.BiomeIceStarfield;
import org.betterx.betterend.world.biome.cave.*;
import org.betterx.betterend.world.biome.land.*;
import org.betterx.datagen.betterend.EndRegistrySupplier;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;

import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EndBiomesDataProvider extends TagDataProvider<Biome> {
    private static final Set<BCLBiome> BIOMES = new HashSet<>();
    // Better End Land
    private static final EndBiome FOGGY_MUSHROOMLAND = registerBiome(new FoggyMushroomlandBiome(), BiomeType.END_LAND);
    private static final EndBiome CHORUS_FOREST = registerBiome(new ChorusForestBiome(), BiomeType.END_LAND);
    private static final EndBiome DUST_WASTELANDS = registerBiome(new DustWastelandsBiome(), BiomeType.END_LAND);
    private static final EndBiome NEON_OASIS = registerSubBiome(new NeonOasisBiome(), DUST_WASTELANDS);
    private static final EndBiome PAINTED_MOUNTAINS = registerSubBiome(new PaintedMountainsBiome(), DUST_WASTELANDS);
    private static final EndBiome MEGALAKE = registerBiome(new MegalakeBiome(), BiomeType.END_LAND);
    private static final EndBiome MEGALAKE_GROVE = registerSubBiome(new MegalakeGroveBiome(), MEGALAKE);
    private static final EndBiome CRYSTAL_MOUNTAINS = registerBiome(new CrystalMountainsBiome(), BiomeType.END_LAND);
    private static final EndBiome SHADOW_FOREST = registerBiome(new ShadowForestBiome(), BiomeType.END_LAND);
    private static final EndBiome AMBER_LAND = registerBiome(new AmberLandBiome(), BiomeType.END_LAND);
    private static final EndBiome BLOSSOMING_SPIRES = registerBiome(new BlossomingSpiresBiome(), BiomeType.END_LAND);
    private static final EndBiome SULPHUR_SPRINGS = registerBiome(new SulphurSpringsBiome(), BiomeType.END_LAND);
    private static final EndBiome UMBRELLA_JUNGLE = registerBiome(new UmbrellaJungleBiome(), BiomeType.END_LAND);
    private static final EndBiome GLOWING_GRASSLANDS = registerBiome(new GlowingGrasslandsBiome(), BiomeType.END_LAND);
    private static final EndBiome DRAGON_GRAVEYARDS = registerBiome(new DragonGraveyardsBiome(), BiomeType.END_LAND);
    private static final EndBiome DRY_SHRUBLAND = registerBiome(new DryShrublandBiome(), BiomeType.END_LAND);
    private static final EndBiome LANTERN_WOODS = registerBiome(new LanternWoodsBiome(), BiomeType.END_LAND);
    private static final EndBiome UMBRA_VALLEY = registerBiome(new UmbraValleyBiome(), BiomeType.END_LAND);
    // Better End Void
    private static final EndBiome ICE_STARFIELD = registerBiome(new BiomeIceStarfield(), BiomeType.END_VOID);
    // Better End Caves
    private static final EndCaveBiome EMPTY_END_CAVE = registerCaveBiome(new EmptyEndCaveBiome());
    private static final EndCaveBiome EMPTY_SMARAGDANT_CAVE = registerCaveBiome(new EmptySmaragdantCaveBiome());
    private static final EndCaveBiome LUSH_SMARAGDANT_CAVE = registerCaveBiome(new LushSmaragdantCaveBiome());
    private static final EndCaveBiome EMPTY_AURORA_CAVE = registerCaveBiome(new EmptyAuroraCaveBiome());
    private static final EndCaveBiome LUSH_AURORA_CAVE = registerCaveBiome(new LushAuroraCaveBiome());
    private static final EndCaveBiome JADE_CAVE = registerCaveBiome(new JadeCaveBiome());

    /**
     * Constructs a new {@link TagDataProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link PackOutput} instance
     * @param registriesFuture the backing registry for the tag type
     */
    public EndBiomesDataProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture,
            ExistingFileHelper existingFileHelper
    ) {
        super(TagManager.BIOMES, List.of(BetterEnd.MOD_ID), output, registriesFuture, existingFileHelper);
    }


    public static void bootstrap(BootstapContext<Biome> ctx) {
        BCLBiomeBuilder.registerUnbound(ctx);
        EndRegistrySupplier.INSTANCE.MAIN_LOCK.release();
        BetterEnd.LOGGER.info("Registered Biomes");
    }

    public static void bootstrapBCL(BootstapContext<BCLBiome> ctx) {
        for (var biome : BIOMES) {
            ctx.register(biome.getBCLBiomeKey(), biome);

        }
        BetterEnd.LOGGER.info("Registered BCLBiomes: " + BCLBiomeRegistry.BUILTIN_BCL_BIOMES.size());
    }

    public static void ensureStaticallyLoaded() {
        EndBiomes.register();
        BCLBiomeRegistry.register();
        TagManager.BIOMES.add(
                EndStructures.ETERNAL_PORTAL.biomeTag,
                BiomeAPI.END_BARRENS.getBiomeKey(),
                BiomeAPI.END_MIDLANDS.getBiomeKey(),
                BiomeAPI.END_HIGHLANDS.getBiomeKey()
        );
    }

    static {
        try {
            EndRegistrySupplier.INSTANCE.MAIN_LOCK.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Put existing {@link EndBiome} as a sub-biome into selected parent.
     *
     * @param biomeConfig - {@link EndBiome.Config} instance
     * @param parent      - {@link EndBiome} to be linked with
     * @return registered {@link EndBiome}
     */
    public static EndBiome registerSubBiome(EndBiome.Config biomeConfig, EndBiome parent) {
        final EndBiome biome = EndBiome.createSubBiome(biomeConfig, parent);
        BIOMES.add(biome);
        return biome;
    }

    /**
     * Registers {@link EndBiome} and adds it into worldgen.
     *
     * @param biomeConfig - {@link EndBiome.Config} instance
     * @param type        - {@link BiomeType}
     * @return registered {@link EndBiome}
     */
    public static EndBiome registerBiome(EndBiome.Config biomeConfig, BiomeType type) {
        final EndBiome biome = EndBiome.create(biomeConfig, type);
        BIOMES.add(biome);
        return biome;
    }

    public static EndCaveBiome registerCaveBiome(EndCaveBiome.Config biomeConfig) {
        final EndCaveBiome biome = EndCaveBiome.create(biomeConfig);
        BIOMES.add(biome);
        return biome;
    }
}
