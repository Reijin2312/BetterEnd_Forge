package org.betterx.betterend.config;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.FeatureConfigAPI;
import org.betterx.bclib.config.EntryConfig;
import org.betterx.bclib.config.IdConfig;
import org.betterx.bclib.config.PathConfig;
import org.betterx.betterend.BetterEnd;

import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Configs {
    public static final PathConfig ENTITY_CONFIG = new PathConfig(BetterEnd.MOD_ID, "entities");
    public static final PathConfig BLOCK_CONFIG = new PathConfig(BetterEnd.MOD_ID, "blocks");
    public static final PathConfig ITEM_CONFIG = new PathConfig(BetterEnd.MOD_ID, "items");
    public static final IdConfig BIOME_CONFIG = new EntryConfig(BetterEnd.MOD_ID, "biomes");
    public static final PathConfig GENERATOR_CONFIG = new PathConfig(BetterEnd.MOD_ID, "generator", false);
    public static final PathConfig STRUCTURE_CONFIG = new PathConfig(BetterEnd.MOD_ID, "structures");
    public static final PathConfig FEATURE_CONFIG = new PathConfig(BetterEnd.MOD_ID, "features");
    public static final PathConfig RECIPE_CONFIG = new PathConfig(BetterEnd.MOD_ID, "recipes");
    public static final PathConfig ENCHANTMENT_CONFIG = new PathConfig(BetterEnd.MOD_ID, "enchantments");

    public static final PathConfig CLENT_CONFIG = new PathConfig(BetterEnd.MOD_ID, "client", false);

    public static void saveConfigs() {
        ENTITY_CONFIG.saveChanges();
        BLOCK_CONFIG.saveChanges();
        BIOME_CONFIG.saveChanges();
        ITEM_CONFIG.saveChanges();
        GENERATOR_CONFIG.saveChanges();
        STRUCTURE_CONFIG.saveChanges();
        FEATURE_CONFIG.saveChanges();
        RECIPE_CONFIG.saveChanges();
        ENCHANTMENT_CONFIG.saveChanges();

        if (BCLib.isClient()) {
            CLENT_CONFIG.saveChanges();
        }
    }

    public static boolean isStructureEnabled(ResourceLocation id) {
        return !BetterEnd.MOD_ID.equals(id.getNamespace()) || STRUCTURE_CONFIG.getBooleanRoot(id.getPath(), true);
    }

    public static boolean isFeatureEnabled(ResourceLocation id) {
        return !BetterEnd.MOD_ID.equals(id.getNamespace()) || FEATURE_CONFIG.getBooleanRoot(id.getPath(), true);
    }

    public static void registerFeatureConfigEntries(Class<?>... sources) {
        for (Class<?> source : sources) {
            for (Field field : source.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && BCLFeature.class.isAssignableFrom(field.getType())) {
                    registerFeatureConfigEntry(field);
                }
            }
        }
        FEATURE_CONFIG.saveChanges();
    }

    private static void registerFeatureConfigEntry(Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(null);
            if (value instanceof BCLFeature<?, ?> feature) {
                FeatureConfigAPI.registerFeature(feature);
                feature.getPlacedFeature().unwrapKey().ifPresent(key -> isFeatureEnabled(key.location()));
            }
        } catch (IllegalAccessException ex) {
            BetterEnd.LOGGER.warning("Failed to register feature config entry for " + field.getName());
        }
    }
}
