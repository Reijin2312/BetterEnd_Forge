package org.betterx.betterend.item;

import org.betterx.betterend.interfaces.BetterEndElytra;
import org.betterx.betterend.interfaces.MultiModelItem;
import org.betterx.betterend.item.material.EndArmorMaterial;
import org.betterx.betterend.registry.EndItems;

public class CrystaliteElytra extends ArmoredElytra implements MultiModelItem, BetterEndElytra {
    public CrystaliteElytra(int durability, double movementFactor) {
        super(
                "elytra_crystalite",
                EndArmorMaterial.CRYSTALITE,
                EndItems.ENCHANTED_MEMBRANE,
                durability,
                movementFactor,
                1.25D,
                1.25F,
                false
        );
    }
}
