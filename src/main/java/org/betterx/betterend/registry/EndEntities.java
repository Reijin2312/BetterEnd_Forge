package org.betterx.betterend.registry;

import org.betterx.bclib.api.v2.spawning.SpawnRuleBuilder;
import org.betterx.bclib.entity.BCLEntityWrapper;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.entity.*;
import org.betterx.ui.ColorUtil;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap.Types;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegisterEvent;

import com.google.common.collect.Maps;

import java.util.Map;

public class EndEntities {
    public static final ResourceLocation DRAGONFLY_ID = BetterEnd.makeID("dragonfly");
    public static final ResourceLocation END_SLIME_ID = BetterEnd.makeID("end_slime");
    public static final ResourceLocation END_FISH_ID = BetterEnd.makeID("end_fish");
    public static final ResourceLocation SHADOW_WALKER_ID = BetterEnd.makeID("shadow_walker");
    public static final ResourceLocation CUBOZOA_ID = BetterEnd.makeID("cubozoa");
    public static final ResourceLocation SILK_MOTH_ID = BetterEnd.makeID("silk_moth");

    private static final EntityType<DragonflyEntity> DRAGONFLY_TYPE = buildEntityType(
            DragonflyEntity::new,
            MobCategory.AMBIENT,
            0.6F,
            0.5F,
            DRAGONFLY_ID
    );
    private static final EntityType<EndSlimeEntity> END_SLIME_TYPE = buildEntityType(
            EndSlimeEntity::new,
            MobCategory.MONSTER,
            2F,
            2F,
            END_SLIME_ID
    );
    private static final EntityType<EndFishEntity> END_FISH_TYPE = buildEntityType(
            EndFishEntity::new,
            MobCategory.WATER_AMBIENT,
            0.5F,
            0.5F,
            END_FISH_ID
    );
    private static final EntityType<ShadowWalkerEntity> SHADOW_WALKER_TYPE = buildEntityType(
            ShadowWalkerEntity::new,
            MobCategory.MONSTER,
            0.6F,
            1.95F,
            SHADOW_WALKER_ID
    );
    private static final EntityType<CubozoaEntity> CUBOZOA_TYPE = buildEntityType(
            CubozoaEntity::new,
            MobCategory.WATER_AMBIENT,
            0.6F,
            1F,
            CUBOZOA_ID
    );
    private static final EntityType<SilkMothEntity> SILK_MOTH_TYPE = buildEntityType(
            SilkMothEntity::new,
            MobCategory.AMBIENT,
            0.6F,
            0.6F,
            SILK_MOTH_ID
    );

    public static final BCLEntityWrapper<DragonflyEntity> DRAGONFLY = new BCLEntityWrapper<>(
            DRAGONFLY_TYPE,
            Configs.ENTITY_CONFIG.getBooleanRoot(DRAGONFLY_ID.getPath(), true)
    );
    public static final BCLEntityWrapper<EndSlimeEntity> END_SLIME = new BCLEntityWrapper<>(
            END_SLIME_TYPE,
            Configs.ENTITY_CONFIG.getBooleanRoot(END_SLIME_ID.getPath(), true)
    );
    public static final BCLEntityWrapper<EndFishEntity> END_FISH = new BCLEntityWrapper<>(
            END_FISH_TYPE,
            Configs.ENTITY_CONFIG.getBooleanRoot(END_FISH_ID.getPath(), true)
    );
    public static final BCLEntityWrapper<ShadowWalkerEntity> SHADOW_WALKER = new BCLEntityWrapper<>(
            SHADOW_WALKER_TYPE,
            Configs.ENTITY_CONFIG.getBooleanRoot(SHADOW_WALKER_ID.getPath(), true)
    );
    public static final BCLEntityWrapper<CubozoaEntity> CUBOZOA = new BCLEntityWrapper<>(
            CUBOZOA_TYPE,
            Configs.ENTITY_CONFIG.getBooleanRoot(CUBOZOA_ID.getPath(), true)
    );
    public static final BCLEntityWrapper<SilkMothEntity> SILK_MOTH = new BCLEntityWrapper<>(
            SILK_MOTH_TYPE,
            Configs.ENTITY_CONFIG.getBooleanRoot(SILK_MOTH_ID.getPath(), true)
    );

    private static final Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> ATTR_BUILDERS = Maps.newHashMap();
    private static boolean spawnEggsRegistered;
    private static boolean spawnRulesRegistered;

    public static void register() {
        setupSpawnRules();
    }

