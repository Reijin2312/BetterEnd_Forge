package org.betterx.betterend.world.structures.village;

import org.betterx.bclib.api.v2.levelgen.structures.SingleEndPoolElement;
import org.betterx.bclib.api.v2.levelgen.structures.StructurePools;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.registry.EndProcessors;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import com.google.common.collect.ImmutableList;

import java.util.function.Function;

public class VillagePools {
    public static final ResourceKey<PlacedFeature> CHORUS_VILLAGE = ResourceKey.create(
            Registries.PLACED_FEATURE,
            BetterEnd.makeID("village_chorus")
    );
    public static ResourceKey<StructureTemplatePool> TERMINATORS_KEY = StructurePools
            .createKey(BetterEnd.makeID("village/terminators"));
    public static ResourceKey<StructureTemplatePool> START = StructurePools
            .createKey(BetterEnd.makeID("village/center_piece"));
    public static ResourceKey<StructureTemplatePool> HOUSES_KEY = StructurePools
            .createKey(BetterEnd.makeID("village/houses"));
    public static ResourceKey<StructureTemplatePool> STREET_KEY = StructurePools
            .createKey(BetterEnd.makeID("village/streets"));
    public static ResourceKey<StructureTemplatePool> STREET_DECO_KEY = StructurePools
            .createKey(BetterEnd.makeID("village/street_decorations"));
    public static ResourceKey<StructureTemplatePool> DECORATIONS_KEY = StructurePools
            .createKey(BetterEnd.makeID("village/decorations"));

    public static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> single(
            String name,
            Holder<StructureProcessorList> processor,
            int weight
    ) {
        return Pair.of(SingleEndPoolElement.end(BetterEnd.makeID(name), processor), weight);
    }

    public static void bootstrap(BootstapContext<StructureTemplatePool> ctx) {
        final HolderGetter<StructureProcessorList> processorGetter = ctx.lookup(Registries.PROCESSOR_LIST);
        final HolderGetter<PlacedFeature> featureGetter = ctx.lookup(Registries.PLACED_FEATURE);
        final HolderGetter<StructureTemplatePool> poolGetter = ctx.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<Biome> biomeGetter = ctx.lookup(Registries.BIOME);

        final Holder.Reference<StructureTemplatePool> emptyPool = poolGetter.getOrThrow(Pools.EMPTY);
        final Holder.Reference<StructureTemplatePool> terminatorPool = poolGetter.getOrThrow(VillagePools.TERMINATORS_KEY);

        final Holder.Reference<StructureProcessorList> mossifyProcessor = processorGetter.getOrThrow(EndProcessors.WEATHERED_10_PERCENT);
        final Holder.Reference<StructureProcessorList> crack = processorGetter.getOrThrow(EndProcessors.CRACK_20_PERCENT);
        final Holder.Reference<StructureProcessorList> cry = processorGetter.getOrThrow(EndProcessors.CRYING_10_PERCENT);
        final Holder.Reference<StructureProcessorList> crackAndWeather = processorGetter.getOrThrow(EndProcessors.CRACK_AND_WEATHER);
        final Holder.Reference<StructureProcessorList> endStreetProcessor = processorGetter.getOrThrow(EndProcessors.END_STREET);
        final Holder.Reference<StructureProcessorList> emptyProcessor = processorGetter.getOrThrow(
                ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("minecraft", "empty"))
        );

