package org.betterx.betterend.complexmaterials;

import org.betterx.bclib.blocks.BaseSlabBlock;
import org.betterx.bclib.blocks.BaseStairsBlock;
import org.betterx.bclib.blocks.BaseWallBlock;
import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.EndPedestal;
import org.betterx.betterend.blocks.basis.LitBaseBlock;
import org.betterx.betterend.blocks.basis.LitPillarBlock;
import org.betterx.betterend.recipe.CraftingRecipes;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class CrystalSubblocksMaterial {
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
        FabricBlockSettings material = FabricBlockSettings.copyOf(source);
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

        // Recipes //
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bricks"), bricks)
                        .setOutputCount(4)
                        .setShape("##", "##")
                        .addMaterial('#', source)
                        .setGroup("end_bricks")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_polished"), polished)
                        .setOutputCount(4)
                        .setShape("##", "##")
                        .addMaterial('#', bricks)
                        .setGroup("end_tile")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_tiles"), tiles)
                        .setOutputCount(4)
                        .setShape("##", "##")
                        .addMaterial('#', polished)
                        .setGroup("end_small_tile")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_pillar"), pillar)
                        .setShape("#", "#")
                        .addMaterial('#', slab)
                        .setGroup("end_pillar")
                        .build();

        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_stairs"), stairs)
                        .setOutputCount(4)
                        .setShape("#  ", "## ", "###")
                        .addMaterial('#', source)
                        .setGroup("end_stone_stairs")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_slab"), slab)
                        .setOutputCount(6)
                        .setShape("###")
                        .addMaterial('#', source)
                        .setGroup("end_stone_slabs")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bricks_stairs"), brick_stairs)
                        .setOutputCount(4)
                        .setShape("#  ", "## ", "###")
                        .addMaterial('#', bricks)
                        .setGroup("end_stone_stairs")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bricks_slab"), brick_slab)
                        .setOutputCount(6)
                        .setShape("###")
                        .addMaterial('#', bricks)
                        .setGroup("end_stone_slabs")
                        .build();

        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_wall"), wall)
                        .setOutputCount(6)
                        .setShape("###", "###")
                        .addMaterial('#', source)
                        .setGroup("end_wall")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bricks_wall"), brick_wall)
                        .setOutputCount(6)
                        .setShape("###", "###")
                        .addMaterial('#', bricks)
                        .setGroup("end_wall")
                        .build();

        CraftingRecipes.registerPedestal(name + "_pedestal", pedestal, slab, pillar);

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
}