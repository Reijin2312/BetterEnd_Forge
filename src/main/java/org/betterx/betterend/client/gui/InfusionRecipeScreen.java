package org.betterx.betterend.client.gui;

import org.betterx.betterend.recipe.builders.InfusionRecipe;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InfusionRecipeScreen extends Screen {
    private static final int PANEL_WIDTH = 260;
    private static final int PANEL_HEIGHT = 184;
    private static final int SLOT_SIZE = 18;
    private static final int RADIUS = 48;

    private final List<InfusionRecipe> recipes = new ArrayList<>();
    private int selected;
    private int left;
    private int top;

    public InfusionRecipeScreen() {
        super(Component.translatable(EndBlocks.INFUSION_PEDESTAL.getDescriptionId()));
    }

    @Override
    protected void init() {
        recipes.clear();
        if (minecraft != null && minecraft.level != null) {
            recipes.addAll(InfusionRecipeAccess.all(minecraft.level));
            recipes.sort(Comparator.comparing(recipe -> output(recipe).getHoverName().getString()));
        }

        left = (width - PANEL_WIDTH) / 2;
        top = (height - PANEL_HEIGHT) / 2;
        addRenderableWidget(Button.builder(Component.literal("<"), button -> select(-1))
                                  .bounds(left + 8, top + PANEL_HEIGHT - 28, 32, 20)
                                  .build());
        addRenderableWidget(Button.builder(Component.literal(">"), button -> select(1))
                                  .bounds(left + PANEL_WIDTH - 40, top + PANEL_HEIGHT - 28, 32, 20)
                                  .build());
    }

    private void select(int direction) {
        if (!recipes.isEmpty()) selected = Math.floorMod(selected + direction, recipes.size());
    }

    private ItemStack output(InfusionRecipe recipe) {
        return recipe.getResultItem(minecraft.level.registryAccess()).copy();
    }

    private ItemStack ingredientStack(Ingredient ingredient) {
        if (ingredient == null || ingredient.isEmpty()) return ItemStack.EMPTY;
        ItemStack[] items = ingredient.getItems();
        if (items.length == 0) return ItemStack.EMPTY;
        long tick = minecraft == null || minecraft.level == null ? 0 : minecraft.level.getGameTime();
        return items[(int) ((tick / 20) % items.length)].copy();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, width, height, 0xB0000000);
        graphics.fill(left - 3, top - 3, left + PANEL_WIDTH + 3, top + PANEL_HEIGHT + 3, 0xFF5A2A1C);
        graphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xFFFFF9EC);
        graphics.drawString(font, title, left + 8, top + 8, 0x404040, false);

        if (recipes.isEmpty()) {
            graphics.drawString(font, Component.translatable("gui.betterend.infusion.no_recipes"),
                    left + 8, top + 30, 0x8A7A5C, false);
            super.render(graphics, mouseX, mouseY, partialTick);
            return;
        }

        InfusionRecipe recipe = recipes.get(selected);
        ItemStack result = output(recipe);
        int centerX = left + 112;
        int centerY = top + 92;

        List<Ingredient> ingredients = recipe.getIngredients();
        drawSlot(graphics, centerX, centerY,
                ingredients.isEmpty() ? ItemStack.EMPTY : ingredientStack(ingredients.get(0)), mouseX, mouseY);
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4.0;
            int x = centerX + (int) Math.round(Math.sin(angle) * RADIUS);
            int y = centerY - (int) Math.round(Math.cos(angle) * RADIUS);
            ItemStack stack = ingredients.size() > i + 1 ? ingredientStack(ingredients.get(i + 1)) : ItemStack.EMPTY;
            drawSlot(graphics, x, y, stack, mouseX, mouseY);
        }

        drawSlot(graphics, left + 218, centerY, result, mouseX, mouseY);
        graphics.drawString(font, result.getHoverName(), left + 150, top + 28, 0x404040, false);
        graphics.drawCenteredString(font, Component.literal((selected + 1) + " / " + recipes.size()),
                left + PANEL_WIDTH / 2, top + PANEL_HEIGHT - 22, 0x8A7A5C);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawSlot(GuiGraphics graphics, int centerX, int centerY, ItemStack stack, int mouseX, int mouseY) {
        int x = centerX - SLOT_SIZE / 2;
        int y = centerY - SLOT_SIZE / 2;
        graphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, 0xFF8B795E);
        graphics.fill(x + 1, y + 1, x + SLOT_SIZE - 1, y + SLOT_SIZE - 1, 0xFFEFE2C4);
        if (stack.isEmpty()) return;
        graphics.renderItem(stack, x + 1, y + 1);
        graphics.renderItemDecorations(font, stack, x + 1, y + 1);
        if (mouseX >= x && mouseX < x + SLOT_SIZE && mouseY >= y && mouseY < y + SLOT_SIZE) {
            graphics.renderTooltip(font, stack, mouseX, mouseY);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