        final Holder.Reference<PlacedFeature> chorus = featureGetter.getOrThrow(CHORUS_VILLAGE);
        ctx.register(VillagePools.TERMINATORS_KEY, new StructureTemplatePool(
                emptyPool,
                ImmutableList.of(
                        single("village/terminators/stree_terminator_01", emptyProcessor, 1)
                ),
                StructureTemplatePool.Projection.TERRAIN_MATCHING
        ));
        ctx.register(VillagePools.START, new StructureTemplatePool(
                terminatorPool,
                ImmutableList.of(
                        single("village/center/light_pyramid_01", emptyProcessor, 2),
                        single("village/center/light_pyramid_02", emptyProcessor, 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));
        ctx.register(VillagePools.HOUSES_KEY, new StructureTemplatePool(
                terminatorPool,
                ImmutableList.of(
                        Pair.of(StructurePoolElement.empty(), 5),
                        single("village/houses/small_house_01", emptyProcessor, 4),
                        single("village/houses/small_house_02", emptyProcessor, 4),
                        single("village/houses/small_house_03", emptyProcessor, 2),
                        single("village/houses/small_house_04", emptyProcessor, 4),
                        single("village/houses/small_house_05", emptyProcessor, 4),
                        single("village/houses/small_house_06", emptyProcessor, 4),
                        single("village/houses/small_house_07", emptyProcessor, 4),
                        single("village/houses/small_house_08", emptyProcessor, 4),
                        single("village/houses/small_house_09", emptyProcessor, 4),
                        single("village/houses/small_house_10", emptyProcessor, 2),
                        single("village/houses/small_house_11", emptyProcessor, 1),
                        single("village/houses/small_house_12", emptyProcessor, 4),
                        single("village/houses/small_house_13", emptyProcessor, 4),
                        single("village/houses/small_house_14", emptyProcessor, 4),
                        single("village/houses/small_house_15", emptyProcessor, 4),
                        single("village/houses/small_house_16", emptyProcessor, 2),
                        single("village/houses/small_house_17", crackAndWeather, 4),
                        single("village/houses/animal_pen_01", emptyProcessor, 3),
                        single("village/decoration/stable_01", crack, 2),
                        single("village/decoration/pond_01", mossifyProcessor, 1),
                        single("village/decoration/respawn_01", endStreetProcessor, 1),
                        single("village/decoration/respawn_02", endStreetProcessor, 1),
                        single("village/decoration/fountain_01", endStreetProcessor, 1),
                        single("village/street_decoration/work_01", endStreetProcessor, 1)

                ),
                StructureTemplatePool.Projection.RIGID
        ));
        ctx.register(VillagePools.STREET_KEY, new StructureTemplatePool(
                terminatorPool,
                ImmutableList.of(
                        single("village/streets/street_01", endStreetProcessor, 6),
                        single("village/streets/street_02", endStreetProcessor, 5),
                        single("village/streets/street_03", endStreetProcessor, 7),
                        single("village/streets/street_04", endStreetProcessor, 6),
                        single("village/streets/curve_01", endStreetProcessor, 8),
                        single("village/streets/curve_02", endStreetProcessor, 8),
                        single("village/streets/t_crossing_01", endStreetProcessor, 4),
                        single("village/streets/t_crossing_02", endStreetProcessor, 4)
                ),
                StructureTemplatePool.Projection.TERRAIN_MATCHING
        ));
        ctx.register(VillagePools.STREET_DECO_KEY, new StructureTemplatePool(
                terminatorPool,
                ImmutableList.of(
                        Pair.of(StructurePoolElement.empty(), 5),
                        single("village/street_decoration/lamp_02", emptyProcessor, 4),
                        single("village/street_decoration/lamp_05", emptyProcessor, 2),
                        single("village/street_decoration/lamp_06", emptyProcessor, 3),
                        single("village/street_decoration/obsidian_01", cry, 2),
                        single("village/street_decoration/obsidian_02", cry, 3),
                        single("village/street_decoration/obsidian_03", cry, 2),
                        single("village/street_decoration/obsidian_04", cry, 2),
                        single("village/street_decoration/obsidian_05", cry, 1),
                        Pair.of(StructurePoolElement.feature(chorus), 2)
                ),
                StructureTemplatePool.Projection.RIGID
        ));
        ctx.register(VillagePools.DECORATIONS_KEY, new StructureTemplatePool(
                terminatorPool,
                ImmutableList.of(),
                StructureTemplatePool.Projection.RIGID
        ));
    }
}
