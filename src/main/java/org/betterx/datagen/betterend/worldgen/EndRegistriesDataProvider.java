package org.betterx.datagen.betterend.worldgen;

import org.betterx.bclib.api.v3.datagen.RegistriesDataProvider;
import org.betterx.betterend.BetterEnd;
import org.betterx.datagen.betterend.EndRegistrySupplier;

import net.minecraft.core.HolderLookup;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.util.concurrent.CompletableFuture;

public class EndRegistriesDataProvider extends RegistriesDataProvider {
    public EndRegistriesDataProvider(
            FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(BetterEnd.LOGGER, EndRegistrySupplier.INSTANCE, output, registriesFuture);
    }
}
