package org.betterx.betterend.complexmaterials;

import org.betterx.bclib.blocks.*;
import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.EndPedestal;
import org.betterx.betterend.blocks.FlowerPotBlock;
import org.betterx.betterend.blocks.basis.StoneLanternBlock;
import org.betterx.betterend.recipe.CraftingRecipes;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;
import org.betterx.worlds.together.tag.v3.CommonItemTags;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class StoneMaterial {
    public final Block stone;
    public final String baseName;

    public final Block polished;
    public final Block tiles;
    public final Block pillar;
    public final Block stairs;
    public final Block slab;
    public final Block wall;
    public final Block button;
    public final Block pressurePlate;
    public final Block pedestal;
    public final Block lantern;

    public final Block bricks;
    public final Block brickStairs;
    public final Block brickSlab;
    public final Block brickWall;
    public final Block furnace;
    public final Block flowerPot;

    public static FabricBlockSettings createMaterial(MapColor color) {
        return FabricBlockSettings.copyOf(Blocks.END_STONE).mapColor(color);
    }

    public StoneMaterial(String name, MapColor color) {
        FabricBlockSettings material = createMaterial(color);

        this.baseName = name;
        stone = EndBlocks.registerBlock(name, new BaseBlock.Stone(material));
        polished = EndBlocks.registerBlock(name + "_polished", new BaseBlock.Stone(material));
        tiles = EndBlocks.registerBlock(name + "_tiles", new BaseBlock.Stone(material));
        pillar = EndBlocks.registerBlock(name + "_pillar", new BaseRotatedPillarBlock.Stone(material));
        stairs = EndBlocks.registerBlock(name + "_stairs", new BaseStairsBlock.Stone(stone));
        slab = EndBlocks.registerBlock(name + "_slab", new BaseSlabBlock.Stone(stone));
        wall = EndBlocks.registerBlock(name + "_wall", new BaseWallBlock.Stone(stone));
        button = EndBlocks.registerBlock(name + "_button", new BaseButtonBlock.Stone(stone, BlockSetType.STONE));
        pressurePlate = EndBlocks.registerBlock(
                name + "_plate",
                new BasePressurePlateBlock.Stone(stone, BlockSetType.STONE)
        );
        pedestal = EndBlocks.registerBlock(name + "_pedestal", new EndPedestal.Stone(stone));
        lantern = EndBlocks.registerBlock(name + "_lantern", new StoneLanternBlock(stone));

        bricks = EndBlocks.registerBlock(name + "_bricks", new BaseBlock.Stone(material));
        brickStairs = EndBlocks.registerBlock(name + "_bricks_stairs", new BaseStairsBlock.Stone(bricks));
        brickSlab = EndBlocks.registerBlock(name + "_bricks_slab", new BaseSlabBlock.Stone(bricks));
        brickWall = EndBlocks.registerBlock(name + "_bricks_wall", new BaseWallBlock.Stone(bricks));
        furnace = EndBlocks.registerBlock(name + "_furnace", new BaseFurnaceBlock.Stone(bricks));
        flowerPot = EndBlocks.registerBlock(name + "_flower_pot", new FlowerPotBlock.Stone(bricks));

        // Recipes //
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bricks"), bricks)
                        .setOutputCount(4)
                        .setShape("##", "##")
                        .addMaterial('#', stone)
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
                        .addMaterial('#', stone)
                        .setGroup("end_stone_stairs")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_slab"), slab)
                        .setOutputCount(6)
                        .setShape("###")
                        .addMaterial('#', stone)
                        .setGroup("end_stone_slabs")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bricks_stairs"), brickStairs)
                        .setOutputCount(4)
                        .setShape("#  ", "## ", "###")
                        .addMaterial('#', bricks)
                        .setGroup("end_stone_stairs")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bricks_slab"), brickSlab)
                        .setOutputCount(6)
                        .setShape("###")
                        .addMaterial('#', bricks)
                        .setGroup("end_stone_slabs")
                        .build();

        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_wall"), wall)
                        .setOutputCount(6)
                        .setShape("###", "###")
                        .addMaterial('#', stone)
                        .setGroup("end_wall")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bricks_wall"), brickWall)
                        .setOutputCount(6)
                        .setShape("###", "###")
                        .addMaterial('#', bricks)
                        .setGroup("end_wall")
                        .build();

        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_button"), button)
                        .shapeless()
                        .addMaterial('#', stone)
                        .setGroup("end_stone_buttons")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_pressure_plate"), pressurePlate)
                        .setShape("##")
                        .addMaterial('#', stone)
                        .setGroup("end_stone_plates")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_lantern"), lantern)
                        .setShape("S", "#", "S")
                        .addMaterial('#', EndItems.CRYSTAL_SHARDS)
                        .addMaterial('S', slab, brickSlab)
                        .setGroup("end_stone_lanterns")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_furnace"), furnace)
                        .setShape("###", "# #", "###")
                        .addMaterial('#', stone)
                        .setGroup("end_stone_ITEM_FURNACES")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_flower_pot"), flowerPot)
                        .setOutputCount(3)
                        .setShape("# #", " # ")
                        .addMaterial('#', bricks)
                        .setGroup("end_pots")
                        .build();

        CraftingRecipes.registerPedestal(name + "_pedestal", pedestal, slab, pillar);
        StoneMaterial.recipesForStoneMaterial(this);

        // Item Tags //
        TagManager.ITEMS.add(ItemTags.SLABS, slab.asItem(), brickSlab.asItem());
        TagManager.ITEMS.add(ItemTags.STONE_BRICKS, bricks.asItem());
        TagManager.ITEMS.add(ItemTags.STONE_CRAFTING_MATERIALS, stone.asItem());
        TagManager.ITEMS.add(ItemTags.STONE_TOOL_MATERIALS, stone.asItem());
        TagManager.ITEMS.add(CommonItemTags.FURNACES, furnace.asItem());

        // Block Tags //
        TagManager.BLOCKS.add(BlockTags.STONE_BRICKS, bricks);
        TagManager.BLOCKS.add(BlockTags.WALLS, wall, brickWall);
        TagManager.BLOCKS.add(BlockTags.SLABS, slab, brickSlab);
        TagManager.BLOCKS.add(pressurePlate, BlockTags.PRESSURE_PLATES, BlockTags.STONE_PRESSURE_PLATES);
        TagManager.BLOCKS.add(CommonBlockTags.END_STONES, stone);

        TagManager.BLOCKS.add(BlockTags.DRAGON_IMMUNE, stone, stairs, slab, wall);

        TagManager.BLOCKS.add(CommonBlockTags.END_STONES, stone);
        TagManager.BLOCKS.add(CommonBlockTags.END_STONES, stone);
    }

    public static void recipesForStoneMaterial(StoneMaterial mat) {
        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_bricks_stonecutting"),
                        mat.bricks
                )
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_wall_stonecutting"),
                        mat.wall
                )
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_slab_stonecutting"),
                        mat.slab
                )
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_stairs_stonecutting"),
                        mat.stairs
                )
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_tiles_stonecutting"),
                        mat.tiles
                )
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_brick_slab_stonecutting"),
                        mat.brickSlab
                )
                .setOutputCount(2)
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_brick_stair_stonecutting"),
                        mat.brickStairs
                )
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_brick_wall_stonecutting"),
                        mat.brickWall
                )
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_pillar_stonecutting"),
                        mat.pillar
                )
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_polished_stonecutting"),
                        mat.polished
                )
                .setPrimaryInputAndUnlock(mat.stone)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_brick_slabs_from_" + mat.baseName + "_brick_stonecutting"),
                        mat.brickSlab
                )
                .setOutputCount(2)
                .setPrimaryInputAndUnlock(mat.bricks)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_brick_stair_from_" + mat.baseName + "_brick_stonecutting"),
                        mat.brickStairs
                )
                .setPrimaryInputAndUnlock(mat.bricks)
                .setGroup(mat.baseName + "_stonecutting")
                .build();

        BCLRecipeBuilder
                .stonecutting(
                        BetterEnd.makeID(mat.baseName + "_brick_wall_from_" + mat.baseName + "_brick_stonecutting"),
                        mat.brickWall
                )
                .setPrimaryInputAndUnlock(mat.bricks)
                .setGroup(mat.baseName + "_stonecutting")
                .build();
    }
}