package org.betterx.betterend.recipe;

import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.item.material.EndToolMaterial;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;

public class AnvilRecipes {
    public static void register() {
        BCLRecipeBuilder.anvil(BetterEnd.makeID("ender_pearl_to_dust"), EndItems.ENDER_DUST)
                        .setPrimaryInputAndUnlock(Items.ENDER_PEARL)
                        .setAnvilLevel(Tiers.IRON.getLevel())
                        .setToolLevel(4)
                        .setDamage(5)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID("ender_shard_to_dust"), EndItems.ENDER_DUST)
                        .setPrimaryInputAndUnlock(EndItems.ENDER_SHARD)

                        .setAnvilLevel(Tiers.IRON.getLevel())
                        .setToolLevel(0)
                        .setDamage(3)
                        .build();

        int anvilLevel = EndToolMaterial.AETERNIUM.getLevel();
        BCLRecipeBuilder.anvil(BetterEnd.makeID("aeternium_axe_head"), EndItems.AETERNIUM_AXE_HEAD)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_INGOT)
                        .setAnvilLevel(anvilLevel)
                        .setToolLevel(anvilLevel)
                        .setDamage(6)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID("aeternium_pickaxe_head"), EndItems.AETERNIUM_PICKAXE_HEAD)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_INGOT)
                        .setAnvilLevel(anvilLevel)
                        .setToolLevel(anvilLevel)
                        .setDamage(6)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID("aeternium_shovel_head"), EndItems.AETERNIUM_SHOVEL_HEAD)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_INGOT)
                        .setAnvilLevel(anvilLevel)
                        .setToolLevel(anvilLevel)
                        .setDamage(6)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID("aeternium_hoe_head"), EndItems.AETERNIUM_HOE_HEAD)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_INGOT)
                        .setAnvilLevel(anvilLevel)
                        .setToolLevel(anvilLevel)
                        .setDamage(6)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID("aeternium_hammer_head"), EndItems.AETERNIUM_HAMMER_HEAD)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_INGOT)
                        .setAnvilLevel(anvilLevel)
                        .setToolLevel(EndToolMaterial.THALLASIUM.getLevel())
                        .setDamage(6)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID("aeternium_sword_blade"), EndItems.AETERNIUM_SWORD_BLADE)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_INGOT)
                        .setAnvilLevel(anvilLevel)
                        .setToolLevel(anvilLevel)
                        .setDamage(6)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID("aeternium_forged_plate"), EndItems.AETERNIUM_FORGED_PLATE)
                        .setPrimaryInputAndUnlock(EndItems.AETERNIUM_INGOT)
                        .setAnvilLevel(anvilLevel)
                        .setToolLevel(anvilLevel)
                        .setDamage(6)
                        .build();
    }
}
