package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import net.minecraftforge.registries.RegisterEvent;

public class EndSounds {
    // Music
    public static Holder<SoundEvent> MUSIC_FOREST;
    public static Holder<SoundEvent> MUSIC_WATER;
    public static Holder<SoundEvent> MUSIC_DARK;
    public static Holder<SoundEvent> MUSIC_OPENSPACE;
    public static Holder<SoundEvent> MUSIC_CAVES;

    // Ambient
    public static Holder<SoundEvent> AMBIENT_FOGGY_MUSHROOMLAND;
    public static Holder<SoundEvent> AMBIENT_CHORUS_FOREST;
    public static Holder<SoundEvent> AMBIENT_MEGALAKE;
    public static Holder<SoundEvent> AMBIENT_DUST_WASTELANDS;
    public static Holder<SoundEvent> AMBIENT_MEGALAKE_GROVE;
    public static Holder<SoundEvent> AMBIENT_BLOSSOMING_SPIRES;
    public static Holder<SoundEvent> AMBIENT_SULPHUR_SPRINGS;
    public static Holder<SoundEvent> AMBIENT_UMBRELLA_JUNGLE;
    public static Holder<SoundEvent> AMBIENT_GLOWING_GRASSLANDS;
    public static Holder<SoundEvent> AMBIENT_CAVES;
    public static Holder<SoundEvent> AMBIENT_AMBER_LAND;
    public static Holder<SoundEvent> UMBRA_VALLEY;

    // Entity
    public static Holder<SoundEvent> ENTITY_DRAGONFLY;
    public static Holder<SoundEvent> ENTITY_SHADOW_WALKER;
    public static Holder<SoundEvent> ENTITY_SHADOW_WALKER_DAMAGE;
    public static Holder<SoundEvent> ENTITY_SHADOW_WALKER_DEATH;

    // Records
    public static Holder<SoundEvent> RECORD_STRANGE_AND_ALIEN;
    public static Holder<SoundEvent> RECORD_GRASPING_AT_STARS;
    public static Holder<SoundEvent> RECORD_ENDSEEKER;
    public static Holder<SoundEvent> RECORD_EO_DRACONA;

    private static final ResourceKey<SoundEvent> KEY_MUSIC_FOREST = key("betterend.music.forest");
    private static final ResourceKey<SoundEvent> KEY_MUSIC_WATER = key("betterend.music.water");
    private static final ResourceKey<SoundEvent> KEY_MUSIC_DARK = key("betterend.music.dark");
    private static final ResourceKey<SoundEvent> KEY_MUSIC_OPENSPACE = key("betterend.music.openspace");
    private static final ResourceKey<SoundEvent> KEY_MUSIC_CAVES = key("betterend.music.caves");

    private static final ResourceKey<SoundEvent> KEY_AMBIENT_FOGGY_MUSHROOMLAND = key("betterend.ambient.foggy_mushroomland");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_CHORUS_FOREST = key("betterend.ambient.chorus_forest");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_MEGALAKE = key("betterend.ambient.megalake");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_DUST_WASTELANDS = key("betterend.ambient.dust_wastelands");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_MEGALAKE_GROVE = key("betterend.ambient.megalake_grove");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_BLOSSOMING_SPIRES = key("betterend.ambient.blossoming_spires");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_SULPHUR_SPRINGS = key("betterend.ambient.sulphur_springs");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_UMBRELLA_JUNGLE = key("betterend.ambient.umbrella_jungle");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_GLOWING_GRASSLANDS = key("betterend.ambient.glowing_grasslands");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_CAVES = key("betterend.ambient.caves");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_AMBER_LAND = key("betterend.ambient.amber_land");
    private static final ResourceKey<SoundEvent> KEY_AMBIENT_UMBRA_VALLEY = key("betterend.ambient.umbra_valley");

