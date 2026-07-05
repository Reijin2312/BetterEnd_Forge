package org.betterx.betterend.integration;

import org.betterx.bclib.api.v2.ModIntegrationAPI;
import org.betterx.bclib.integration.ModIntegration;
import org.betterx.betterend.integration.byg.BYGIntegration;
import org.betterx.betterend.integration.elytraslot.ElytraSlotCompat;

import net.minecraftforge.fml.ModList;

public class Integrations {
    public static final ModIntegration BYG = ModIntegrationAPI.register(new BYGIntegration());
    public static final ModIntegration NOURISH = ModIntegrationAPI.register(new NourishIntegration());
    public static final ModIntegration FLAMBOYANT_REFABRICATED = ModIntegrationAPI.register(new FlamboyantRefabricatedIntegration());

    private static boolean hasHydrogen;

    public static void init() {
        hasHydrogen = ModList.get().isLoaded("hydrogen");
        ElytraSlotCompat.init();
    }

    public static boolean hasHydrogen() {
        return hasHydrogen;
    }
}
