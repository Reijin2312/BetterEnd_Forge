package org.betterx.betterend.util;

import org.betterx.bclib.api.v3.bonemeal.BonemealAPI;
import org.betterx.bclib.api.v3.bonemeal.WaterGrassSpreader;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndFeatures;
import org.betterx.betterend.registry.EndTags;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.world.level.block.Block;

public class BonemealPlants {
    public static void init() {
        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.END_MOSS,
                EndFeatures.BONEMEAL_END_MOSS
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.RUTISCUS,
                EndFeatures.BONEMEAL_RUTISCUS
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.END_MYCELIUM,
                EndFeatures.BONEMEAL_END_MYCELIUM
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.JUNGLE_MOSS,
                EndFeatures.BONEMEAL_JUNGLE_MOSS
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.SANGNUM,
                EndFeatures.BONEMEAL_SANGNUM
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.MOSSY_OBSIDIAN,
                EndFeatures.BONEMEAL_MOSSY_OBSIDIAN
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.MOSSY_DRAGON_BONE,
                EndFeatures.BONEMEAL_MOSSY_DRAGON_BONE
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.CAVE_MOSS,
                EndFeatures.BONEMEAL_CAVE_MOSS
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.CHORUS_NYLIUM,
                EndFeatures.BONEMEAL_CHORUS_NYLIUM
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.CRYSTAL_MOSS,
                EndFeatures.BONEMEAL_CRYSTAL_MOSS
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.SHADOW_GRASS,
                EndFeatures.BONEMEAL_SHADOW_GRASS
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.PINK_MOSS,
                EndFeatures.BONEMEAL_PINK_MOSS
        );

        BonemealAPI.INSTANCE.addSpreadableFeatures(
                EndBlocks.AMBER_MOSS,
                EndFeatures.BONEMEAL_AMBER_MOSS
        );


//        BonemealAPI.addLandGrass(EndBlocks.CAVE_GRASS, EndBlocks.CAVE_MOSS);
//
//        BonemealAPI.addLandGrass(EndBlocks.CHORUS_GRASS, EndBlocks.CHORUS_NYLIUM);
//
//        BonemealAPI.addLandGrass(EndBlocks.CRYSTAL_GRASS, EndBlocks.CRYSTAL_MOSS);
//
//        BonemealAPI.addLandGrass(EndBlocks.SHADOW_PLANT, EndBlocks.SHADOW_GRASS);
//
//        BonemealAPI.addLandGrass(EndBlocks.BUSHY_GRASS, EndBlocks.PINK_MOSS);
//
//        BonemealAPI.addLandGrass(EndBlocks.AMBER_GRASS, EndBlocks.AMBER_MOSS);

//        BonemealAPI.addLandGrass(EndBlocks.CREEPING_MOSS, EndBlocks.END_MOSS);
//        BonemealAPI.addLandGrass(EndBlocks.UMBRELLA_MOSS, EndBlocks.END_MOSS);
//
//        BonemealAPI.addLandGrass(
//                EndBiomes.GLOWING_GRASSLANDS.getID(),
//                EndBlocks.BLOOMING_COOKSONIA,
//                EndBlocks.END_MOSS
//        );
//        BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.VAIOLUSH_FERN, EndBlocks.END_MOSS);
//        BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.FRACTURN, EndBlocks.END_MOSS);
//        BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.SALTEAGO, EndBlocks.END_MOSS);
//        BonemealAPI.addLandGrass(
//                EndBiomes.GLOWING_GRASSLANDS.getID(),
//                EndBlocks.CREEPING_MOSS,
//                EndBlocks.END_MOSS,
//                0.1F
//        );
//        BonemealAPI.addLandGrass(
//                EndBiomes.GLOWING_GRASSLANDS.getID(),
//                EndBlocks.UMBRELLA_MOSS,
//                EndBlocks.END_MOSS,
//                0.1F
//        );
//        BonemealAPI.addLandGrass(
//                EndBiomes.GLOWING_GRASSLANDS.getID(),
//                EndBlocks.TWISTED_UMBRELLA_MOSS,
//                EndBlocks.END_MOSS,
//                0.1F
//        );

//        BonemealAPI.addLandGrass(EndBlocks.CREEPING_MOSS, EndBlocks.END_MYCELIUM);
//        BonemealAPI.addLandGrass(EndBlocks.UMBRELLA_MOSS, EndBlocks.END_MYCELIUM);


//        BonemealAPI.addLandGrass(EndBlocks.JUNGLE_GRASS, EndBlocks.JUNGLE_MOSS);
//        BonemealAPI.addLandGrass(EndBlocks.TWISTED_UMBRELLA_MOSS, EndBlocks.JUNGLE_MOSS);
//        BonemealAPI.addLandGrass(EndBlocks.SMALL_JELLYSHROOM, EndBlocks.JUNGLE_MOSS, 0.1F);

//        BonemealAPI.addLandGrass(EndBlocks.ORANGO, EndBlocks.RUTISCUS);
//        BonemealAPI.addLandGrass(EndBlocks.AERIDIUM, EndBlocks.RUTISCUS, 0.2F);
//        BonemealAPI.addLandGrass(EndBlocks.LUTEBUS, EndBlocks.RUTISCUS, 0.2F);
//        BonemealAPI.addLandGrass(EndBlocks.LAMELLARIUM, EndBlocks.RUTISCUS);
//
//        BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS.getID(), EndBlocks.AERIDIUM, EndBlocks.RUTISCUS, 0.2F);
//        BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS.getID(), EndBlocks.LAMELLARIUM, EndBlocks.RUTISCUS);
//        BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS.getID(), EndBlocks.BOLUX_MUSHROOM, EndBlocks.RUTISCUS, 0.05F);

//        BonemealAPI.addLandGrass(
//                EndBlocks.GLOBULAGUS,
//                EndBlocks.SANGNUM,
//                EndBlocks.MOSSY_OBSIDIAN,
//                EndBlocks.MOSSY_DRAGON_BONE
//        );
//        BonemealAPI.addLandGrass(
//                EndBlocks.CLAWFERN,
//                EndBlocks.SANGNUM,
//                EndBlocks.MOSSY_OBSIDIAN,
//                EndBlocks.MOSSY_DRAGON_BONE
//        );
//        BonemealAPI.addLandGrass(EndBlocks.SMALL_AMARANITA_MUSHROOM, EndBlocks.SANGNUM, 0.1F);


//        BonemealAPI.addLandGrass(EndBlocks.SMALL_AMARANITA_MUSHROOM, EndBlocks.MOSSY_DRAGON_BONE, 0.1F);
//        BonemealAPI.addLandGrass(EndBlocks.GLOBULAGUS, EndBlocks.MOSSY_DRAGON_BONE);
//        BonemealAPI.addLandGrass(EndBlocks.CLAWFERN, EndBlocks.MOSSY_DRAGON_BONE);
//        BonemealAPI.addLandGrass(EndBlocks.SMALL_AMARANITA_MUSHROOM, EndBlocks.MOSSY_DRAGON_BONE, 0.1F);

//        BonemealAPI.addLandGrass(EndBlocks.SMALL_AMARANITA_MUSHROOM, EndBlocks.MOSSY_OBSIDIAN, 0.1F);
//        BonemealAPI.addLandGrass(EndBlocks.GLOBULAGUS, EndBlocks.MOSSY_OBSIDIAN);
//        BonemealAPI.addLandGrass(EndBlocks.CLAWFERN, EndBlocks.MOSSY_OBSIDIAN);
//        BonemealAPI.addLandGrass(EndBlocks.SMALL_AMARANITA_MUSHROOM, EndBlocks.MOSSY_OBSIDIAN, 0.1F);

