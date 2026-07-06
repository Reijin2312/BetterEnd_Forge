package org.betterx.betterend.mixin.client;

import org.betterx.betterend.client.ClientOptions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(MusicManager.class)
public class MusicTrackerMixin {
    @Unique private static final float FADE_SPEED = 0.2f; // Units per second (0.2f -> Fade across 5 seconds)
    @Unique private static final float TICK_DELTA = 0.05f;
    @Unique private static final RandomSource BE_FALLBACK_RANDOM = RandomSource.create();
    // Note: Assume game is at a constant 20 tps since MC doesn't have getTPS()
    // The use of currentTimeMillis() is ditched since it is overly complex for this system
    // The difference from this constant will only be noticeable if the game's TPS is extremely low
    // If the game is lagging to that extent, smooth music blending is the least of the player's worries

    @Unique private final MusicManager be_thisObj = (MusicManager)(Object)this;
    @Unique private boolean be_waitChange = false;
    @Unique private float be_volume = 1.0f;

    @Unique private static Field be_minecraftField;
    @Unique private static Field be_randomField;
    @Unique private static Field be_currentMusicField;
    @Unique private static Field be_nextSongDelayField;

    @Unique
    private boolean be_isCorrectDimension(Minecraft minecraft) {
        return minecraft.player != null && minecraft.level != null
                && minecraft.level.dimension() == Level.END;
    }

    @Unique
    private boolean be_shouldChangeMusic(Music toMusic) {
        ResourceLocation currentMusicLocation = be_getCurrentMusicLocation();
        return currentMusicLocation == null || !toMusic.getEvent().value().getLocation().equals(currentMusicLocation);
    }

    @Unique
    private ResourceLocation be_getCurrentMusicLocation() {
        SoundInstance currentMusic = be_getCurrentMusic();
        if (currentMusic instanceof AbstractSoundInstanceAccessor accessor) {
            return accessor.getLocation();
        }
        return null;
    }

    @Unique
    private Minecraft be_getMinecraft() {
        Minecraft minecraft = be_getField(be_getMinecraftField(), Minecraft.class);
        return minecraft == null ? Minecraft.getInstance() : minecraft;
    }

    @Unique
    private RandomSource be_getRandom() {
        RandomSource random = be_getField(be_getRandomField(), RandomSource.class);
        return random == null ? BE_FALLBACK_RANDOM : random;
    }

    @Unique
    private SoundInstance be_getCurrentMusic() {
        return be_getField(be_getCurrentMusicField(), SoundInstance.class);
    }

    @Unique
    private void be_setCurrentMusic(SoundInstance currentMusic) {
        be_setField(be_getCurrentMusicField(), currentMusic);
    }

    @Unique
    private int be_getNextSongDelay() {
        Integer nextSongDelay = be_getField(be_getNextSongDelayField(), Integer.class);
        return nextSongDelay == null ? 0 : nextSongDelay;
    }

    @Unique
    private void be_setNextSongDelay(int nextSongDelay) {
        be_setField(be_getNextSongDelayField(), nextSongDelay);
    }

    @Unique
    private static Field be_getMinecraftField() {
        if (be_minecraftField == null) {
            be_minecraftField = be_findField(Minecraft.class);
        }
        return be_minecraftField;
    }

    @Unique
    private static Field be_getRandomField() {
        if (be_randomField == null) {
            be_randomField = be_findField(RandomSource.class);
        }
        return be_randomField;
    }

    @Unique
    private static Field be_getCurrentMusicField() {
        if (be_currentMusicField == null) {
            be_currentMusicField = be_findField(SoundInstance.class);
        }
        return be_currentMusicField;
    }

    @Unique
    private static Field be_getNextSongDelayField() {
        if (be_nextSongDelayField == null) {
            be_nextSongDelayField = be_findField(int.class);
        }
        return be_nextSongDelayField;
    }