    private static final ResourceKey<SoundEvent> KEY_ENTITY_DRAGONFLY = key("betterend.entity.dragonfly");
    private static final ResourceKey<SoundEvent> KEY_ENTITY_SHADOW_WALKER = key("betterend.entity.shadow_walker");
    private static final ResourceKey<SoundEvent> KEY_ENTITY_SHADOW_WALKER_DAMAGE = key("betterend.entity.shadow_walker_damage");
    private static final ResourceKey<SoundEvent> KEY_ENTITY_SHADOW_WALKER_DEATH = key("betterend.entity.shadow_walker_death");

    private static final ResourceKey<SoundEvent> KEY_RECORD_STRANGE_AND_ALIEN = key("betterend.record.strange_and_alien");
    private static final ResourceKey<SoundEvent> KEY_RECORD_GRASPING_AT_STARS = key("betterend.record.grasping_at_stars");
    private static final ResourceKey<SoundEvent> KEY_RECORD_ENDSEEKER = key("betterend.record.endseeker");
    private static final ResourceKey<SoundEvent> KEY_RECORD_EO_DRACONA = key("betterend.record.eo_dracona");

    public static final SoundEvent RECORD_STRANGE_AND_ALIEN_EVENT = SoundEvent.createVariableRangeEvent(
            KEY_RECORD_STRANGE_AND_ALIEN.location()
    );
    public static final SoundEvent RECORD_GRASPING_AT_STARS_EVENT = SoundEvent.createVariableRangeEvent(
            KEY_RECORD_GRASPING_AT_STARS.location()
    );
    public static final SoundEvent RECORD_ENDSEEKER_EVENT = SoundEvent.createVariableRangeEvent(
            KEY_RECORD_ENDSEEKER.location()
    );
    public static final SoundEvent RECORD_EO_DRACONA_EVENT = SoundEvent.createVariableRangeEvent(
            KEY_RECORD_EO_DRACONA.location()
    );

