package org.betterx.betterend.complexmaterials;

import org.betterx.bclib.api.v3.datagen.RecipeDataProvider;
import org.betterx.bclib.blocks.BaseSlabBlock;
import org.betterx.bclib.blocks.BaseStairsBlock;
import org.betterx.bclib.blocks.BaseWallBlock;
import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.bclib.util.RecipeHelper;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.EndPedestal;
import org.betterx.betterend.blocks.basis.LitBaseBlock;
import org.betterx.betterend.blocks.basis.LitPillarBlock;
import org.betterx.betterend.recipe.CraftingRecipes;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class CrystalSubblocksMaterial {
    private final String baseName;
    private final Block source;
    public final Block polished;
    public final Block tiles;
    public final Block pillar;
    public final Block stairs;
    public final Block slab;
    public final Block wall;
    public final Block pedestal;
    public final Block bricks;
    public final Block brick_stairs;
    public final Block brick_slab;
    public final Block brick_wall;

    public CrystalSubblocksMaterial(String name, Block source) {
        BlockBehaviour.Properties material = BlockBehaviour.Properties.copy(source);
        this.baseName = name;
        this.source = source;
        polished = EndBlocks.registerBlock(name + "_polished", new LitBaseBlock(material));
        tiles = EndBlocks.registerBlock(name + "_tiles", new LitBaseBlock(material));
        pillar = EndBlocks.registerBlock(name + "_pillar", new LitPillarBlock(material));
        stairs = EndBlocks.registerBlock(name + "_stairs", new BaseStairsBlock.Stone(source));
        slab = EndBlocks.registerBlock(name + "_slab", new BaseSlabBlock.Stone(source));
        wall = EndBlocks.registerBlock(name + "_wall", new BaseWallBlock.Stone(source));
        pedestal = EndBlocks.registerBlock(name + "_pedestal", new EndPedestal.Stone(source));
        bricks = EndBlocks.registerBlock(name + "_bricks", new LitBaseBlock(material));
        brick_stairs = EndBlocks.registerBlock(name + "_bricks_stairs", new BaseStairsBlock.Stone(bricks));
        brick_slab = EndBlocks.registerBlock(name + "_bricks_slab", new BaseSlabBlock.Stone(bricks));
        brick_wall = EndBlocks.registerBlock(name + "_bricks_wall", new BaseWallBlock.Stone(bricks));

        RecipeDataProvider.defer(this::registerRecipes);

        // Item Tags //
        TagManager.ITEMS.add(ItemTags.SLABS, slab.asItem(), brick_slab.asItem());
        TagManager.ITEMS.add(ItemTags.STONE_BRICKS, bricks.asItem());
        TagManager.ITEMS.add(ItemTags.STONE_CRAFTING_MATERIALS, source.asItem());
        TagManager.ITEMS.add(ItemTags.STONE_TOOL_MATERIALS, source.asItem());

        // Block Tags //
        TagManager.BLOCKS.add(BlockTags.STONE_BRICKS, bricks);
        TagManager.BLOCKS.add(BlockTags.WALLS, wall, brick_wall);
        TagManager.BLOCKS.add(BlockTags.SLABS, slab, brick_slab);
    }

    private void registerRecipes() {
        if (exists(bricks, source)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_bricks"), bricks)
                            .setOutputCount(4)
                            .setShape("##", "##")
                            .addMaterial('#', source)
                            .setGroup("end_bricks")
                            .build();
        }
        if (exists(polished, bricks)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_polished"), polished)
                            .setOutputCount(4)
                            .setShape("##", "##")
                            .addMaterial('#', bricks)
                            .setGroup("end_tile")
                            .build();
        }
        if (exists(tiles, polished)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_tiles"), tiles)
                            .setOutputCount(4)
                            .setShape("##", "##")
                            .addMaterial('#', polished)
                            .setGroup("end_small_tile")
                            .build();
        }
        if (exists(pillar, slab)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_pillar"), pillar)
                            .setShape("#", "#")
                            .addMaterial('#', slab)
                            .setGroup("end_pillar")
                            .build();
        }

        if (exists(stairs, source)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_stairs"), stairs)
                            .setOutputCount(4)
                            .setShape("#  ", "## ", "###")
                            .addMaterial('#', source)
                            .setGroup("end_stone_stairs")
                            .build();
        }
        if (exists(slab, source)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_slab"), slab)
                            .setOutputCount(6)
                            .setShape("###")
                            .addMaterial('#', source)
                            .setGroup("end_stone_slabs")
                            .build();
        }
        if (exists(brick_stairs, bricks)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_bricks_stairs"), brick_stairs)
                            .setOutputCount(4)
                            .setShape("#  ", "## ", "###")
                            .addMaterial('#', bricks)
                            .setGroup("end_stone_stairs")
                            .build();
        }
        if (exists(brick_slab, bricks)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_bricks_slab"), brick_slab)
                            .setOutputCount(6)
                            .setShape("###")
                            .addMaterial('#', bricks)
                            .setGroup("end_stone_slabs")
                            .build();
        }

        if (exists(wall, source)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_wall"), wall)
                            .setOutputCount(6)
                            .setShape("###", "###")
                            .addMaterial('#', source)
                            .setGroup("end_wall")
                            .build();
        }
        if (exists(brick_wall, bricks)) {
            BCLRecipeBuilder.crafting(BetterEnd.makeID(baseName + "_bricks_wall"), brick_wall)
                            .setOutputCount(6)
                            .setShape("###", "###")
                            .addMaterial('#', bricks)
                            .setGroup("end_wall")
                            .build();
        }

        if (exists(pedestal, slab, pillar)) {
            CraftingRecipes.registerPedestal(baseName + "_pedestal", pedestal, slab, pillar);
        }
    }

    private static boolean exists(ItemLike... items) {
        return RecipeHelper.exists(items);
    }
}
