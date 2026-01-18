package org.betterx.betterend.complexmaterials;

import org.betterx.bclib.api.v3.datagen.RecipeDataProvider;
import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.RecipeHelper;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBlocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

import net.minecraft.world.level.block.state.BlockBehaviour;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Function;

public class ColoredMaterial {
    private static final Map<Integer, ItemLike> DYES = Maps.newHashMap();
    private static final Map<Integer, String> COLORS = Maps.newHashMap();
    private final Map<Integer, Block> colors = Maps.newHashMap();

    public ColoredMaterial(Function<BlockBehaviour.Properties, Block> constructor, Block source, boolean craftEight) {
        this(resolveBaseName(source), constructor, source, COLORS, DYES, craftEight);
    }

    public ColoredMaterial(
            String baseName,
            Function<BlockBehaviour.Properties, Block> constructor,
            Block source,
            boolean craftEight
    ) {
        this(baseName, constructor, source, COLORS, DYES, craftEight);
    }

    public ColoredMaterial(
            Function<BlockBehaviour.Properties, Block> constructor,
            Block source,
            Map<Integer, String> colors,
            Map<Integer, ItemLike> dyes,
            boolean craftEight
    ) {
        this(resolveBaseName(source), constructor, source, colors, dyes, craftEight);
    }

    public ColoredMaterial(
            String baseName,
            Function<BlockBehaviour.Properties, Block> constructor,
            Block source,
            Map<Integer, String> colors,
            Map<Integer, ItemLike> dyes,
            boolean craftEight
    ) {
        String id = requireBaseName(baseName, source);
        colors.forEach((color, name) -> {
            String blockName = id + "_" + name;
            Block block = constructor.apply(BlockBehaviour.Properties.copy(source).mapColor(MapColor.COLOR_BLACK));
            EndBlocks.registerBlock(blockName, block);
            ItemLike dye = dyes.get(color);
            RecipeDataProvider.defer(() -> registerRecipe(blockName, block, source, dye, craftEight));
            this.colors.put(color, block);
            BlocksHelper.addBlockColor(block, color);
        });
    }

    public Block getByColor(DyeColor color) {
        return colors.get(color.getMapColor().col);
    }

    public Block getByColor(int color) {
        return colors.get(color);
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            int colorRGB = color.getMapColor().col;
            COLORS.put(colorRGB, color.getName());
            DYES.put(colorRGB, DyeItem.byColor(color));
        }
    }

    private static void registerRecipe(
            String blockName,
            Block block,
            Block source,
            ItemLike dye,
            boolean craftEight
    ) {
        if (dye == null || !exists(block, source, dye)) {
            return;
        }
        if (craftEight) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(blockName), block)
                            .setOutputCount(8)
                            .setShape("###", "#D#", "###")
                            .addMaterial('#', source)
                            .addMaterial('D', dye)
                            .build();
        } else {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(blockName), block)
                            .setList("#D")
                            .addMaterial('#', source)
                            .addMaterial('D', dye)
                            .build();
        }
    }

    private static boolean exists(ItemLike... items) {
        return RecipeHelper.exists(items);
    }

    private static String resolveBaseName(Block source) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(source);
        if (key == null || key.equals(BuiltInRegistries.BLOCK.getDefaultKey())) {
            throw new IllegalStateException("ColoredMaterial base block is not registered yet: " + source);
        }
        return key.getPath();
    }

    private static String requireBaseName(String baseName, Block source) {
        if (baseName == null || baseName.isBlank()) {
            throw new IllegalArgumentException("ColoredMaterial base name is required for " + source);
        }
        return baseName;
    }
}