    public static void register() {
        // no-op; registration happens via RegisterEvent
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.SOUND_EVENT)) {
            return;
        }
        event.register(Registries.SOUND_EVENT, helper -> {
            register(helper, KEY_MUSIC_FOREST);
            register(helper, KEY_MUSIC_WATER);
            register(helper, KEY_MUSIC_DARK);
            register(helper, KEY_MUSIC_OPENSPACE);
            register(helper, KEY_MUSIC_CAVES);

            register(helper, KEY_AMBIENT_FOGGY_MUSHROOMLAND);
            register(helper, KEY_AMBIENT_CHORUS_FOREST);
            register(helper, KEY_AMBIENT_MEGALAKE);
            register(helper, KEY_AMBIENT_DUST_WASTELANDS);
            register(helper, KEY_AMBIENT_MEGALAKE_GROVE);
            register(helper, KEY_AMBIENT_BLOSSOMING_SPIRES);
            register(helper, KEY_AMBIENT_SULPHUR_SPRINGS);
            register(helper, KEY_AMBIENT_UMBRELLA_JUNGLE);
            register(helper, KEY_AMBIENT_GLOWING_GRASSLANDS);
            register(helper, KEY_AMBIENT_CAVES);
            register(helper, KEY_AMBIENT_AMBER_LAND);
            register(helper, KEY_AMBIENT_UMBRA_VALLEY);

            register(helper, KEY_ENTITY_DRAGONFLY);
            register(helper, KEY_ENTITY_SHADOW_WALKER);
            register(helper, KEY_ENTITY_SHADOW_WALKER_DAMAGE);
            register(helper, KEY_ENTITY_SHADOW_WALKER_DEATH);

            register(helper, KEY_RECORD_STRANGE_AND_ALIEN, RECORD_STRANGE_AND_ALIEN_EVENT);
            register(helper, KEY_RECORD_GRASPING_AT_STARS, RECORD_GRASPING_AT_STARS_EVENT);
            register(helper, KEY_RECORD_ENDSEEKER, RECORD_ENDSEEKER_EVENT);
            register(helper, KEY_RECORD_EO_DRACONA, RECORD_EO_DRACONA_EVENT);
        });
    }

    private static ResourceKey<SoundEvent> key(String id) {
        return ResourceKey.create(Registries.SOUND_EVENT, BetterEnd.makeID(id));
    }

    private static void register(RegisterEvent.RegisterHelper<SoundEvent> helper, ResourceKey<SoundEvent> key) {
        register(helper, key, SoundEvent.createVariableRangeEvent(key.location()));
    }

    private static void register(
            RegisterEvent.RegisterHelper<SoundEvent> helper,
            ResourceKey<SoundEvent> key,
            SoundEvent event
    ) {
        ResourceLocation id = key.location();
        helper.register(id, event);
        assignHolder(key, BuiltInRegistries.SOUND_EVENT.getHolder(key).orElseThrow());
    }

    private static void assignHolder(ResourceKey<SoundEvent> key, Holder<SoundEvent> holder) {
        if (key == KEY_MUSIC_FOREST) {
            MUSIC_FOREST = holder;
        } else if (key == KEY_MUSIC_WATER) {
            MUSIC_WATER = holder;
        } else if (key == KEY_MUSIC_DARK) {
            MUSIC_DARK = holder;
        } else if (key == KEY_MUSIC_OPENSPACE) {
            MUSIC_OPENSPACE = holder;
        } else if (key == KEY_MUSIC_CAVES) {
            MUSIC_CAVES = holder;
        } else if (key == KEY_AMBIENT_FOGGY_MUSHROOMLAND) {
            AMBIENT_FOGGY_MUSHROOMLAND = holder;
        } else if (key == KEY_AMBIENT_CHORUS_FOREST) {
            AMBIENT_CHORUS_FOREST = holder;
        } else if (key == KEY_AMBIENT_MEGALAKE) {
            AMBIENT_MEGALAKE = holder;
        } else if (key == KEY_AMBIENT_DUST_WASTELANDS) {
            AMBIENT_DUST_WASTELANDS = holder;
        } else if (key == KEY_AMBIENT_MEGALAKE_GROVE) {
            AMBIENT_MEGALAKE_GROVE = holder;
        } else if (key == KEY_AMBIENT_BLOSSOMING_SPIRES) {
            AMBIENT_BLOSSOMING_SPIRES = holder;
        } else if (key == KEY_AMBIENT_SULPHUR_SPRINGS) {
            AMBIENT_SULPHUR_SPRINGS = holder;
        } else if (key == KEY_AMBIENT_UMBRELLA_JUNGLE) {
            AMBIENT_UMBRELLA_JUNGLE = holder;
        } else if (key == KEY_AMBIENT_GLOWING_GRASSLANDS) {
            AMBIENT_GLOWING_GRASSLANDS = holder;
        } else if (key == KEY_AMBIENT_CAVES) {
            AMBIENT_CAVES = holder;
        } else if (key == KEY_AMBIENT_AMBER_LAND) {
            AMBIENT_AMBER_LAND = holder;
        } else if (key == KEY_AMBIENT_UMBRA_VALLEY) {
            UMBRA_VALLEY = holder;
        } else if (key == KEY_ENTITY_DRAGONFLY) {
            ENTITY_DRAGONFLY = holder;
        } else if (key == KEY_ENTITY_SHADOW_WALKER) {
            ENTITY_SHADOW_WALKER = holder;
        } else if (key == KEY_ENTITY_SHADOW_WALKER_DAMAGE) {
            ENTITY_SHADOW_WALKER_DAMAGE = holder;
        } else if (key == KEY_ENTITY_SHADOW_WALKER_DEATH) {
            ENTITY_SHADOW_WALKER_DEATH = holder;
        } else if (key == KEY_RECORD_STRANGE_AND_ALIEN) {
            RECORD_STRANGE_AND_ALIEN = holder;
        } else if (key == KEY_RECORD_GRASPING_AT_STARS) {
            RECORD_GRASPING_AT_STARS = holder;
        } else if (key == KEY_RECORD_ENDSEEKER) {
            RECORD_ENDSEEKER = holder;
        } else if (key == KEY_RECORD_EO_DRACONA) {
            RECORD_EO_DRACONA = holder;
        }
    }
}
