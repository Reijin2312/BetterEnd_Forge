package org.betterx.betterend.advancements;

import org.betterx.betterend.BetterEnd;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class BECriteria {
    public static PlayerTrigger PORTAL_ON;
    public static PlayerTrigger PORTAL_TRAVEL;
    public static PlayerTrigger INFUSION_FINISHED;

    public static PlayerTrigger.TriggerInstance PORTAL_ON_TRIGGER;
    public static PlayerTrigger.TriggerInstance PORTAL_TRAVEL_TRIGGER;
    public static PlayerTrigger.TriggerInstance INFUSION_FINISHED_TRIGGER;


    public static void register() {
        PORTAL_ON = CriteriaTriggers.register(new PlayerTrigger(BetterEnd.makeID("portal_on")));
        PORTAL_TRAVEL = CriteriaTriggers.register(new PlayerTrigger(BetterEnd.makeID("portal_travel")));
        INFUSION_FINISHED = CriteriaTriggers.register(new PlayerTrigger(BetterEnd.makeID("infusion_finished")));

        PORTAL_ON_TRIGGER = new PlayerTrigger.TriggerInstance(
                PORTAL_ON.getId(),
                ContextAwarePredicate.ANY
        );
        PORTAL_TRAVEL_TRIGGER = new PlayerTrigger.TriggerInstance(
                PORTAL_TRAVEL.getId(),
                ContextAwarePredicate.ANY
        );
        INFUSION_FINISHED_TRIGGER = new PlayerTrigger.TriggerInstance(
                INFUSION_FINISHED.getId(),
                ContextAwarePredicate.ANY
        );
    }
}
