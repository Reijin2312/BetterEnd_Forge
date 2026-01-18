package org.betterx.betterend.registry;

import org.betterx.bclib.api.v2.poi.BCLPoiType;
import org.betterx.bclib.api.v2.poi.PoiManager;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.RunedFlavolite;

import com.google.common.collect.ImmutableSet;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.registries.RegisterEvent;

import java.util.HashSet;
import java.util.Set;

public class EndPoiTypes {
    public static BCLPoiType ETERNAL_PORTAL;
    public static BCLPoiType ETERNAL_PORTAL_FRAME;

    public static void register() {
        if (ETERNAL_PORTAL == null && EndBlocks.END_PORTAL_BLOCK != null) {
            ETERNAL_PORTAL = resolveExistingPoiType(EndBlocks.END_PORTAL_BLOCK);
        }
        if (ETERNAL_PORTAL == null && EndBlocks.END_PORTAL_BLOCK != null) {
            var portalStates = new HashSet<>(BCLPoiType.getBlockStates(EndBlocks.END_PORTAL_BLOCK));
            portalStates.removeIf(PoiTypes::hasPoi);
            if (!portalStates.isEmpty()) {
                try {
                    ETERNAL_PORTAL = PoiManager.register(
                            BetterEnd.makeID("eternal_portal"),
                            portalStates,
                            0,
                            1
                    );
                } catch (IllegalStateException ex) {
                    BetterEnd.LOGGER.warning("Skip eternal_portal POI registration: " + ex.getMessage());
                    ETERNAL_PORTAL = resolveExistingPoiType(EndBlocks.END_PORTAL_BLOCK);
                }
            }
        }

        if (ETERNAL_PORTAL_FRAME == null && EndBlocks.FLAVOLITE_RUNED_ETERNAL != null) {
            ETERNAL_PORTAL_FRAME = resolveExistingPoiType(EndBlocks.FLAVOLITE_RUNED_ETERNAL);
        }
        if (ETERNAL_PORTAL_FRAME == null && EndBlocks.FLAVOLITE_RUNED_ETERNAL != null) {
            var frameStates = new HashSet<>(Set.of(
                    EndBlocks.FLAVOLITE_RUNED_ETERNAL.defaultBlockState().setValue(RunedFlavolite.ACTIVATED, false)
            ));
            frameStates.removeIf(PoiTypes::hasPoi);
            if (!frameStates.isEmpty()) {
                try {
                    ETERNAL_PORTAL_FRAME = PoiManager.register(
                            BetterEnd.makeID("eternal_portal_frame"),
                            frameStates,
                            0,
                            1
                    );
                } catch (IllegalStateException ex) {
                    BetterEnd.LOGGER.warning("Skip eternal_portal_frame POI registration: " + ex.getMessage());
                    ETERNAL_PORTAL_FRAME = resolveExistingPoiType(EndBlocks.FLAVOLITE_RUNED_ETERNAL);
                }
            }
        }
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.POINT_OF_INTEREST_TYPE)) {
            return;
        }
        register();
    }

    private static BCLPoiType resolveExistingPoiType(Block block) {
        if (block == null) {
            return null;
        }
        Holder<PoiType> holder = findExistingPoiHolder(block);
        if (holder == null) {
            return null;
        }
        ResourceKey<PoiType> key = holder.unwrapKey().orElse(null);
        if (key == null) {
            return null;
        }
        return new BCLPoiType(
                key,
                holder.value(),
                ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates()),
                0,
                1
        );
    }

    private static Holder<PoiType> findExistingPoiHolder(Block block) {
        for (BlockState state : block.getStateDefinition().getPossibleStates()) {
            Holder<PoiType> holder = PoiTypes.forState(state).orElse(null);
            if (holder != null) {
                return holder;
            }
        }
        return null;
    }
}
