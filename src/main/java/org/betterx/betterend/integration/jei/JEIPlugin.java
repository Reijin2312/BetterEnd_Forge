package org.betterx.betterend.integration.jei;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.basis.EndAnvilBlock;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.bclib.recipes.AnvilRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static final RecipeType<AnvilRecipe> ANVIL_RECIPE_TYPE = RecipeType.create(
            BetterEnd.MOD_ID,
            "anvil",
            AnvilRecipe.class
    );

    @Override
    public ResourceLocation getPluginUid() {
        return BetterEnd.makeID("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIAnvilCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        RecipeManager manager = minecraft.level.getRecipeManager();
        registration.addRecipes(ANVIL_RECIPE_TYPE, manager.getAllRecipesFor(AnvilRecipe.TYPE));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        List<ItemStack> anvils = EndBlocks.getModBlocks()
                                          .stream()
                                          .filter(EndAnvilBlock.class::isInstance)
                                          .map(Block::asItem)
                                          .filter(item -> BuiltInRegistries.ITEM.getKey(item) != BuiltInRegistries.ITEM.getDefaultKey())
                                          .map(ItemStack::new)
                                          .collect(Collectors.toList());
        anvils.add(0, new ItemStack(Blocks.ANVIL));
        for (ItemStack stack : anvils) {
            registration.addRecipeCatalyst(stack, ANVIL_RECIPE_TYPE);
        }
    }
}
