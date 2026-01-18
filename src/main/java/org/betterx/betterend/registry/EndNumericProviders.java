package org.betterx.betterend.registry;

import org.betterx.bclib.interfaces.NumericProvider;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.world.surface.SplitNoiseCondition;
import org.betterx.betterend.world.surface.SulphuricSurfaceNoiseCondition;
import org.betterx.betterend.world.surface.UmbraSurfaceNoiseCondition;
import org.betterx.betterend.world.surface.VerticalBandNoiseCondition;

import net.minecraft.core.Registry;

public class EndNumericProviders {
    private static boolean registered;

    public static void register() {
        if (registered) {
            return;
        }
        Registry.register(
                NumericProvider.NUMERIC_PROVIDER,
                BetterEnd.makeID("sulphuric_surf"),
                SulphuricSurfaceNoiseCondition.CODEC
        );

        Registry.register(
                NumericProvider.NUMERIC_PROVIDER,
                BetterEnd.makeID("vertical_band"),
                VerticalBandNoiseCondition.CODEC
        );

        Registry.register(
                NumericProvider.NUMERIC_PROVIDER,
                BetterEnd.makeID("split_noise"),
                SplitNoiseCondition.CODEC
        );

        Registry.register(
                NumericProvider.NUMERIC_PROVIDER,
                BetterEnd.makeID("umbra_srf"),
                UmbraSurfaceNoiseCondition.CODEC
        );
        registered = true;
    }
}
