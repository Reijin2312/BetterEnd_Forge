package org.betterx.betterend.integration;

import org.betterx.bclib.api.v2.ModIntegrationAPI;
import org.betterx.bclib.integration.ModIntegration;
import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.events.PlayerAdvancementsCallback;
import org.betterx.betterend.integration.byg.BYGIntegration;
import org.betterx.betterend.item.GuideBookItem;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.fml.ModList;

public class Integrations {
    public static final ModIntegration BYG = ModIntegrationAPI.register(new BYGIntegration());
    public static final ModIntegration NOURISH = ModIntegrationAPI.register(new NourishIntegration());
    public static final ModIntegration FLAMBOYANT_REFABRICATED = ModIntegrationAPI.register(new FlamboyantRefabricatedIntegration());

    private static boolean hasHydrogen;

    public static void init() {
        if (hasGuideBook()) {
            GuideBookItem.register();

            PlayerAdvancementsCallback.PLAYER_ADVANCEMENT_COMPLETE.register((player, advancement, criterionName) -> {
                ResourceLocation advId = new ResourceLocation("minecraft:end/enter_end_gateway");
                if (advId.equals(advancement.getId())) {
                    player.addItem(new ItemStack(GuideBookItem.GUIDE_BOOK));
                }
            });

            BCLRecipeBuilder.crafting(BetterEnd.makeID("guide_book"), GuideBookItem.GUIDE_BOOK)
                            .setShape("D", "B", "C")
                            .addMaterial('D', EndItems.ENDER_DUST)
                            .addMaterial('B', Items.BOOK)
                            .addMaterial('C', EndItems.CRYSTAL_SHARDS)
                            .build();
        }
        hasHydrogen = ModList.get().isLoaded("hydrogen");
    }

    public static boolean hasGuideBook() {
        return ModList.get().isLoaded("patchouli");
    }

    public static boolean hasHydrogen() {
        return hasHydrogen;
    }
}
