package org.betterx.betterend.item;

import org.betterx.betterend.registry.EndAttributes;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Rarity;

import java.util.UUID;

public class CrystaliteHelmet extends CrystaliteArmor {

    public CrystaliteHelmet() {
        super(Type.HELMET, EndItems.makeEndItemSettings().rarity(Rarity.RARE));
        UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[EquipmentSlot.HEAD.getIndex()];
        addAttributeModifier(
                EndAttributes.BLINDNESS_RESISTANCE,
                new AttributeModifier(uuid, "Helmet blindness resistance", 1.0, AttributeModifier.Operation.ADDITION)
        );
    }
}
