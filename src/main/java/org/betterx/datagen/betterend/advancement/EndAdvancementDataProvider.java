package org.betterx.datagen.betterend.advancement;

import org.betterx.bclib.api.v2.advancement.AdvancementManager;
import org.betterx.bclib.api.v3.datagen.AdvancementDataProvider;
import org.betterx.bclib.complexmaterials.set.wood.WoodSlots;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.advancements.BECriteria;
import org.betterx.betterend.complexmaterials.MetalMaterial;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;
import org.betterx.betterend.registry.EndStructures;
import org.betterx.betterend.registry.EndTemplates;
import org.betterx.betterend.world.biome.EndBiome;

import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

public class EndAdvancementDataProvider extends AdvancementDataProvider {
    public EndAdvancementDataProvider(
    ) {
        super(List.of(BetterEnd.MOD_ID));
    }

    @Override
    protected void bootstrap() {
        ResourceLocation root = AdvancementManager.Builder
                .create(BetterEnd.makeID("root"))
                .startDisplay(EndBlocks.END_MYCELIUM)
                .frame(FrameType.TASK)
                .hideFromChat()
                .background(new ResourceLocation("textures/gui/advancements/backgrounds/end.png"))
                .endDisplay()
                .addCriterion(
                        "welcome",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.ANY)
                )
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation enterEnd = AdvancementManager.Builder
                .create(BetterEnd.makeID("enter_end"))
                .startDisplay(EndBlocks.CAVE_MOSS)
                .endDisplay()
                .parent(root)
                .addCriterion(
                        "entered_end",
                        ChangeDimensionTrigger
                                .TriggerInstance
                                .changedDimensionTo(Level.END)
                )
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation portal = AdvancementManager.Builder
                .create(BetterEnd.makeID("portal"))
                .parent(enterEnd)
                .startDisplay(EndBlocks.ETERNAL_PEDESTAL)
                .frame(FrameType.GOAL)
                .endDisplay()
                .addAtStructureCriterion("eternal_portal", EndStructures.ETERNAL_PORTAL)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation portalOn = AdvancementManager.Builder
                .create(BetterEnd.makeID("portal_on"))
                .parent(portal)
                .startDisplay(EndItems.ETERNAL_CRYSTAL)
                .endDisplay()
                .addCriterion("turn_on", BECriteria.PORTAL_ON_TRIGGER)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation portalTravel = AdvancementManager.Builder
                .create(BetterEnd.makeID("portal_travel"))
                .parent(portalOn)
                .startDisplay(Items.GRASS_BLOCK)
                .frame(FrameType.CHALLENGE)
                .endDisplay()
                .addCriterion("travel", BECriteria.PORTAL_TRAVEL_TRIGGER)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation allTheBiomes = AdvancementManager.Builder
                .create(BetterEnd.makeID("all_the_biomes"))
                .parent(enterEnd)
                .startDisplay(EndItems.AETERNIUM_BOOTS)
                .frame(FrameType.CHALLENGE)
                .endDisplay()
                .addVisitBiomesCriterion(EndBiome.getAllBeBiomes().stream().map(b -> b.getBiomeKey()).toList())
                .requirements(RequirementsStrategy.AND)
                .rewardXP(1500)
                .build();

        ResourceLocation village = AdvancementManager.Builder
                .create(BetterEnd.makeID("village"))
                .parent(allTheBiomes)
                .startDisplay(EndBlocks.TENANEA.getBlock(WoodSlots.DOOR))
                .frame(FrameType.GOAL)
                .endDisplay()
                .addAtStructureCriterion("end_village", EndStructures.END_VILLAGE)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation allElytras = AdvancementManager.Builder
                .create(BetterEnd.makeID("all_elytras"))
                .parent(enterEnd)
                .startDisplay(EndItems.CRYSTALITE_ELYTRA)
                .frame(FrameType.GOAL)
                .endDisplay()
                .addInventoryChangedCriterion("vanilla", Items.ELYTRA)
                .addInventoryChangedCriterion("crystalite", EndItems.CRYSTALITE_ELYTRA)
                .addInventoryChangedCriterion("armored", EndItems.ARMORED_ELYTRA)
                .requirements(RequirementsStrategy.AND)
                .build();

