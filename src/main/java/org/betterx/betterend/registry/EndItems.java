package org.betterx.betterend.registry;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.items.BaseArmorItem;
import org.betterx.bclib.items.ModelProviderItem;
import org.betterx.bclib.items.tool.BaseAxeItem;
import org.betterx.bclib.items.tool.BaseHoeItem;
import org.betterx.bclib.items.tool.BaseShovelItem;
import org.betterx.bclib.items.tool.BaseSwordItem;
import org.betterx.bclib.registry.BaseRegistry;
import org.betterx.bclib.registry.ItemRegistry;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.config.Configs;
import org.betterx.betterend.item.*;
import org.betterx.betterend.item.material.EndArmorMaterial;
import org.betterx.betterend.item.material.EndToolMaterial;
import org.betterx.betterend.item.tool.EndHammerItem;
import org.betterx.betterend.item.tool.EndPickaxe;
import org.betterx.betterend.util.DebugHelpers;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;

import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class EndItems {
    private static final ItemRegistry REGISTRY = new ItemRegistry(Configs.ITEM_CONFIG);

    // Materials //
    public final static Item ENDER_DUST = registerEndItem("ender_dust");
    public final static Item ENDER_SHARD = registerEndItem("ender_shard");
    public final static Item AETERNIUM_INGOT = registerEndItem(
            "aeternium_ingot",
            new ModelProviderItem(makeEndItemSettings().fireResistant())
    );
    public final static Item AETERNIUM_FORGED_PLATE = registerEndItem(
            "aeternium_forged_plate",
            new ModelProviderItem(makeEndItemSettings().fireResistant())
    );
    public final static Item END_LILY_LEAF = registerEndItem("end_lily_leaf");
    public final static Item END_LILY_LEAF_DRIED = registerEndItem("end_lily_leaf_dried");
    public final static Item CRYSTAL_SHARDS = registerEndItem("crystal_shards");
    public final static Item RAW_AMBER = registerEndItem("raw_amber");
    public final static Item AMBER_GEM = registerEndItem("amber_gem");
    public final static Item GLOWING_BULB = registerEndItem("glowing_bulb");
    public final static Item CRYSTALLINE_SULPHUR = registerEndItem("crystalline_sulphur");
    public final static Item HYDRALUX_PETAL = registerEndItem("hydralux_petal");
    public final static Item GELATINE = registerEndItem("gelatine");
    public static final Item ETERNAL_CRYSTAL = registerEndItem("eternal_crystal", new EternalCrystalItem());
    public final static Item ENCHANTED_PETAL = registerEndItem("enchanted_petal", new EnchantedItem(HYDRALUX_PETAL));
    public final static Item LEATHER_STRIPE = registerEndItem("leather_stripe");
    public final static Item LEATHER_WRAPPED_STICK = registerEndItem("leather_wrapped_stick");
    public final static Item SILK_FIBER = registerEndItem("silk_fiber");
    public final static Item LUMECORN_ROD = registerEndItem("lumecorn_rod");
    public final static Item SILK_MOTH_MATRIX = registerEndItem("silk_moth_matrix");
    public final static Item ENCHANTED_MEMBRANE = registerEndItem(
            "enchanted_membrane",
            new EnchantedItem(Items.PHANTOM_MEMBRANE)
    );

    // Music Discs
    public final static Item MUSIC_DISC_STRANGE_AND_ALIEN = registerEndDisc(
            "music_disc_strange_and_alien",
            0,
            EndSounds.RECORD_STRANGE_AND_ALIEN_EVENT,
            (4 * 60) + 26
    );
    public final static Item MUSIC_DISC_GRASPING_AT_STARS = registerEndDisc(
            "music_disc_grasping_at_stars",
            0,
            EndSounds.RECORD_GRASPING_AT_STARS_EVENT,
            (8 * 60) + 48
    );
    public final static Item MUSIC_DISC_ENDSEEKER = registerEndDisc(
            "music_disc_endseeker",
            0,
            EndSounds.RECORD_ENDSEEKER_EVENT,
            (7 * 60) + 41
    );
    public final static Item MUSIC_DISC_EO_DRACONA = registerEndDisc(
            "music_disc_eo_dracona",
            0,
            EndSounds.RECORD_EO_DRACONA_EVENT,
            (5 * 60) + 53
    );

    // Armor //
    public static final Item AETERNIUM_HELMET = registerEndItem(
            "aeternium_helmet",
            new BaseArmorItem(
                    EndArmorMaterial.AETERNIUM,
                    ArmorItem.Type.HELMET,
                    makeEndItemSettings().fireResistant()
            )
    );
    public static final Item AETERNIUM_CHESTPLATE = registerEndItem(
            "aeternium_chestplate",
            new BaseArmorItem(
                    EndArmorMaterial.AETERNIUM,
                    ArmorItem.Type.CHESTPLATE,
                    makeEndItemSettings().fireResistant()
            )
    );
    public static final Item AETERNIUM_LEGGINGS = registerEndItem(
            "aeternium_leggings",
            new BaseArmorItem(
                    EndArmorMaterial.AETERNIUM,
                    ArmorItem.Type.LEGGINGS,
                    makeEndItemSettings().fireResistant()
            )
    );
    public static final Item AETERNIUM_BOOTS = registerEndItem(
            "aeternium_boots",
            new BaseArmorItem(
                    EndArmorMaterial.AETERNIUM,
                    ArmorItem.Type.BOOTS,
                    makeEndItemSettings().fireResistant()
            )
    );
    public static final Item CRYSTALITE_HELMET = registerEndItem("crystalite_helmet", new CrystaliteHelmet());
    public static final Item CRYSTALITE_CHESTPLATE = registerEndItem(
            "crystalite_chestplate",
            new CrystaliteChestplate()
    );
    public static final Item CRYSTALITE_LEGGINGS = registerEndItem("crystalite_leggings", new CrystaliteLeggings());
    public static final Item CRYSTALITE_BOOTS = registerEndItem("crystalite_boots", new CrystaliteBoots());
    public static final Item ARMORED_ELYTRA = registerEndItem(
            "elytra_armored",
            new ArmoredElytra(
                    "elytra_armored",
                    EndArmorMaterial.AETERNIUM,
                    Items.PHANTOM_MEMBRANE,
                    900,
                    0.97D,
                    true
            )
    );
    public static final Item CRYSTALITE_ELYTRA = registerEndItem("elytra_crystalite", new CrystaliteElytra(650, 1.0D));

    // Tools //
    public static final TieredItem AETERNIUM_SHOVEL = registerEndTool("aeternium_shovel", new BaseShovelItem(
            EndToolMaterial.AETERNIUM, 1.5F, -3.0F, makeEndItemSettings().fireResistant()));
    public static final TieredItem AETERNIUM_SWORD = registerEndTool(
            "aeternium_sword",
            new BaseSwordItem(
                    EndToolMaterial.AETERNIUM,
                    3,
                    -2.4F,
                    makeEndItemSettings().fireResistant()
            )
    );
    public static final TieredItem AETERNIUM_PICKAXE = registerEndTool(
            "aeternium_pickaxe",
            new EndPickaxe(
                    EndToolMaterial.AETERNIUM,
                    1,
                    -2.8F,
                    makeEndItemSettings().fireResistant()
            )
    );
    public static final TieredItem AETERNIUM_AXE = registerEndTool(
            "aeternium_axe",
            new BaseAxeItem(
                    EndToolMaterial.AETERNIUM,
                    5.0F,
                    -3.0F,
                    makeEndItemSettings().fireResistant()
            )
    );
    public static final TieredItem AETERNIUM_HOE = registerEndTool(
            "aeternium_hoe",
            new BaseHoeItem(
                    EndToolMaterial.AETERNIUM,
                    -3,
                    0.0F,
                    makeEndItemSettings().fireResistant()
            )
    );
    public static final TieredItem AETERNIUM_HAMMER = registerEndTool(
            "aeternium_hammer",
            new EndHammerItem(
                    EndToolMaterial.AETERNIUM,
                    6.0F,
                    -3.0F,
                    0.3D,
                    makeEndItemSettings().fireResistant()
            )
    );

    // Toolparts //
    public final static Item AETERNIUM_SHOVEL_HEAD = registerEndItem(
            "aeternium_shovel_head",
            new ModelProviderItem(makeEndItemSettings().fireResistant())
    );
    public final static Item AETERNIUM_PICKAXE_HEAD = registerEndItem(
            "aeternium_pickaxe_head",
            new ModelProviderItem(makeEndItemSettings().fireResistant())
    );
    public final static Item AETERNIUM_AXE_HEAD = registerEndItem(
            "aeternium_axe_head",
            new ModelProviderItem(makeEndItemSettings().fireResistant())
    );
    public final static Item AETERNIUM_HOE_HEAD = registerEndItem(
            "aeternium_hoe_head",
            new ModelProviderItem(makeEndItemSettings().fireResistant())
    );
    public final static Item AETERNIUM_HAMMER_HEAD = registerEndItem(
            "aeternium_hammer_head",
            new ModelProviderItem(makeEndItemSettings().fireResistant())
    );
    public final static Item AETERNIUM_SWORD_BLADE = registerEndItem(
            "aeternium_sword_blade",
            new ModelProviderItem(makeEndItemSettings().fireResistant())
    );
    public final static Item AETERNIUM_SWORD_HANDLE = registerEndItem(
            "aeternium_sword_handle",
            new ModelProviderItem(makeEndItemSettings().fireResistant())
    );

    // ITEM_HAMMERS //
    public static final TieredItem IRON_HAMMER = registerEndTool(
            "iron_hammer",
            new EndHammerItem(
                    Tiers.IRON,
                    5.0F,
                    -3.2F,
                    0.2D,
                    makeEndItemSettings()
            )
    );
    public static final TieredItem GOLDEN_HAMMER = registerEndTool(
            "golden_hammer",
            new EndHammerItem(
                    Tiers.GOLD,
                    4.5F,
                    -3.4F,
                    0.3D,
                    makeEndItemSettings()
            )
    );
    public static final TieredItem DIAMOND_HAMMER = registerEndTool(
            "diamond_hammer",
            new EndHammerItem(
                    Tiers.DIAMOND,
                    5.5F,
                    -3.1F,
                    0.2D,
                    makeEndItemSettings()
            )
    );
    public static final TieredItem NETHERITE_HAMMER = registerEndTool(
            "netherite_hammer",
            new EndHammerItem(
                    Tiers.NETHERITE,
                    5.0F,
                    -3.0F,
                    0.2D,
                    makeEndItemSettings().fireResistant()
            )
    );

    // Food //
    public final static Item SHADOW_BERRY_RAW = registerEndFood("shadow_berry_raw", 4, 0.5F);
    public final static Item SHADOW_BERRY_COOKED = registerEndFood("shadow_berry_cooked", 6, 0.7F);
    public final static Item END_FISH_RAW = registerEndFood("end_fish_raw", Foods.SALMON);
    public final static Item END_FISH_COOKED = registerEndFood("end_fish_cooked", Foods.COOKED_SALMON);
    public final static Item BUCKET_END_FISH = registerEndItem(
            "bucket_end_fish",
            new EndBucketItem(EndEntities.END_FISH.type())
    );
    public final static Item BUCKET_CUBOZOA = registerEndItem(
            "bucket_cubozoa",
            new EndBucketItem(EndEntities.CUBOZOA.type())
    );
    public final static Item SWEET_BERRY_JELLY = registerEndFood("sweet_berry_jelly", 8, 0.7F);
    public final static Item SHADOW_BERRY_JELLY = registerEndFood(
            "shadow_berry_jelly",
            6,
            0.8F,
            new MobEffectInstance(MobEffects.NIGHT_VISION, 400)
    );
    public final static Item BLOSSOM_BERRY_JELLY = registerEndFood("blossom_berry_jelly", 8, 0.7F);
    public final static Item BLOSSOM_BERRY = registerEndFood("blossom_berry", Foods.APPLE);
    public final static Item AMBER_ROOT_RAW = registerEndFood("amber_root_raw", 2, 0.8F);
    public final static Item CHORUS_MUSHROOM_RAW = registerEndFood("chorus_mushroom_raw", 3, 0.5F);
    public final static Item CHORUS_MUSHROOM_COOKED = registerEndFood("chorus_mushroom_cooked", Foods.MUSHROOM_STEW);
    public final static Item BOLUX_MUSHROOM_COOKED = registerEndFood("bolux_mushroom_cooked", Foods.MUSHROOM_STEW);
    public final static Item CAVE_PUMPKIN_PIE = registerEndFood("cave_pumpkin_pie", Foods.PUMPKIN_PIE);

    // Drinks //
    public final static Item UMBRELLA_CLUSTER_JUICE = registerEndDrink("umbrella_cluster_juice", 5, 0.7F);

    public static List<Item> getModItems() {
        return BaseRegistry.getModItems(BetterEnd.MOD_ID);
    }

    public static Item registerEndDisc(String name, int power, SoundEvent sound, int lengthInSeconds) {
        return getItemRegistry().registerDisc(BetterEnd.makeID(name), power, sound, lengthInSeconds);
    }

    public static Item registerEndItem(String name) {
        return getItemRegistry().register(BetterEnd.makeID(name));
    }

    public static Item registerEndItem(String name, Item item) {
        if (item instanceof EndArmorItem) {
            return getItemRegistry().register(BetterEnd.makeID(name), item, "armour");
        }
        return getItemRegistry().register(BetterEnd.makeID(name), item);
    }

    public static TieredItem registerEndTool(String name, TieredItem item) {
        if (!Configs.ITEM_CONFIG.getBoolean("tools", name, true)) {
            return item;
        }
        return (TieredItem) getItemRegistry().registerTool(BetterEnd.makeID(name), item);
    }

    public static Item registerEndEgg(String name, EntityType<? extends Mob> type, int background, int dots) {
        return getItemRegistry().registerEgg(BetterEnd.makeID(name), type, background, dots);
    }

    public static Item registerEndFood(String name, int hunger, float saturation, MobEffectInstance... effects) {
        return getItemRegistry().registerFood(BetterEnd.makeID(name), hunger, saturation, effects);
    }

    public static Item registerEndFood(String name, FoodProperties foodComponent) {
        return getItemRegistry().registerFood(BetterEnd.makeID(name), foodComponent);
    }

    public static Item registerEndDrink(String name, int hunger, float saturation) {
        return getItemRegistry().registerDrink(BetterEnd.makeID(name), hunger, saturation);
    }

    public static Item.Properties makeEndItemSettings() {
        return getItemRegistry().makeItemSettings();
    }

    @NotNull
    public static ItemRegistry getItemRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static void register() {
        if (BCLib.isDevEnvironment()) {
            DebugHelpers.generateDebugItems();
        }
    }

    @ApiStatus.Internal
    public static void ensureStaticallyLoaded() {
        // triggers class loading of static fields
    }

}
