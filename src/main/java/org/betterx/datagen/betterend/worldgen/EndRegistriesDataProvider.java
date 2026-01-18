package org.betterx.datagen.betterend.worldgen;

import org.betterx.bclib.api.v3.datagen.RegistriesDataProvider;
import org.betterx.betterend.BetterEnd;
import org.betterx.datagen.betterend.EndRegistrySupplier;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class EndRegistriesDataProvider extends RegistriesDataProvider {
    public EndRegistriesDataProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(BetterEnd.LOGGER, EndRegistrySupplier.INSTANCE, output, registriesFuture);
    }
}