        Block[] charnias = new Block[]{
                EndBlocks.CHARNIA_CYAN,
                EndBlocks.CHARNIA_GREEN,
                EndBlocks.CHARNIA_ORANGE,
                EndBlocks.CHARNIA_LIGHT_BLUE,
                EndBlocks.CHARNIA_PURPLE,
                EndBlocks.CHARNIA_RED
        };
//        List<Block> terrain = Lists.newArrayList();
//        EndBlocks.getModBlocks().forEach(block -> {
//            if (block instanceof EndTerrainBlock) {
//                terrain.add(block);
//            }
//        });
//        terrain.add(Blocks.END_STONE);
//        terrain.add(EndBlocks.ENDSTONE_DUST);
//        terrain.add(EndBlocks.CAVE_MOSS);
//        terrain.add(EndBlocks.SULPHURIC_ROCK.stone);
//        terrain.add(EndBlocks.VIOLECITE.stone);
//        terrain.add(EndBlocks.FLAVOLITE.stone);
//        terrain.add(EndBlocks.AZURE_JADESTONE.stone);
//        terrain.add(EndBlocks.VIRID_JADESTONE.stone);
//        terrain.add(EndBlocks.SANDY_JADESTONE.stone);
//        terrain.add(EndBlocks.BRIMSTONE);
//        Block[] terrainBlocks = terrain.toArray(new Block[terrain.size()]);
        for (Block charnia : charnias) {
            TagManager.BLOCKS.add(EndTags.BONEMEAL_SOURCE_WATER_GRASS, charnia);
            //BonemealAPI.addWaterGrass(charnia, terrainBlocks);
        }

        BonemealAPI.INSTANCE.addSpreadableBlocks(
                EndTags.BONEMEAL_TARGET_WATER_GRASS,
                new WaterGrassSpreader(EndTags.BONEMEAL_SOURCE_WATER_GRASS)
        );

        BonemealAPI.INSTANCE.addSpreadableBlocks(
                EndTags.BONEMEAL_TARGET_DRAGON_BONE,
                EndTags.BONEMEAL_SOURCE_DRAGON_BONE
        );
    }
}