    public static void registerSpawnEggs() {
        if (spawnEggsRegistered) {
            return;
        }
        spawnEggsRegistered = true;
        EndItems.registerEndEgg(
                "spawn_egg_dragonfly",
                DRAGONFLY_TYPE,
                ColorUtil.color(32, 42, 176),
                ColorUtil.color(115, 225, 249)
        );
        EndItems.registerEndEgg(
                "spawn_egg_end_slime",
                END_SLIME_TYPE,
                ColorUtil.color(28, 28, 28),
                ColorUtil.color(99, 11, 99)
        );
        EndItems.registerEndEgg(
                "spawn_egg_end_fish",
                END_FISH_TYPE,
                ColorUtil.color(3, 50, 76),
                ColorUtil.color(120, 206, 255)
        );
        EndItems.registerEndEgg(
                "spawn_egg_shadow_walker",
                SHADOW_WALKER_TYPE,
                ColorUtil.color(30, 30, 30),
                ColorUtil.color(5, 5, 5)
        );
        EndItems.registerEndEgg(
                "spawn_egg_cubozoa",
                CUBOZOA_TYPE,
                ColorUtil.color(151, 77, 181),
                ColorUtil.color(93, 176, 238)
        );
        EndItems.registerEndEgg(
                "spawn_egg_silk_moth",
                SILK_MOTH_TYPE,
                ColorUtil.color(198, 138, 204),
                ColorUtil.color(242, 220, 236)
        );
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ENTITY_TYPE)) {
            return;
        }
        event.register(Registries.ENTITY_TYPE, helper -> {
            helper.register(DRAGONFLY_ID, DRAGONFLY_TYPE);
            helper.register(END_SLIME_ID, END_SLIME_TYPE);
            helper.register(END_FISH_ID, END_FISH_TYPE);
            helper.register(SHADOW_WALKER_ID, SHADOW_WALKER_TYPE);
            helper.register(CUBOZOA_ID, CUBOZOA_TYPE);
            helper.register(SILK_MOTH_ID, SILK_MOTH_TYPE);
            setupSpawnRules();
        });
    }

    public static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        if (ATTR_BUILDERS.isEmpty()) {
            registerAttribute(DRAGONFLY_TYPE, DragonflyEntity.createMobAttributes());
            registerAttribute(END_SLIME_TYPE, EndSlimeEntity.createMobAttributes());
            registerAttribute(END_FISH_TYPE, EndFishEntity.createMobAttributes());
            registerAttribute(SHADOW_WALKER_TYPE, ShadowWalkerEntity.createMobAttributes());
            registerAttribute(CUBOZOA_TYPE, CubozoaEntity.createMobAttributes());
            registerAttribute(SILK_MOTH_TYPE, SilkMothEntity.createMobAttributes());
        }
        ATTR_BUILDERS.forEach((type, builder) -> event.put(type, builder.build()));
    }

    private static void setupSpawnRules() {
        if (spawnRulesRegistered) {
            return;
        }
        if (DRAGONFLY == null || SILK_MOTH == null || END_SLIME == null || SHADOW_WALKER == null || END_FISH == null || CUBOZOA == null) {
            return;
        }
        // Air //
        SpawnRuleBuilder.start(DRAGONFLY).aboveGround(2).maxNearby(4, 32).buildNoRestrictions(Types.MOTION_BLOCKING);
        SpawnRuleBuilder.start(SILK_MOTH).aboveGround(2).maxNearby(4, 32).buildNoRestrictions(Types.MOTION_BLOCKING);

        // Land //
        SpawnRuleBuilder
                .start(END_SLIME)
                .notPeaceful()
                .maxNearby(6, 32)
                .onlyOnValidBlocks()
                .customRule(EndSlimeEntity::canSpawn)
                .buildOnGround(Types.MOTION_BLOCKING_NO_LEAVES);

        SpawnRuleBuilder.start(SHADOW_WALKER)
                        .vanillaHostile()
                        .onlyOnValidBlocks()
                        .maxNearby(8, 64)
                        .buildOnGround(Types.MOTION_BLOCKING);

        // Water //
        SpawnRuleBuilder.start(END_FISH).maxNearby(16, 16).buildInWater(Types.MOTION_BLOCKING_NO_LEAVES);
        SpawnRuleBuilder.start(CUBOZOA).maxNearby(16, 16).buildInWater(Types.MOTION_BLOCKING_NO_LEAVES);

        spawnRulesRegistered = true;
    }

    private static <T extends Mob> EntityType<T> buildEntityType(
            EntityFactory<T> factory,
            MobCategory category,
            float width,
            float height,
            ResourceLocation id
    ) {
        return EntityType.Builder.of(factory, category).sized(width, height).build(id.toString());
    }

    private static void registerAttribute(EntityType<? extends LivingEntity> entity, AttributeSupplier.Builder builder) {
        ATTR_BUILDERS.put(entity, builder);
    }
}
