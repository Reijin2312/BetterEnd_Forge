package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.EndStoneSmelter;
import org.betterx.betterend.blocks.basis.PedestalBlock;
import org.betterx.betterend.blocks.entities.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.registries.RegisterEvent;

public class EndBlockEntities {
    public static BlockEntityType<EndStoneSmelterBlockEntity> END_STONE_SMELTER;
    public static BlockEntityType<PedestalBlockEntity> PEDESTAL;
    public static BlockEntityType<EternalPedestalEntity> ETERNAL_PEDESTAL;
    public static BlockEntityType<InfusionPedestalEntity> INFUSION_PEDESTAL;
    public static BlockEntityType<BlockEntityHydrothermalVent> HYDROTHERMAL_VENT;

    public static void register() {
        // no-op; registration happens via RegisterEvent
    }

    public static void register(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
            return;
        }
        event.register(Registries.BLOCK_ENTITY_TYPE, helper -> {
            END_STONE_SMELTER = BlockEntityType.Builder.of(
                    EndStoneSmelterBlockEntity::new,
                    EndBlocks.END_STONE_SMELTER
            ).build(null);
            helper.register(BetterEnd.makeID(EndStoneSmelter.ID), END_STONE_SMELTER);

            PEDESTAL = BlockEntityType.Builder.of(
                    PedestalBlockEntity::new,
                    getPedestals()
            ).build(null);
            helper.register(BetterEnd.makeID("pedestal"), PEDESTAL);

            ETERNAL_PEDESTAL = BlockEntityType.Builder.of(
                    EternalPedestalEntity::new,
                    EndBlocks.ETERNAL_PEDESTAL
            ).build(null);
            helper.register(BetterEnd.makeID("eternal_pedestal"), ETERNAL_PEDESTAL);

            INFUSION_PEDESTAL = BlockEntityType.Builder.of(
                    InfusionPedestalEntity::new,
                    EndBlocks.INFUSION_PEDESTAL
            ).build(null);
            helper.register(BetterEnd.makeID("infusion_pedestal"), INFUSION_PEDESTAL);

            HYDROTHERMAL_VENT = BlockEntityType.Builder.of(
                    BlockEntityHydrothermalVent::new,
                    EndBlocks.HYDROTHERMAL_VENT
            ).build(null);
            helper.register(BetterEnd.makeID("hydrother_malvent"), HYDROTHERMAL_VENT);
        });
    }

    static Block[] getPedestals() {
        return EndBlocks.getModBlocks()
                        .stream()
                        .filter(block -> block instanceof PedestalBlock && !((PedestalBlock) block).hasUniqueEntity())
                        .toArray(Block[]::new);
    }
}
