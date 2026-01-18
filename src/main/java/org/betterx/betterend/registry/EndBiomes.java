package org.betterx.betterend.registry;

import org.betterx.bclib.api.v2.LifeCycleAPI;
import org.betterx.bclib.api.v2.generator.BiomePicker;
import org.betterx.bclib.api.v2.generator.map.hex.HexBiomeMap;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeRegistry;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.world.biome.EndBiome;
import org.betterx.betterend.world.biome.cave.*;
import org.betterx.betterend.world.generator.GeneratorOptions;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;

public class EndBiomes {
    public static final BiomeAPI.BiomeType END_CAVE = new BiomeAPI.BiomeType("END_CAVE", BiomeAPI.BiomeType.END_IGNORE);
    public static BiomePicker CAVE_BIOMES = null;
    private static HexBiomeMap caveBiomeMap;
    private static long lastSeed;

    public static final ResourceKey<Biome> AMBER_LAND = cKey("amber_land");
    public static final ResourceKey<Biome> BLOSSOMING_SPIRES = cKey("blossoming_spires");
    public static final ResourceKey<Biome> CHORUS_FOREST = cKey("chorus_forest");
    public static final ResourceKey<Biome> FOGGY_MUSHROOMLAND = cKey("foggy_mushroomland");
    public static final ResourceKey<Biome> GLOWING_GRASSLANDS = cKey("glowing_grasslands");
    public static final ResourceKey<Biome> LANTERN_WOODS = cKey("lantern_woods");
    public static final ResourceKey<Biome> MEGALAKE = cKey("megalake");
    public static final ResourceKey<Biome> MEGALAKE_GROVE = cKey("megalake_grove");
    public static final ResourceKey<Biome> NEON_OASIS = cKey("neon_oasis");
    public static final ResourceKey<Biome> SHADOW_FOREST = cKey("shadow_forest");
    public static final ResourceKey<Biome> SULPHUR_SPRINGS = cKey("sulphur_springs");
    public static final ResourceKey<Biome> UMBRELLA_JUNGLE = cKey("umbrella_jungle");


    private static ResourceKey<Biome> cKey(String path) {
        return ResourceKey.create(Registries.BIOME, BetterEnd.makeID(path));
    }

    public static void register() {
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("biome"), EndBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("cave_biome"), EndCaveBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(
                BetterEnd.makeID("empty_aurora_cave_biome"),
                EmptyAuroraCaveBiome.KEY_CODEC
        );
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("empty_end_cave_biome"), EmptyEndCaveBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(
                BetterEnd.makeID("empty_smaragdant_cave_biome"),
                EmptySmaragdantCaveBiome.KEY_CODEC
        );
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("jade_cave_biome"), JadeCaveBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(BetterEnd.makeID("lush_aurora_cave_biome"), LushAuroraCaveBiome.KEY_CODEC);
        BCLBiomeRegistry.registerBiomeCodec(
                BetterEnd.makeID("lush_smaragdant_cave_biome"),
                LushSmaragdantCaveBiome.KEY_CODEC
        );
        LifeCycleAPI.onLevelLoad(EndBiomes::onWorldLoad);
    }

    private static void onWorldLoad(ServerLevel level, long seed, Registry<Biome> registry) {
        if (CAVE_BIOMES == null || CAVE_BIOMES.biomeRegistry != registry) {
            CAVE_BIOMES = new BiomePicker(registry);
            registry.stream()
                    .filter(biome -> registry.getResourceKey(biome).isPresent())
                    .map(biome -> registry.getHolderOrThrow(registry.getResourceKey(biome).get()))
                    .map(biome -> biome.unwrapKey().orElseThrow().location())
                    .filter(id -> BiomeAPI.wasRegisteredAs(id, END_CAVE))
                    .map(BiomeAPI::getBiome)
                    .filter(bcl -> !BCLBiomeRegistry.isEmptyBiome(bcl))
                    .forEach(CAVE_BIOMES::addBiome);

            CAVE_BIOMES.rebuild();
            caveBiomeMap = null;
        }

        if (caveBiomeMap == null || lastSeed != seed) {
            caveBiomeMap = new HexBiomeMap(seed, GeneratorOptions.getBiomeSizeCaves(), CAVE_BIOMES);
            lastSeed = seed;
        }
    }

    /**
     * Put integration sub-biome {@link EndBiome} into subbiomes list and registers it.
     *
     * @param biomeConfig - {@link EndBiome.Config} instance
     * @return registered {@link EndBiome}
     */
    public static EndBiome registerSubBiomeIntegration(EndBiome.Config biomeConfig) {
        //TODO: 1.19.3 this was don on runtime, but biomes are now created in DataGen, so we need a fix...
        return EndBiome.create(biomeConfig, BiomeAPI.BiomeType.END_LAND);
    }

    /**
     * Link integration sub-biome with parent.
     *
     * @param biome  - {@link EndBiome} instance
     * @param parent - {@link ResourceLocation} parent id
     */
    public static void addSubBiomeIntegration(EndBiome biome, ResourceLocation parent) {
        if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
            BCLBiome parentBiome = BiomeAPI.getBiome(parent);
            if (!BCLBiomeRegistry.isEmptyBiome(parentBiome) && biome.getParentBiome().getID().equals(biome.getID())) {
                parentBiome.addSubBiome(biome);
            }
        }
    }

    public static BiomePicker.ActualBiome getCaveBiome(int x, int z) {
        return caveBiomeMap.getBiome(x, 5, z);
    }

}
