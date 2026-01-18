package org.betterx.betterend.recipe;

import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;
import org.betterx.betterend.registry.EndTemplates;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class SmithingRecipes {

    public static void register() {
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_pickaxe"), EndItems.AETERNIUM_PICKAXE)
                        .setTemplate(EndTemplates.LEATHER_HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_PICKAXE_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_axe"), EndItems.AETERNIUM_AXE)
                        .setTemplate(EndTemplates.LEATHER_HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_AXE_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_shovel"), EndItems.AETERNIUM_SHOVEL)
                        .setTemplate(EndTemplates.LEATHER_HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_SHOVEL_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_hoe"), EndItems.AETERNIUM_HOE)
                        .setTemplate(EndTemplates.LEATHER_HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_HOE_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_hammer"), EndItems.AETERNIUM_HAMMER)
                        .setTemplate(EndTemplates.LEATHER_HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_HAMMER_HEAD)
                        .setAddition(EndItems.LEATHER_WRAPPED_STICK)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_sword"), EndItems.AETERNIUM_SWORD)
                        .setTemplate(EndTemplates.TOOL_ASSEMBLY)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_SWORD_BLADE)
                        .setAddition(EndItems.AETERNIUM_SWORD_HANDLE)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_helmet"), EndItems.AETERNIUM_HELMET)
                        .setTemplate(EndTemplates.PLATE_UPGRADE)
                        .setPrimaryInputAndUnlock(EndBlocks.TERMINITE.helmet)
                        .setAddition(EndItems.AETERNIUM_FORGED_PLATE)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_chestplate"), EndItems.AETERNIUM_CHESTPLATE)
                        .setTemplate(EndTemplates.PLATE_UPGRADE)
                        .setPrimaryInputAndUnlock(EndBlocks.TERMINITE.chestplate)
                        .setAddition(EndItems.AETERNIUM_FORGED_PLATE)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_leggings"), EndItems.AETERNIUM_LEGGINGS)
                        .setTemplate(EndTemplates.PLATE_UPGRADE)
                        .setPrimaryInputAndUnlock(EndBlocks.TERMINITE.leggings)
                        .setAddition(EndItems.AETERNIUM_FORGED_PLATE)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_boots"), EndItems.AETERNIUM_BOOTS)
                        .setTemplate(EndTemplates.PLATE_UPGRADE)
                        .setPrimaryInputAndUnlock(EndBlocks.TERMINITE.boots)
                        .setAddition(EndItems.AETERNIUM_FORGED_PLATE)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("aeternium_sword_handle"), EndItems.AETERNIUM_SWORD_HANDLE)
                        .setTemplate(EndTemplates.TERMINITE_UPGRADE)
                        .setPrimaryInputAndUnlock(EndItems.LEATHER_WRAPPED_STICK)
                        .setAddition(EndBlocks.TERMINITE.ingot)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("thallasium_anvil_updrade"), EndBlocks.TERMINITE.anvilBlock)
                        .setTemplate(EndTemplates.TERMINITE_UPGRADE)
                        .setPrimaryInputAndUnlock(EndBlocks.THALLASIUM.anvilBlock)
                        .setAddition(EndBlocks.TERMINITE.ingot)
                        .build();

        BCLRecipeBuilder.smithing(BetterEnd.makeID("terminite_anvil_updrade"), EndBlocks.AETERNIUM_ANVIL)
                        .setTemplate(EndTemplates.AETERNIUM_UPGRADE)
                        .setPrimaryInputAndUnlock(EndBlocks.TERMINITE.anvilBlock)
                        .setAddition(EndItems.AETERNIUM_INGOT)
                        .build();


        BCLRecipeBuilder.smithing(BetterEnd.makeID("armored_elytra"), EndItems.ARMORED_ELYTRA)
                        .setTemplate(EndTemplates.AETERNIUM_UPGRADE)
                        .setPrimaryInputAndUnlock(Items.ELYTRA)
                        .setAddition(EndItems.AETERNIUM_INGOT)
                        .build();


        BCLRecipeBuilder.smithing(BetterEnd.makeID("netherite_hammer"), EndItems.NETHERITE_HAMMER)
                        .setTemplate(EndTemplates.NETHERITE_UPGRADE)
                        .setPrimaryInputAndUnlock(EndItems.DIAMOND_HAMMER)
                        .setAddition(Items.NETHERITE_INGOT)
                        .build();


        BCLRecipeBuilder.copySmithingTemplate(
                BetterEnd.makeID("copy_plate_upgrade"),
                EndTemplates.PLATE_UPGRADE,
                Items.IRON_INGOT
        ).build();

        BCLRecipeBuilder.copySmithingTemplate(
                BetterEnd.makeID("copy_leather_handle_attachment"),
                EndTemplates.LEATHER_HANDLE_ATTACHMENT,
                Items.LEATHER
        ).build();

        BCLRecipeBuilder.copyCheapSmithingTemplate(
                BetterEnd.makeID("copy_handle_attachment"),
                EndTemplates.HANDLE_ATTACHMENT,
                Items.DIAMOND
        ).build();

        BCLRecipeBuilder.copyCheapSmithingTemplate(
                BetterEnd.makeID("copy_terminite_upgrade"),
                EndTemplates.TERMINITE_UPGRADE,
                EndBlocks.TERMINITE.ingot
        ).build();

        BCLRecipeBuilder.copyCheapSmithingTemplate(
                BetterEnd.makeID("copy_thallasium_upgrade"),
                EndTemplates.THALLASIUM_UPGRADE,
                EndBlocks.THALLASIUM.ingot
        ).build();

        BCLRecipeBuilder.copySmithingTemplate(
                BetterEnd.makeID("copy_aeternium_upgrade"),
                EndTemplates.AETERNIUM_UPGRADE,
                Blocks.LAPIS_BLOCK
        ).build();

        BCLRecipeBuilder.copyCheapSmithingTemplate(
                BetterEnd.makeID("copy_tool_assembly"),
                EndTemplates.TOOL_ASSEMBLY,
                Blocks.IRON_BLOCK
        ).build();

        BCLRecipeBuilder.copySmithingTemplate(
                BetterEnd.makeID("copy_netherite_upgrade"),
                EndTemplates.NETHERITE_UPGRADE,
                Blocks.NETHERRACK
        ).build();
    }
}