        ResourceLocation infusion = AdvancementManager.Builder
                .create(BetterEnd.makeID("infusion"))
                .parent(enterEnd)
                .startDisplay(EndBlocks.INFUSION_PEDESTAL)
                .endDisplay()
                .addInventoryChangedCriterion("infusion_pedestal", EndBlocks.INFUSION_PEDESTAL)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation infusionFinished = AdvancementManager.Builder
                .create(BetterEnd.makeID("infusion_finished"))
                .parent(infusion)
                .startDisplay(Items.ENDER_EYE)
                .frame(FrameType.GOAL)
                .endDisplay()
                .addCriterion("finished", BECriteria.INFUSION_FINISHED_TRIGGER)
                .requirements(RequirementsStrategy.OR)
                .build();


        ResourceLocation allTheTemplates = AdvancementManager.Builder
                .create(BetterEnd.makeID("all_the_templates"))
                .parent(enterEnd)
                .startDisplay(EndTemplates.TOOL_ASSEMBLY)
                .frame(FrameType.CHALLENGE)
                .endDisplay()
                .addInventoryChangedAnyCriterion("got_handle", EndTemplates.HANDLE_ATTACHMENT)
                .addInventoryChangedAnyCriterion("got_tool", EndTemplates.TOOL_ASSEMBLY)
                .addInventoryChangedAnyCriterion("got_leather", EndTemplates.LEATHER_HANDLE_ATTACHMENT)
                .addInventoryChangedAnyCriterion("got_plate", EndTemplates.PLATE_UPGRADE)
                .addInventoryChangedAnyCriterion("got_terminite", EndTemplates.TERMINITE_UPGRADE)
                .addInventoryChangedAnyCriterion("got_aeternium", EndTemplates.AETERNIUM_UPGRADE)
                .addInventoryChangedAnyCriterion("got_thallasium", EndTemplates.THALLASIUM_UPGRADE)
                .addInventoryChangedAnyCriterion("got_netherite", EndTemplates.NETHERITE_UPGRADE)
                .requirements(RequirementsStrategy.AND)
                .rewardXP(1500)
                .build();

        ResourceLocation hammer = AdvancementManager.Builder
                .create(BetterEnd.makeID("hammer"))
                .parent(enterEnd)
                .startDisplay(EndItems.DIAMOND_HAMMER)
                .endDisplay()
                .addInventoryChangedCriterion("got_diamond_hammer", EndItems.DIAMOND_HAMMER)
                .addInventoryChangedCriterion("got_thallasium_hammer", EndBlocks.THALLASIUM.hammer)
                .addInventoryChangedCriterion("got_terminite_hammer", EndBlocks.TERMINITE.hammer)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation thallasiumAnvil = AdvancementManager.Builder
                .create(BetterEnd.makeID("thallasium_anvil"))
                .parent(hammer)
                .startDisplay(EndBlocks.THALLASIUM.anvilBlock)
                .endDisplay()
                .addInventoryChangedCriterion("got_thallasium_anvil", EndBlocks.THALLASIUM.anvilBlock)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation thallasiumPlate = AdvancementManager.Builder
                .create(BetterEnd.makeID("thallasium_plate"))
                .parent(thallasiumAnvil)
                .startDisplay(EndBlocks.THALLASIUM.forgedPlate)
                .endDisplay()
                .addInventoryChangedCriterion("got_thallasium_plate", EndBlocks.THALLASIUM.forgedPlate)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation terminiteAnvil = AdvancementManager.Builder
                .create(BetterEnd.makeID("terminite_anvil"))
                .parent(thallasiumAnvil)
                .startDisplay(EndBlocks.TERMINITE.anvilBlock)
                .endDisplay()
                .addInventoryChangedCriterion("got_terminite_anvil", EndBlocks.TERMINITE.anvilBlock)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation terminitePlate = AdvancementManager.Builder
                .create(BetterEnd.makeID("terminite_plate"))
                .parent(terminiteAnvil)
                .startDisplay(EndBlocks.TERMINITE.forgedPlate)
                .endDisplay()
                .addInventoryChangedCriterion("got_erminite_plate", EndBlocks.TERMINITE.forgedPlate)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation aeterniumAnvil = AdvancementManager.Builder
                .create(BetterEnd.makeID("aeternium_anvil"))
                .parent(terminiteAnvil)
                .startDisplay(EndBlocks.AETERNIUM_ANVIL)
                .frame(FrameType.CHALLENGE)
                .endDisplay()
                .addInventoryChangedCriterion("got_aeternium_anvil", EndBlocks.AETERNIUM_ANVIL)
                .requirements(RequirementsStrategy.OR)
                .rewardXP(500)
                .build();

