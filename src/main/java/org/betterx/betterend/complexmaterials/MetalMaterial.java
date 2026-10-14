package org.betterx.betterend.complexmaterials;

import org.betterx.bclib.blocks.*;
import org.betterx.bclib.items.ModelProviderItem;
import org.betterx.bclib.items.tool.BaseAxeItem;
import org.betterx.bclib.items.tool.BaseHoeItem;
import org.betterx.bclib.items.tool.BaseShovelItem;
import org.betterx.bclib.items.tool.BaseSwordItem;
import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.BulbVineLanternBlock;
import org.betterx.betterend.blocks.BulbVineLanternColoredBlock;
import org.betterx.betterend.blocks.ChandelierBlock;
import org.betterx.betterend.blocks.basis.EndAnvilBlock;
import org.betterx.betterend.item.EndArmorItem;
import org.betterx.betterend.item.tool.EndHammerItem;
import org.betterx.betterend.item.tool.EndPickaxe;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;
import org.betterx.betterend.registry.EndTemplates;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class MetalMaterial {
    public final Block ore;
    public final Block block;
    public final Block tile;
    public final Block bars;
    public final Block pressurePlate;
    public final Block door;
    public final Block trapdoor;
    public final Block chain;
    public final Block stairs;
    public final Block slab;

    public final Block chandelier;
    public final Block bulb_lantern;
    public final ColoredMaterial bulb_lantern_colored;

    public final Block anvilBlock;

    public final Item rawOre;
    public final Item nugget;
    public final Item ingot;

    public final Item shovelHead;
    public final Item pickaxeHead;
    public final Item axeHead;
    public final Item hoeHead;
    public final Item swordBlade;
    public final Item swordHandle;

    public final Item shovel;
    public final Item sword;
    public final Item pickaxe;
    public final Item axe;
    public final Item hoe;
    public final Item hammer;

    public final Item forgedPlate;
    public final Item helmet;
    public final Item chestplate;
    public final Item leggings;
    public final Item boots;

    public final TagKey<Item> alloyingOre;
    public final SmithingTemplateItem swordHandleTemplate;

    public static MetalMaterial makeNormal(
            String name,
            MapColor color,
            Tier material,
            ArmorMaterial armor,
            int anvilAndToolLevel,
            SmithingTemplateItem swordHandleTemplate
    ) {
        return new MetalMaterial(
                name,
                true,
                FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).mapColor(color),
                EndItems.makeEndItemSettings(),
                material,
                armor,
                anvilAndToolLevel,
                swordHandleTemplate
        );
    }

    public static MetalMaterial makeNormal(
            String name,
            MapColor color,
            float hardness,
            float resistance,
            Tier material,
            ArmorMaterial armor,
            int anvilAndToolLevel,
            SmithingTemplateItem swordHandleTemplate
    ) {
        return new MetalMaterial(
                name,
                true,
                FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
                                   .mapColor(color)
                                   .hardness(hardness)
                                   .resistance(resistance),
                EndItems.makeEndItemSettings(),
                material,
                armor,
                anvilAndToolLevel,
                swordHandleTemplate
        );
    }

    public static MetalMaterial makeOreless(
            String name,
            MapColor color,
            Tier material,
            ArmorMaterial armor,
            int anvilAndToolLevel,
            SmithingTemplateItem swordHandleTemplate
    ) {
        return new MetalMaterial(
                name,
                false,
                FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).mapColor(color),
                EndItems.makeEndItemSettings(),
                material,
                armor,
                anvilAndToolLevel,
                swordHandleTemplate
        );
    }

    public static MetalMaterial makeOreless(
            String name,
            MapColor color,
            float hardness,
            float resistance,
            Tier material,
            ArmorMaterial armor,
            int anvilAndToolLevel,
            SmithingTemplateItem swordHandleTemplate
    ) {
        return new MetalMaterial(
                name,
                false,
                FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
                                   .mapColor(color)
                                   .hardness(hardness)
                                   .resistance(resistance),
                EndItems.makeEndItemSettings(),
                material,
                armor,
                anvilAndToolLevel,
                swordHandleTemplate
        );
    }

    public final String name;

    private MetalMaterial(
            String name,
            boolean hasOre,
            FabricBlockSettings settings,
            Properties itemSettings,
            Tier material,
            ArmorMaterial armor,
            int anvilAndToolLevel,
            SmithingTemplateItem swordHandleTemplate
    ) {
        BlockBehaviour.Properties lanternProperties = FabricBlockSettings.copyOf(settings)
                                                                         .hardness(1)
                                                                         .resistance(1)
                                                                         .lightLevel((bs) -> 15)
                                                                         .sound(SoundType.LANTERN);
        final int level = material.getLevel();
        this.name = name;
        this.swordHandleTemplate = swordHandleTemplate;
        rawOre = hasOre ? EndItems.registerEndItem(name + "_raw", new ModelProviderItem(itemSettings)) : null;
        ore = hasOre ? EndBlocks.registerBlock(name + "_ore", new BaseOreBlock(() -> rawOre, 1, 3, 1)) : null;
        alloyingOre = hasOre ? TagManager.ITEMS.makeTag(BetterEnd.MOD_ID, name + "_alloying") : null;
        if (hasOre) {
            TagManager.ITEMS.add(alloyingOre, ore.asItem(), rawOre);
        }

        block = EndBlocks.registerBlock(name + "_block", new BaseBlock.Metal(settings));
        tile = EndBlocks.registerBlock(name + "_tile", new BaseBlock.Metal(settings));
        stairs = EndBlocks.registerBlock(name + "_stairs", new BaseStairsBlock.Metal(tile));
        slab = EndBlocks.registerBlock(name + "_slab", new BaseSlabBlock.Metal(tile));
        door = EndBlocks.registerBlock(name + "_door", new BaseDoorBlock.Metal(block, BlockSetType.IRON));
        trapdoor = EndBlocks.registerBlock(name + "_trapdoor", new BaseTrapdoorBlock.Metal(block, BlockSetType.IRON));
        bars = EndBlocks.registerBlock(name + "_bars", new BaseBarsBlock.Metal(block));
        chain = EndBlocks.registerBlock(name + "_chain", new BaseChainBlock.Metal(block.defaultMapColor()));
        pressurePlate = EndBlocks.registerBlock(
                name + "_plate",
                new BasePressurePlateBlock.Wood(block, BlockSetType.IRON)
        );

        chandelier = EndBlocks.registerBlock(name + "_chandelier", new ChandelierBlock(block));
        bulb_lantern = EndBlocks.registerBlock(name + "_bulb_lantern", new BulbVineLanternBlock(lanternProperties));
        bulb_lantern_colored = new ColoredMaterial(BulbVineLanternColoredBlock::new, bulb_lantern, false);

        nugget = EndItems.registerEndItem(name + "_nugget", new ModelProviderItem(itemSettings));
        ingot = EndItems.registerEndItem(name + "_ingot", new ModelProviderItem(itemSettings));

        shovelHead = EndItems.registerEndItem(name + "_shovel_head");
        pickaxeHead = EndItems.registerEndItem(name + "_pickaxe_head");
        axeHead = EndItems.registerEndItem(name + "_axe_head");
        hoeHead = EndItems.registerEndItem(name + "_hoe_head");
        swordBlade = EndItems.registerEndItem(name + "_sword_blade");
        swordHandle = EndItems.registerEndItem(name + "_sword_handle");

        shovel = EndItems.registerEndTool(name + "_shovel", new BaseShovelItem(material, 1.5F, -3.0F, itemSettings));
        sword = EndItems.registerEndTool(name + "_sword", new BaseSwordItem(material, 3, -2.4F, itemSettings));
        pickaxe = EndItems.registerEndTool(name + "_pickaxe", new EndPickaxe(material, 1, -2.8F, itemSettings));
        axe = EndItems.registerEndTool(name + "_axe", new BaseAxeItem(material, 6.0F, -3.0F, itemSettings));
        hoe = EndItems.registerEndTool(name + "_hoe", new BaseHoeItem(material, -3, 0.0F, itemSettings));
        hammer = EndItems.registerEndTool(
                name + "_hammer",
                new EndHammerItem(material, 5.0F, -3.2F, 0.3D, itemSettings)
        );

        forgedPlate = EndItems.registerEndItem(
                name + "_forged_plate",
                new ModelProviderItem(EndItems.makeEndItemSettings())
        );
        helmet = EndItems.registerEndItem(
                name + "_helmet",
                new EndArmorItem(armor, ArmorItem.Type.HELMET, EndItems.makeEndItemSettings())
        );
        chestplate = EndItems.registerEndItem(
                name + "_chestplate",
                new EndArmorItem(armor, ArmorItem.Type.CHESTPLATE, EndItems.makeEndItemSettings())
        );
        leggings = EndItems.registerEndItem(
                name + "_leggings",
                new EndArmorItem(armor, ArmorItem.Type.LEGGINGS, EndItems.makeEndItemSettings())
        );
        boots = EndItems.registerEndItem(
                name + "_boots",
                new EndArmorItem(armor, ArmorItem.Type.BOOTS, EndItems.makeEndItemSettings())
        );

        anvilBlock = EndBlocks.registerBlock(
                name + "_anvil",
                new EndAnvilBlock(this, block.defaultMapColor(), anvilAndToolLevel)
        );

        if (hasOre) {
            BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_ingot_furnace_ore"), ingot)
                            .setPrimaryInputAndUnlock(ore)
                            .buildWithBlasting();
            BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_ingot_furnace_raw"), ingot)
                            .setPrimaryInputAndUnlock(rawOre)
                            .buildWithBlasting();
            BCLRecipeBuilder.alloying(BetterEnd.makeID(name + "_ingot_alloy"), ingot)
                            .setInput(alloyingOre, alloyingOre)
                            .setOutputCount(3)
                            .setExperience(2.1F)
                            .build();
        }

        // Basic recipes
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_ingot_from_nuggets"), ingot)
                        .setShape("###", "###", "###")
                        .addMaterial('#', nugget)
                        .setGroup("end_metal_ingots_nug")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_nuggets_from_ingot"), nugget)
                        .setOutputCount(9)
                        .shapeless()
                        .addMaterial('#', ingot)
                        .setGroup("end_metal_nuggets_ing")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_block"), block)
                        .setShape("###", "###", "###")
                        .addMaterial('#', ingot)
                        .setGroup("end_metal_blocks")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_ingot_from_block"), ingot)
                        .setOutputCount(9)
                        .shapeless()
                        .addMaterial('#', block)
                        .setGroup("end_metal_ingots")
                        .build();

        // Block recipes
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_tile"), tile)
                        .setOutputCount(4)
                        .setShape("##", "##")
                        .addMaterial('#', block)
                        .setGroup("end_metal_tiles")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bars"), bars)
                        .setOutputCount(16)
                        .setShape("###", "###")
                        .addMaterial('#', ingot)
                        .setGroup("end_metal_bars")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_pressure_plate"), pressurePlate)
                        .setShape("##")
                        .addMaterial('#', ingot)
                        .setGroup("end_metal_plates")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_door"), door)
                        .setOutputCount(3)
                        .setShape("##", "##", "##")
                        .addMaterial('#', ingot)
                        .setGroup("end_metal_doors")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_trapdoor"), trapdoor)
                        .setShape("##", "##")
                        .addMaterial('#', ingot)
                        .setGroup("end_metal_trapdoors")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_stairs"), stairs)
                        .setOutputCount(4)
                        .setShape("#  ", "## ", "###")
                        .addMaterial('#', block, tile)
                        .setGroup("end_metal_stairs")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_slab"), slab)
                        .setOutputCount(6)
                        .setShape("###")
                        .addMaterial('#', block, tile)
                        .setGroup("end_metal_slabs")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_chain"), chain)
                        .setShape("N", "#", "N")
                        .addMaterial('#', ingot)
                        .addMaterial('N', nugget)
                        .setGroup("end_metal_chain")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_anvil"), anvilBlock)
                        .setShape("###", " I ", "III")
                        .addMaterial('#', block, tile)
                        .addMaterial('I', ingot)
                        .setGroup("end_metal_anvil")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_bulb_lantern"), bulb_lantern)
                        .setShape("C", "I", "#")
                        .addMaterial('C', chain)
                        .addMaterial('I', ingot)
                        .addMaterial('#', EndItems.GLOWING_BULB)
                        .build();

        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_chandelier"), chandelier)
                        .setShape("I#I", " # ")
                        .addMaterial('#', ingot)
                        .addMaterial('I', EndItems.LUMECORN_ROD)
                        .setGroup("end_metal_chandelier")
                        .build();

        // Tools & armor into nuggets
        BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_axe_nugget"), nugget)
                        .setPrimaryInputAndUnlock(axe)
                        .buildWithBlasting();
        BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_hoe_nugget"), nugget)
                        .setPrimaryInputAndUnlock(hoe)
                        .buildWithBlasting();
        BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_pickaxe_nugget"), nugget)
                        .setPrimaryInputAndUnlock(pickaxe)
                        .buildWithBlasting();
        BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_sword_nugget"), nugget)
                        .setPrimaryInputAndUnlock(sword)
                        .buildWithBlasting();
        BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_hammer_nugget"), nugget)
                        .setPrimaryInputAndUnlock(hammer)
                        .buildWithBlasting();
        BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_helmet_nugget"), nugget)
                        .setPrimaryInputAndUnlock(helmet)
                        .buildWithBlasting();
        BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_chestplate_nugget"), nugget)
                        .setPrimaryInputAndUnlock(chestplate)
                        .buildWithBlasting();
        BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_leggings_nugget"), nugget)
                        .setPrimaryInputAndUnlock(leggings)
                        .buildWithBlasting();
        BCLRecipeBuilder.smelting(BetterEnd.makeID(name + "_boots_nugget"), nugget)
                        .setPrimaryInputAndUnlock(boots)
                        .buildWithBlasting();

        // Tool parts from ingots
        BCLRecipeBuilder.anvil(BetterEnd.makeID(name + "_shovel_head"), shovelHead)
                        .setPrimaryInput(ingot)
                        .setAnvilLevel(anvilAndToolLevel)
                        .setToolLevel(level)
                        .setDamage(level)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID(name + "_pickaxe_head"), pickaxeHead)
                        .setPrimaryInput(ingot)
                        .setInputCount(3)
                        .setAnvilLevel(anvilAndToolLevel)
                        .setToolLevel(level)
                        .setDamage(level)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID(name + "_axe_head"), axeHead)
                        .setPrimaryInput(ingot)
                        .setInputCount(3)
                        .setAnvilLevel(anvilAndToolLevel)
                        .setToolLevel(level)
                        .setDamage(level)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID(name + "_hoe_head"), hoeHead)
                        .setPrimaryInput(ingot)
                        .setInputCount(2)
                        .setAnvilLevel(anvilAndToolLevel)
                        .setToolLevel(level)
                        .setDamage(level)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID(name + "_sword_blade"), swordBlade)
                        .setPrimaryInput(ingot)
                        .setAnvilLevel(anvilAndToolLevel)
                        .setToolLevel(level)
                        .setDamage(level)
                        .build();
        BCLRecipeBuilder.anvil(BetterEnd.makeID(name + "_forged_plate"), forgedPlate)
                        .setPrimaryInput(ingot)
                        .setAnvilLevel(anvilAndToolLevel)
                        .setToolLevel(level)
                        .setDamage(level)
                        .build();

        // Tools from parts
        BCLRecipeBuilder.smithing(BetterEnd.makeID(name + "_hammer"), hammer)
                        .setTemplate(EndTemplates.HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(block)
                        .setAddition(Items.STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID(name + "_axe"), axe)
                        .setTemplate(EndTemplates.HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(axeHead)
                        .setAddition(Items.STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID(name + "_pickaxe"), pickaxe)
                        .setTemplate(EndTemplates.HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(pickaxeHead)
                        .setAddition(Items.STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID(name + "_hoe"), hoe)
                        .setTemplate(EndTemplates.HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(hoeHead)
                        .setAddition(Items.STICK)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID(name + "_sword_handle"), swordHandle)
                        .setTemplate(this.swordHandleTemplate)
                        .setPrimaryInputAndUnlock(Items.STICK)
                        .setAddition(ingot)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID(name + "_sword"), sword)
                        .setTemplate(EndTemplates.TOOL_ASSEMBLY)
                        .setPrimaryInputAndUnlock(swordBlade)
                        .setAddition(swordHandle)
                        .build();
        BCLRecipeBuilder.smithing(BetterEnd.makeID(name + "_shovel"), shovel)
                        .setTemplate(EndTemplates.HANDLE_ATTACHMENT)
                        .setPrimaryInputAndUnlock(shovelHead)
                        .setAddition(Items.STICK)
                        .build();

        // Armor crafting
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_helmet"), helmet)
                        .setShape("###", "# #")
                        .addMaterial('#', forgedPlate)
                        .setGroup("end_metal_helmets")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_chestplate"), chestplate)
                        .setShape("# #", "###", "###")
                        .addMaterial('#', forgedPlate)
                        .setGroup("end_metal_chestplates")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_leggings"), leggings)
                        .setShape("###", "# #", "# #")
                        .addMaterial('#', forgedPlate)
                        .setGroup("end_metal_leggings")
                        .build();
        BCLRecipeBuilder.crafting(BetterEnd.makeID(name + "_boots"), boots)
                        .setShape("# #", "# #")
                        .addMaterial('#', forgedPlate)
                        .setGroup("end_metal_boots")
                        .build();

        TagManager.BLOCKS.add(BlockTags.ANVIL, anvilBlock);
        TagManager.BLOCKS.add(BlockTags.BEACON_BASE_BLOCKS, block);
        TagManager.ITEMS.add(ItemTags.BEACON_PAYMENT_ITEMS, ingot);
        TagManager.BLOCKS.addUntyped(
                TagManager.BLOCKS.makeCommonTag("chains"),
                BetterEnd.makeID(name + "_chain")
        );
        TagManager.ITEMS.addUntyped(
                TagManager.ITEMS.makeCommonTag("chains"),
                BetterEnd.makeID(name + "_chain")
        );
        if (hasOre && ore != null) {
            TagManager.BLOCKS.add(BlockTags.DRAGON_IMMUNE, ore, bars);
        } else {
            TagManager.BLOCKS.add(BlockTags.DRAGON_IMMUNE, bars);
        }
    }
}
