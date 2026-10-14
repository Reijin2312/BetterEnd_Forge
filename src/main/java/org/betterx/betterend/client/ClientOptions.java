package org.betterx.betterend.client;

import org.betterx.betterend.config.Configs;

public class ClientOptions {
    private static boolean customSky;
    private static boolean blendBiomeMusic;
    private static boolean sulfurWaterColor;

    public static void init() {
        customSky = Configs.CLENT_CONFIG.getBooleanRoot("customSky", true);
        blendBiomeMusic = Configs.CLENT_CONFIG.getBooleanRoot("blendBiomeMusic", true);
        sulfurWaterColor = Configs.CLENT_CONFIG.getBooleanRoot("sulfurWaterColor", true);
        Configs.CLENT_CONFIG.saveChanges();
    }

    public static boolean isCustomSky() {
        return customSky;
    }

    public static void setCustomSky(boolean customSky) {
        ClientOptions.customSky = customSky;
    }

    public static boolean blendBiomeMusic() {
        return blendBiomeMusic;
    }

    public static void setBlendBiomeMusic(boolean blendBiomeMusic) {
        ClientOptions.blendBiomeMusic = blendBiomeMusic;
    }

    public static boolean useSulfurWaterColor() {
        return sulfurWaterColor;
    }

    public static void setSulfurWaterColor(boolean sulfurWaterColor) {
        ClientOptions.sulfurWaterColor = sulfurWaterColor;
    }
}
