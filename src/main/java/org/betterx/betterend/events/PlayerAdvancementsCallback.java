package org.betterx.betterend.events;

import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface PlayerAdvancementsCallback {
    Event PLAYER_ADVANCEMENT_COMPLETE = new Event();

    void onAdvancementComplete(ServerPlayer player, Advancement advancement, String criterionName);

    final class Event {
        private final List<PlayerAdvancementsCallback> listeners = new CopyOnWriteArrayList<>();

        public void register(PlayerAdvancementsCallback callback) {
            listeners.add(callback);
        }

        public PlayerAdvancementsCallback invoker() {
            return (player, advancement, criterionName) -> {
                for (PlayerAdvancementsCallback listener : listeners) {
                    listener.onAdvancementComplete(player, advancement, criterionName);
                }
            };
        }
    }
}
