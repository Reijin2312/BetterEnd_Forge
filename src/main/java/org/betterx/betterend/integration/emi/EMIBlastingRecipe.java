package org.betterx.betterend.integration.emi;

import org.betterx.bclib.integration.emi.EMIAbstractAlloyingRecipe;
import org.betterx.bclib.integration.emi.EMIPlugin;
import org.betterx.betterend.BetterEnd;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import dev.emi.emi.api.EmiRegistry;

public class EMIBlastingRecipe extends EMIAbstractAlloyingRecipe<Container, BlastingRecipe> {
    public EMIBlastingRecipe(BlastingRecipe recipe) {
        super(recipe, new ResourceLocation(
                "emi",
                recipe.getId().getNamespace() + "/" + recipe.getId().getPath() + "/allloying"
        ), 1, false);
    }

    @Override
    protected int getSmeltTime() {
        return recipe.getCookingTime();
    }

    @Override
    protected float getExperience() {
        return recipe.getExperience();
    }


    static void addAllRecipes(EmiRegistry emiRegistry, RecipeManager manager) {
        EMIPlugin.addAllRecipes(
                emiRegistry, manager, BetterEnd.LOGGER,
                RecipeType.BLASTING, EMIBlastingRecipe::new
        );
    }
}
