package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.item.EndAttribute;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class EndAttributes {
    public final static Attribute BLINDNESS_RESISTANCE = registerAttribute("generic.blindness_resistance", 0.0, true);

    public static Attribute registerAttribute(String name, double value, boolean syncable) {
        return Registry.register(
                BuiltInRegistries.ATTRIBUTE,
                BetterEnd.makeID(name),
                new EndAttribute("attribute.name." + name, value).setSyncable(syncable)
        );
    }

    public static AttributeSupplier.Builder addLivingEntityAttributes(AttributeSupplier.Builder builder) {
        return builder.add(EndAttributes.BLINDNESS_RESISTANCE);
    }
}


