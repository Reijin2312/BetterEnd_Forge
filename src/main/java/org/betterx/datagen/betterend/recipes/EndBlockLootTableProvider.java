package org.betterx.datagen.betterend.recipes;

import org.betterx.bclib.api.v3.datagen.BlockLootTableProvider;
import org.betterx.betterend.BetterEnd;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.util.List;

public class EndBlockLootTableProvider extends BlockLootTableProvider {
    public EndBlockLootTableProvider(
            FabricDataOutput output
    ) {
        super(output, List.of(BetterEnd.MOD_ID));
    }
}
