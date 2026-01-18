package org.betterx.betterend.integration.trinkets;

import org.betterx.bclib.items.elytra.BCLElytraItem;
import org.betterx.bclib.items.elytra.BCLElytraUtils;

import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;

import java.util.List;
import java.util.Optional;

public class Elytra {
    private static boolean isElytra(ItemStack stack) {
        return stack.getItem() instanceof ElytraItem
                || stack.getItem() instanceof BCLElytraItem;
    }

    public static void register() {
        BCLElytraUtils.slotProvider = (entity, slotGetter) -> {
            ItemStack itemStack = slotGetter.apply(EquipmentSlot.CHEST);
            if (isElytra(itemStack)) return itemStack;

            Optional<TrinketComponent> oTrinketComponent = TrinketsApi.getTrinketComponent(entity);
            if (oTrinketComponent.isPresent()) {
                List<Tuple<SlotReference, ItemStack>> equipped =
                        oTrinketComponent.get().getEquipped(Elytra::isElytra);

                if (!equipped.isEmpty()) return equipped.get(0).getB();
            }
            return null;
        };

        BCLElytraUtils.onBreak = (entity, chestStack) -> {
            Optional<TrinketComponent> oTrinketComponent = TrinketsApi.getTrinketComponent(entity);
            if (oTrinketComponent.isPresent()) {
                List<Tuple<SlotReference, ItemStack>> equipped =
                        oTrinketComponent.get().getEquipped(Elytra::isElytra);

                for (Tuple<SlotReference, ItemStack> slot : equipped) {
                    ItemStack slotStack = slot.getB();
                    if (slotStack == chestStack) {
                        TrinketsApi.onTrinketBroken(slotStack, slot.getA(), entity);
                    }
                }
            }
        };
    }
}


