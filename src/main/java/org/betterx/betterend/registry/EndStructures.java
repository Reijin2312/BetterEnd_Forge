package org.betterx.betterend.registry;

import org.betterx.bclib.api.v2.levelgen.structures.BCLStructure;
import org.betterx.bclib.api.v2.levelgen.structures.BCLStructureBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.world.structures.features.*;
import org.betterx.betterend.world.structures.piece.*;
import org.betterx.betterend.world.structures.village.VillagePools;
import org.betterx.worlds.together.tag.v3.TagManager;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class EndStructures {
    private static final Map<ResourceLocation, StructurePieceType> PIECE_TYPES = new LinkedHashMap<>();

    public static final StructurePieceType VOXEL_PIECE = register("voxel", VoxelPiece::new);
    public static final StructurePieceType MOUNTAIN_PIECE = register("mountain_piece", CrystalMountainPiece::new);
    public static final StructurePieceType CAVE_PIECE = register("cave_piece", CavePiece::new);
    public static final StructurePieceType LAKE_PIECE = register("lake_piece", LakePiece::new);
    public static final StructurePieceType PAINTED_MOUNTAIN_PIECE = register(
            "painted_mountain_piece",
            PaintedMountainPiece::new
    );
    public static final StructurePieceType NBT_PIECE = register("nbt_piece", NBTPiece::new);

    public static final BCLStructure<GiantMossyGlowshroomStructure> GIANT_MOSSY_GLOWSHROOM = BCLStructureBuilder
            .start(BetterEnd.makeID("giant_mossy_glowshroom"), GiantMossyGlowshroomStructure::new)
            .step(Decoration.SURFACE_STRUCTURES)
            .randomPlacement(16, 8)
            .build();

    public static final BCLStructure<MegaLakeStructure> MEGALAKE = BCLStructureBuilder
            .start(BetterEnd.makeID("megalake"), MegaLakeStructure::new)
            .step(Decoration.LAKES)
            .randomPlacement(4, 1)
            .build();

    public static final BCLStructure<MegaLakeSmallStructure> MEGALAKE_SMALL = BCLStructureBuilder
            .start(BetterEnd.makeID("megalake_small"), MegaLakeSmallStructure::new)
            .step(Decoration.LAKES)
            .randomPlacement(4, 1)
            .build();

    public static final BCLStructure<MountainStructure> MOUNTAIN = BCLStructureBuilder
            .start(BetterEnd.makeID("mountain"), MountainStructure::new)
            .step(Decoration.RAW_GENERATION)
            .randomPlacement(3, 2)
            .build();
    public static final BCLStructure<PaintedMountainStructure> PAINTED_MOUNTAIN = BCLStructureBuilder
            .start(BetterEnd.makeID("painted_mountain"), PaintedMountainStructure::new)
            .step(Decoration.RAW_GENERATION)
            .randomPlacement(3, 2)
            .build();
    public static final BCLStructure<EternalPortalStructure> ETERNAL_PORTAL = BCLStructureBuilder
            .start(BetterEnd.makeID("eternal_portal"), EternalPortalStructure::new)
            .step(Decoration.RAW_GENERATION)
            .randomPlacement(40, 12)
            .build();
    public static final BCLStructure<GiantIceStarStructure> GIANT_ICE_STAR = BCLStructureBuilder
            .start(BetterEnd.makeID("giant_ice_star"), GiantIceStarStructure::new)
            .step(Decoration.SURFACE_STRUCTURES)
            .randomPlacement(16, 8)
            .build();

    public static final BCLStructure<JigsawStructure> END_VILLAGE = BCLStructureBuilder
            .jigsaw(BetterEnd.makeID("end_village"))
            .startPool(VillagePools.START)
            .adjustment(TerrainAdjustment.BEARD_THIN)
            .projectStartToHeightmap(Heightmap.Types.WORLD_SURFACE_WG)
            .maxDepth(6)
            .startHeight(ConstantHeight.of(VerticalAnchor.absolute(0)))
            .step(Decoration.SURFACE_STRUCTURES)
            .randomPlacement(34, 8)
            .build();

    public static void register() {
    }

    private static StructurePieceType register(String id, StructurePieceType pieceType) {
        ResourceLocation key = BetterEnd.makeID(id);
        StructurePieceType existing = PIECE_TYPES.get(key);
        if (existing != null) {
            return existing;
        }
        PIECE_TYPES.put(key, pieceType);
        return pieceType;
    }

    public static void addBiomeStructures(ResourceLocation biomeID, Holder<Biome> biome) {
        if (
                !biomeID.getNamespace().equals("minecraft")
                        && !biomeID.getNamespace().equals(BetterEnd.MOD_ID)
                        && !biomeID.getPath().contains("mountain")
                        && !biomeID.getPath().contains("lake")
        ) {
            TagManager.BIOMES.add(ETERNAL_PORTAL.biomeTag, biome.value());
        }
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.STRUCTURE_PIECE)) {
            return;
        }
        event.register(Registries.STRUCTURE_PIECE, helper -> {
            PIECE_TYPES.forEach(helper::register);
            PIECE_TYPES.clear();
        });
    }
}