        ResourceLocation aeterniumHammerHead = AdvancementManager.Builder
                .create(BetterEnd.makeID("aeternium_hammer_head"))
                .parent(aeterniumAnvil)
                .startDisplay(EndItems.AETERNIUM_HAMMER_HEAD)
                .endDisplay()
                .addInventoryChangedCriterion("got_aeternium_hammer_head", EndItems.AETERNIUM_HAMMER_HEAD)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation aeterniumHammer = AdvancementManager.Builder
                .create(BetterEnd.makeID("aeternium_hammer"))
                .parent(aeterniumHammerHead)
                .startDisplay(EndItems.AETERNIUM_HAMMER)
                .endDisplay()
                .addInventoryChangedCriterion("got_aeternium_hammer", EndItems.AETERNIUM_HAMMER)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation aeterniumPlate = AdvancementManager.Builder
                .create(BetterEnd.makeID("aeternium_plate"))
                .parent(aeterniumHammer)
                .startDisplay(EndItems.AETERNIUM_FORGED_PLATE)
                .frame(FrameType.GOAL)
                .endDisplay()
                .addInventoryChangedCriterion("got_aeternium_plate", EndItems.AETERNIUM_FORGED_PLATE)
                .requirements(RequirementsStrategy.OR)
                .rewardXP(200)
                .build();

        ResourceLocation thallasiumArmor = addArmor(EndBlocks.THALLASIUM)
                .parent(thallasiumPlate)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation thallasiumHead = addToolHeads(EndBlocks.THALLASIUM)
                .parent(thallasiumAnvil)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation thallasium = addTools(EndBlocks.THALLASIUM)
                .parent(thallasiumHead)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation terminiteHead = addToolHeads(EndBlocks.TERMINITE)
                .parent(terminiteAnvil)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation terminite = addTools(EndBlocks.TERMINITE)
                .parent(terminiteHead)
                .requirements(RequirementsStrategy.OR)
                .build();

        ResourceLocation terminiteArmor = addArmor(EndBlocks.TERMINITE)
                .parent(terminitePlate)
                .requirements(RequirementsStrategy.OR)
                .build();


        ResourceLocation aeterniumHead = AdvancementManager.Builder
                .create(BetterEnd.makeID("aeternium_tool_head"))
                .startDisplay(EndItems.AETERNIUM_PICKAXE_HEAD)
                .frame(FrameType.GOAL)
                .endDisplay()
                .parent(aeterniumHammer)
                .addInventoryChangedCriterion("got_aeternium_pickaxe_head", EndItems.AETERNIUM_PICKAXE_HEAD)
                .addInventoryChangedCriterion("got_aeternium_hoe_head", EndItems.AETERNIUM_HOE_HEAD)
                .addInventoryChangedCriterion("got_aeternium_axe_head", EndItems.AETERNIUM_AXE_HEAD)
                .addInventoryChangedCriterion("got_aeternium_shovel_head", EndItems.AETERNIUM_SHOVEL_HEAD)
                .addInventoryChangedCriterion(
                        "got_aeternium_sword_head",
                        EndItems.AETERNIUM_SWORD_BLADE,
                        EndItems.AETERNIUM_SWORD_HANDLE
                )
                .requirements(RequirementsStrategy.AND)
                .rewardXP(200)
                .build();

