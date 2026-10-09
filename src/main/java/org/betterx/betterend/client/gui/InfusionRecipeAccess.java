package org.betterx.betterend.client.gui;

import org.betterx.betterend.recipe.builders.InfusionRecipe;

import net.minecraft.world.level.Level;

import java.util.Collection;

public final class InfusionRecipeAccess {
    private InfusionRecipeAccess() {}

    public static Collection<InfusionRecipe> all(Level level) {
        return level.getRecipeManager().getAllRecipesFor(InfusionRecipe.TYPE);
    }
}
