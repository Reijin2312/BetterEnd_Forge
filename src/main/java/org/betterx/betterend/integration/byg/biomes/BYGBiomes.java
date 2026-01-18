package org.betterx.betterend.integration.byg.biomes;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.integration.Integrations;
import org.betterx.betterend.registry.EndBiomes;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.world.level.levelgen.SurfaceRules;

public class BYGBiomes {
    public static final SurfaceRules.ConditionSource BYG_WATER_CHECK = SurfaceRules.waterBlockCheck(-1, 0);
    // New Biomes
    public static final EndBiome OLD_BULBIS_GARDENS = EndBiomes.registerSubBiomeIntegration(new OldBulbisGardens());
    public static final EndBiome NIGHTSHADE_REDWOODS = EndBiomes.registerSubBiomeIntegration(new NightshadeRedwoods());
    //public static final EndBiome ETHERIAL_GROVE = EndBiomes.registerSubBiomeIntegration(new EterialGrove());

    public static void register() {
        BetterEnd.LOGGER.info("Registered " + OLD_BULBIS_GARDENS);
    }

    public static void addBiomes() {
        EndBiomes.addSubBiomeIntegration(OLD_BULBIS_GARDENS, Integrations.BYG.getID("bulbis_gardens"));
        EndBiomes.addSubBiomeIntegration(NIGHTSHADE_REDWOODS, Integrations.BYG.getID("nightshade_forest"));
        //EndBiomes.addSubBiomeIntegration(ETHERIAL_GROVE, Integrations.BYG.getID("ethereal_islands"));
    }
}
