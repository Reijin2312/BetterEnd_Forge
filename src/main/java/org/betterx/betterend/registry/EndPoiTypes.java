package org.betterx.betterend.registry;

import org.betterx.bclib.api.v2.poi.BCLPoiType;
import org.betterx.bclib.api.v2.poi.PoiManager;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.RunedFlavolite;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class EndPoiTypes {
    public static final BCLPoiType ETERNAL_PORTAL = PoiManager.register(
            BetterEnd.makeID("eternal_portal"),
            ImmutableSet.copyOf(EndBlocks.END_PORTAL_BLOCK.getStateDefinition().getPossibleStates()),
            0, 1
    );

    public static final BCLPoiType ETERNAL_PORTAL_FRAME = PoiManager.register(
            BetterEnd.makeID("eternal_portal_frame"),
            Set.of(
                    EndBlocks.FLAVOLITE_RUNED_ETERNAL.defaultBlockState().setValue(RunedFlavolite.ACTIVATED, false)
            ),
            0, 1
    );

    public static void register() {

    }
}
