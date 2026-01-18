package org.betterx.betterend.recipe;

import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;
import org.betterx.betterend.registry.EndTags;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class AlloyingRecipes {
    public static void register() {
        BCLRecipeBuilder.alloying(BetterEnd.makeID("additional_iron"), Items.IRON_INGOT)
                        .setInput(EndTags.ALLOYING_IRON, EndTags.ALLOYING_IRON)
                        .setOutputCount(3)
                        .setExperience(2.1F)
                        .build();
        BCLRecipeBuilder.alloying(BetterEnd.makeID("additional_gold"), Items.GOLD_INGOT)
                        .setInput(EndTags.ALLOYING_GOLD, EndTags.ALLOYING_GOLD)
                        .setOutputCount(3)
                        .setExperience(3F)
                        .build();
        BCLRecipeBuilder.alloying(BetterEnd.makeID("additional_copper"), Items.COPPER_INGOT)
                        .setInput(EndTags.ALLOYING_COPPER, EndTags.ALLOYING_COPPER)
                        .setOutputCount(3)
                        .setExperience(3F)
                        .build();
        BCLRecipeBuilder.alloying(BetterEnd.makeID("additional_netherite"), Items.NETHERITE_SCRAP)
                        .setInput(Blocks.ANCIENT_DEBRIS, Blocks.ANCIENT_DEBRIS)
                        .setOutputCount(3)
                        .setExperience(6F)
                        .setSmeltTime(1000)
                        .build();
        BCLRecipeBuilder.alloying(BetterEnd.makeID("terminite_ingot"), EndBlocks.TERMINITE.ingot)
                        .setInput(Items.IRON_INGOT, EndItems.ENDER_DUST)
                        .setOutputCount(1)
                        .setExperience(2.5F)
                        .setSmeltTime(450)
                        .build();
        BCLRecipeBuilder.alloying(BetterEnd.makeID("aeternium_ingot"), EndItems.AETERNIUM_INGOT)
                        .setInput(EndBlocks.TERMINITE.ingot, Items.NETHERITE_INGOT)
                        .setOutputCount(1)
                        .setExperience(4.5F)
                        .setSmeltTime(850)
                        .build();
        BCLRecipeBuilder.alloying(BetterEnd.makeID("terminite_ingot_thallasium"), EndBlocks.TERMINITE.ingot)
                        .setInput(EndBlocks.THALLASIUM.ingot, EndItems.ENDER_DUST)
                        .setOutputCount(1)
                        .setExperience(2.5F)
                        .setSmeltTime(450)
                        .build();
    }
}