    @Unique
    private static Field be_findField(Class<?> type) {
        for (Field field : MusicManager.class.getDeclaredFields()) {
            if (type.isAssignableFrom(field.getType()) || type == field.getType()) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    @Unique
    private <T> T be_getField(Field field, Class<T> type) {
        if (field == null) {
            return null;
        }
        try {
            Object value = field.get(be_thisObj);
            return type.isInstance(value) ? type.cast(value) : null;
        } catch (IllegalAccessException ignored) {
            return null;
        }
    }

    @Unique
    private void be_setField(Field field, Object value) {
        if (field == null) {
            return;
        }
        try {
            field.set(be_thisObj, value);
        } catch (IllegalAccessException ignored) {
        }
    }

    @Inject(method = "startPlaying", at = @At("TAIL"))
    public void be_startPlaying(Music music, CallbackInfo ci) {
        be_volume = 0.0f; // Mostly to fix issues when the blending system becomes desynced due to other dims
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void be_onTick(CallbackInfo ci) {
        Minecraft minecraft = be_getMinecraft();
        if (!ClientOptions.blendBiomeMusic() || minecraft == null || !be_isCorrectDimension(minecraft)) {
            be_waitChange = false;
            be_volume = 1.0f;
            return;
        }

        Music targetMusic = minecraft.getSituationalMusic();
        if (targetMusic == null || !targetMusic.replaceCurrentMusic()) {
            be_waitChange = false;
            be_volume = 1.0f;
            return; // If the target music cannot replace the current, let vanilla handle it
        }

        RandomSource random = be_getRandom();
        SoundInstance currentMusic = be_getCurrentMusic();
        int nextSongDelay = be_getNextSongDelay();
        if (currentMusic != null && !minecraft.getSoundManager().isActive(currentMusic)) {
            currentMusic = null;
            be_setCurrentMusic(null);
            nextSongDelay = Math.min(
                    nextSongDelay,
                    Mth.nextInt(random, targetMusic.getMinDelay(), targetMusic.getMaxDelay())
            );
        }
        nextSongDelay = Math.min(nextSongDelay, targetMusic.getMaxDelay());
        be_setNextSongDelay(nextSongDelay);

        if (currentMusic == null) {
            be_setNextSongDelay(nextSongDelay - 1);
            if (nextSongDelay <= 0) {
                be_waitChange = false;
                be_thisObj.startPlaying(targetMusic);
                currentMusic = be_getCurrentMusic();
                if (currentMusic instanceof AbstractSoundInstanceAccessor accessor) {
                    accessor.setVolume(0.0f);
                    minecraft.getSoundManager().updateSourceVolume(
                            currentMusic.getSource(),
                            0.0f
                    );
                }
            }
            ci.cancel();
            return;
        }

        boolean volumeChanged = false;
        if (be_waitChange || be_shouldChangeMusic(targetMusic)) {
            if (!be_waitChange) {
                nextSongDelay = random.nextInt(0, Math.max(targetMusic.getMinDelay() / 2, 1));
                be_setNextSongDelay(nextSongDelay);
                be_waitChange = true;
            }
            if (be_volume > 0.0f) {
                // Fade out current music
                volumeChanged = true;
                be_volume -= FADE_SPEED * TICK_DELTA;
                if (be_volume <= 0.0f) {
                    be_volume = 0.0f;
                    minecraft.getSoundManager().stop(currentMusic);
                    currentMusic = null;
                    be_setCurrentMusic(null);
                }
            } else if (nextSongDelay > 0) {
                // In-between music delay
                nextSongDelay -= 1;
                be_setNextSongDelay(nextSongDelay);
            } else {
                // Start new music
                be_waitChange = false;
                be_thisObj.startPlaying(targetMusic);
                currentMusic = be_getCurrentMusic();
                if (currentMusic instanceof AbstractSoundInstanceAccessor accessor) {
                    accessor.setVolume(0.0f);
                    minecraft.getSoundManager().updateSourceVolume(
                            currentMusic.getSource(),
                            0.0f
                    );
                }
            }
        } else if (be_volume < 1.0f) {
            // Fade in new music
            volumeChanged = true;
            be_volume += FADE_SPEED * TICK_DELTA;
        }

        if (volumeChanged) {
            be_volume = Mth.clamp(be_volume, 0.0f, 1.0f);
            if (currentMusic instanceof AbstractSoundInstanceAccessor accessor) {
                accessor.setVolume(be_volume);
                minecraft.getSoundManager().updateSourceVolume(currentMusic.getSource(), be_volume);
            }
        }

        ci.cancel();
    }
}
