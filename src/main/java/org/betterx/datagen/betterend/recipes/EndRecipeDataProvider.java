package org.betterx.datagen.betterend.recipes;

import org.betterx.bclib.api.v3.datagen.RecipeDataProvider;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.recipe.*;

import net.minecraft.data.PackOutput;

import java.util.List;

public class EndRecipeDataProvider extends RecipeDataProvider {
    public EndRecipeDataProvider(PackOutput output) {
        super(List.of(BetterEnd.MOD_ID), output);
    }

    public static void buildRecipes() {
        RecipeDataProvider.defer(EndRecipeDataProvider::registerRecipes);
    }

    private static void registerRecipes() {
        CraftingRecipes.register();
        FurnaceRecipes.register();
        AlloyingRecipes.register();
        AnvilRecipes.register();
        SmithingRecipes.register();
        InfusionRecipes.register();
    }
}
