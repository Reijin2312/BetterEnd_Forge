package org.betterx.betterend.client;

import org.betterx.betterend.config.Configs;

public class ClientOptions {
    private static boolean initialized;
    private static boolean customSky;
    private static boolean blendBiomeMusic;
    private static boolean sulfurWaterColor;

    public static void init() {
        if (initialized) {
            return;
        }
        customSky = Configs.CLENT_CONFIG.getBooleanRoot("customSky", true);
        blendBiomeMusic = Configs.CLENT_CONFIG.getBooleanRoot("blendBiomeMusic", true);
        sulfurWaterColor = Configs.CLENT_CONFIG.getBooleanRoot("sulfurWaterColor", true);
        Configs.CLENT_CONFIG.saveChanges();
        initialized = true;
    }

    private static void ensureInit() {
        if (!initialized) {
            init();
        }
    }

    public static boolean isCustomSky() {
        ensureInit();
        return customSky;
    }

    public static void setCustomSky(boolean customSky) {
        ensureInit();
        ClientOptions.customSky = customSky;
    }

    public static boolean blendBiomeMusic() {
        ensureInit();
        return blendBiomeMusic;
    }

    public static void setBlendBiomeMusic(boolean blendBiomeMusic) {
        ensureInit();
        ClientOptions.blendBiomeMusic = blendBiomeMusic;
    }

    public static boolean useSulfurWaterColor() {
        ensureInit();
        return sulfurWaterColor;
    }

    public static void setSulfurWaterColor(boolean sulfurWaterColor) {
        ensureInit();
        ClientOptions.sulfurWaterColor = sulfurWaterColor;
    }
}
