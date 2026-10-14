package org.betterx.datagen.betterend.recipes;

import org.betterx.bclib.api.v3.datagen.RecipeDataProvider;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.recipe.*;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.util.List;

public class EndRecipeDataProvider extends RecipeDataProvider {
    public EndRecipeDataProvider(FabricDataOutput output) {
        super(List.of(BetterEnd.MOD_ID), output);
    }

    public static void buildRecipes() {
        CraftingRecipes.register();
        FurnaceRecipes.register();
        AlloyingRecipes.register();
        AnvilRecipes.register();
        SmithingRecipes.register();
        InfusionRecipes.register();
    }
}
