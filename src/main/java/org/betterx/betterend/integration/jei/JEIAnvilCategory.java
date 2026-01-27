package org.betterx.betterend.integration.jei;

import org.betterx.bclib.recipes.AnvilRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeIngredientRole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class JEIAnvilCategory implements IRecipeCategory<AnvilRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public JEIAnvilCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(104, 26);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.ANVIL));
    }

    @Override
    public RecipeType<AnvilRecipe> getRecipeType() {
        return JEIPlugin.ANVIL_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.anvil");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AnvilRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> mainInputs = toCountedStacks(recipe.getMainIngredient(), recipe.getInputCount());
        if (!mainInputs.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 0, 4).addItemStacks(mainInputs);
        }

        List<ItemStack> hammers = getHammerStacks(recipe);
        if (!hammers.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 20, 4).addItemStacks(hammers);
        }

        ItemStack result = ItemStack.EMPTY;
        if (Minecraft.getInstance().level != null) {
            result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
        }
        if (!result.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 78, 0).addItemStack(result);
        }
    }

    private static List<ItemStack> toCountedStacks(Ingredient ingredient, int count) {
        ItemStack[] stacks = ingredient.getItems();
        List<ItemStack> out = new ArrayList<>(stacks.length);
        for (ItemStack stack : stacks) {
            ItemStack copy = stack.copy();
            copy.setCount(count);
            out.add(copy);
        }
        return out;
    }

    private static List<ItemStack> getHammerStacks(AnvilRecipe recipe) {
        return StreamSupport.stream(AnvilRecipe.getAllHammers().spliterator(), false)
                            .map(Holder::value)
                            .filter(recipe::canUse)
                            .map(ItemStack::new)
                            .toList();
    }
}