        ResourceLocation aeternium = AdvancementManager.Builder
                .create(BetterEnd.makeID("aeternium_tool"))
                .startDisplay(EndItems.AETERNIUM_PICKAXE)
                .frame(FrameType.CHALLENGE)
                .endDisplay()
                .parent(aeterniumHead)
                .addInventoryChangedCriterion("got_aeternium_pickaxe", EndItems.AETERNIUM_PICKAXE)
                .addInventoryChangedCriterion("got_aeternium_hoe", EndItems.AETERNIUM_HOE)
                .addInventoryChangedCriterion("got_aeternium_axe", EndItems.AETERNIUM_AXE)
                .addInventoryChangedCriterion("got_aeternium_shovel", EndItems.AETERNIUM_SHOVEL)
                .addInventoryChangedCriterion("got_aeternium_sword", EndItems.AETERNIUM_SWORD)
                .requirements(RequirementsStrategy.AND)
                .rewardXP(2000)
                .build();

        ResourceLocation aeterniumArmor = AdvancementManager.Builder
                .create(BetterEnd.makeID("aeternium_armor"))
                .startDisplay(EndItems.AETERNIUM_CHESTPLATE)
                .frame(FrameType.CHALLENGE)
                .endDisplay()
                .parent(aeterniumPlate)
                .addInventoryChangedCriterion("got_aeternium_helmet", EndItems.AETERNIUM_HELMET)
                .addInventoryChangedCriterion("got_aeternium_chestplate", EndItems.AETERNIUM_CHESTPLATE)
                .addInventoryChangedCriterion("got_aeternium_leggings", EndItems.AETERNIUM_LEGGINGS)
                .addInventoryChangedCriterion("got_aeternium_boots", EndItems.AETERNIUM_BOOTS)
                .requirements(RequirementsStrategy.AND)
                .rewardXP(2000)
                .build();
    }

    AdvancementManager.Builder addTools(MetalMaterial mat) {
        return AdvancementManager.Builder
                .create(BetterEnd.makeID(mat.name + "_tool"))
                .startDisplay(mat.pickaxe)
                .endDisplay()
                .addInventoryChangedCriterion("got_" + mat.name + "_pickaxe", mat.pickaxe)
                .addInventoryChangedCriterion("got_" + mat.name + "_hoe", mat.hoe)
                .addInventoryChangedCriterion("got_" + mat.name + "_axe", mat.axe)
                .addInventoryChangedCriterion("got_" + mat.name + "_shovel", mat.shovel)
                .addInventoryChangedCriterion("got_" + mat.name + "_sword", mat.sword);
    }

    AdvancementManager.Builder addToolHeads(MetalMaterial mat) {
        return AdvancementManager.Builder
                .create(BetterEnd.makeID(mat.name + "_tool_head"))
                .startDisplay(mat.pickaxeHead)
                .endDisplay()
                .addInventoryChangedCriterion("got_" + mat.name + "_pickaxe_head", mat.pickaxeHead)
                .addInventoryChangedCriterion("got_" + mat.name + "_hoe_head", mat.hoeHead)
                .addInventoryChangedCriterion("got_" + mat.name + "_axe_head", mat.axeHead)
                .addInventoryChangedCriterion("got_" + mat.name + "_shovel_head", mat.shovelHead)
                .addInventoryChangedCriterion("got_" + mat.name + "_sword_head", mat.swordBlade, mat.swordHandle);
    }

    AdvancementManager.Builder addArmor(MetalMaterial mat) {
        return AdvancementManager.Builder
                .create(BetterEnd.makeID(mat.name + "_armor"))
                .startDisplay(mat.chestplate)
                .endDisplay()
                .addInventoryChangedCriterion("got_" + mat.name + "_helmet", mat.helmet)
                .addInventoryChangedCriterion("got_" + mat.name + "_chestplate", mat.chestplate)
                .addInventoryChangedCriterion("got_" + mat.name + "_leggings", mat.leggings)
                .addInventoryChangedCriterion("got_" + mat.name + "_boots", mat.boots);
    }
}
