package org.betterx.betterend.world.structures.piece;

import org.betterx.bclib.complexmaterials.set.stone.StoneSlots;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndStructures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

/** Serializable multichunk piece for a ruined End-stone bridge. */
public class EndBridgePiece extends BasePiece {
    private static final BlockState DECK = Blocks.END_STONE_BRICKS.defaultBlockState();
    private static final BlockState CRACKED = EndBlocks.END_STONE_BRICK_VARIATIONS
            .getBlock(StoneSlots.CRACKED)
            .defaultBlockState();
    private static final BlockState WEATHERED = EndBlocks.END_STONE_BRICK_VARIATIONS
            .getBlock(StoneSlots.WEATHERED)
            .defaultBlockState();
    private static final BlockState RAILING = Blocks.END_STONE_BRICK_WALL.defaultBlockState();
    private static final int LANDING_LENGTH = 4;
    private static final int PILLAR_SPACING = 8;
    private static final int PILLAR_MAX_DROP = 12;

    private BlockPos start;
    private BlockPos end;
    private long seed;

    public EndBridgePiece(BlockPos start, BlockPos end, long seed) {
        super(EndStructures.END_BRIDGE_PIECE, (int) seed, null);
        this.start = start;
        this.end = end;
        this.seed = seed;
        makeBoundingBox();
    }

    public EndBridgePiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(EndStructures.END_BRIDGE_PIECE, tag);
        makeBoundingBox();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("start", NbtUtils.writeBlockPos(start));
        tag.put("end", NbtUtils.writeBlockPos(end));
        tag.putLong("seed", seed);
    }

    @Override
    protected void fromNbt(CompoundTag tag) {
        start = NbtUtils.readBlockPos(tag.getCompound("start"));
        end = NbtUtils.readBlockPos(tag.getCompound("end"));
        seed = tag.getLong("seed");
    }

    private void makeBoundingBox() {
        boundingBox = new BoundingBox(
                Math.min(start.getX(), end.getX()) - 6,
                Math.min(start.getY(), end.getY()) - PILLAR_MAX_DROP,
                Math.min(start.getZ(), end.getZ()) - 6,
                Math.max(start.getX(), end.getX()) + 6,
                Math.max(start.getY(), end.getY()) + 6,
                Math.max(start.getZ(), end.getZ()) + 6
        );
    }

    @Override
    public void postProcess(
            WorldGenLevel world,
            StructureManager structureManager,
            ChunkGenerator chunkGenerator,
            RandomSource random,
            BoundingBox box,
            ChunkPos chunkPos,
            BlockPos pivot
    ) {
        ChunkAccess chunk = world.getChunk(chunkPos.x, chunkPos.z);
        int chunkX = SectionPos.sectionToBlockCoord(chunkPos.x);
        int chunkZ = SectionPos.sectionToBlockCoord(chunkPos.z);
        int minX = Math.max(boundingBox.minX(), chunkX);
        int maxX = Math.min(boundingBox.maxX(), chunkX + 15);
        int minZ = Math.max(boundingBox.minZ(), chunkZ);
        int maxZ = Math.min(boundingBox.maxZ(), chunkZ + 15);
        if (minX > maxX || minZ > maxZ) {
            return;
        }

        double startX = start.getX() + 0.5;
        double startZ = start.getZ() + 0.5;
        double segmentX = end.getX() - start.getX();
        double segmentZ = end.getZ() - start.getZ();
        double segmentLengthSq = segmentX * segmentX + segmentZ * segmentZ;
        if (segmentLengthSq < 1.0) {
            return;
        }

        double segmentLength = Math.sqrt(segmentLengthSq);
        double rise = Mth.clamp(segmentLength / 20.0, 2.0, 5.0);
        MutableBlockPos pos = new MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                double t = ((x + 0.5 - startX) * segmentX + (z + 0.5 - startZ) * segmentZ)
                        / segmentLengthSq;
                double clampedT = Mth.clamp(t, 0.0, 1.0);
                double centerX = startX + clampedT * segmentX;
                double centerZ = startZ + clampedT * segmentZ;
                double perpendicular = Math.hypot(x + 0.5 - centerX, z + 0.5 - centerZ);
                double along = clampedT * segmentLength;
                boolean landing = Math.min(along, segmentLength - along) < LANDING_LENGTH;
                double halfWidth = landing ? 2.0 : 1.0;
                if (perpendicular > halfWidth + 0.5) {
                    continue;
                }

                int deckY = Mth.floor(Mth.lerp(clampedT, start.getY(), end.getY())
                        + rise * 4.0 * clampedT * (1.0 - clampedT) + 0.5);
                RandomSource columnRandom = RandomSource.create(
                        seed ^ (long) x * 0x9E3779B97F4A7C15L ^ (long) z * 0xC2B2AE3D27D4EB4FL
                );

                if (!landing && (isTerrain(chunk, pos.set(x, deckY, z))
                        || isTerrain(chunk, pos.set(x, deckY + 1, z)))) {
                    continue;
                }

                setIfReplaceable(chunk, pos.set(x, deckY, z), deckMaterial(columnRandom), landing);
                setIfReplaceable(chunk, pos.set(x, deckY - 1, z), deckMaterial(columnRandom), false);
                if (landing) {
                    setIfReplaceable(chunk, pos.set(x, deckY - 2, z), deckMaterial(columnRandom), false);
                } else if (perpendicular > halfWidth - 0.5 && columnRandom.nextFloat() >= 0.2F) {
                    setIfReplaceable(chunk, pos.set(x, deckY + 1, z), RAILING, false);
                }

                if (!landing && perpendicular < 0.5 && Math.round(along) % PILLAR_SPACING == 0) {
                    addPillar(chunk, pos, x, z, deckY, columnRandom);
                }
            }
        }
    }

    private static void addPillar(
            ChunkAccess chunk,
            MutableBlockPos pos,
            int x,
            int z,
            int deckY,
            RandomSource random
    ) {
        int bottom = Math.max(chunk.getMinBuildHeight(), deckY - 1 - PILLAR_MAX_DROP);
        int terrainY = Integer.MIN_VALUE;
        for (int y = deckY - 2; y >= bottom; y--) {
            if (isTerrain(chunk, pos.set(x, y, z))) {
                terrainY = y;
                break;
            }
        }
        for (int y = deckY - 2; y > terrainY && terrainY != Integer.MIN_VALUE; y--) {
            setIfReplaceable(chunk, pos.set(x, y, z), deckMaterial(random), false);
        }
    }

    private static BlockState deckMaterial(RandomSource random) {
        float value = random.nextFloat();
        if (value < 0.10F) {
            return WEATHERED;
        }
        if (value < 0.25F) {
            return CRACKED;
        }
        return DECK;
    }

    private static boolean isTerrain(ChunkAccess chunk, BlockPos pos) {
        return !isReplaceable(chunk.getBlockState(pos));
    }

    private static boolean isReplaceable(BlockState state) {
        return state.isAir() || BlocksHelper.replaceableOrPlant(state) || !state.getFluidState().isEmpty();
    }

    private static void setIfReplaceable(ChunkAccess chunk, BlockPos pos, BlockState state, boolean force) {
        if (force || isReplaceable(chunk.getBlockState(pos))) {
            chunk.setBlockState(pos, state, false);
        }
    }
}
