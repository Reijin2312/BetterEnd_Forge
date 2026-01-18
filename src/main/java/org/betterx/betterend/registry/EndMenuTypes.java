package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.blocks.EndStoneSmelter;
import org.betterx.betterend.client.gui.EndStoneSmelterMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.registries.RegisterEvent;

public class EndMenuTypes {
    public static MenuType<EndStoneSmelterMenu> END_STONE_SMELTER;

    public static void ensureStaticallyLoaded() {
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.MENU)) {
            return;
        }
        event.register(Registries.MENU, helper -> {
            END_STONE_SMELTER = new MenuType<>(EndStoneSmelterMenu::new, FeatureFlags.DEFAULT_FLAGS);
            helper.register(BetterEnd.makeID(EndStoneSmelter.ID), END_STONE_SMELTER);
        });
    }
}
