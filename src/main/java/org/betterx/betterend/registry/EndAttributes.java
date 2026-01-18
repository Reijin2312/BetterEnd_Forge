package org.betterx.betterend.registry;

import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.item.EndAttribute;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class EndAttributes {
    private static final Map<ResourceLocation, Attribute> ATTRIBUTES = new LinkedHashMap<>();

    public final static Attribute BLINDNESS_RESISTANCE = registerAttribute("generic.blindness_resistance", 0.0, true);

    public static Attribute registerAttribute(String name, double value, boolean syncable) {
        Attribute attribute = new EndAttribute("attribute.name." + name, value).setSyncable(syncable);
        ATTRIBUTES.putIfAbsent(BetterEnd.makeID(name), attribute);
        return attribute;
    }

    public static void onRegister(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ATTRIBUTE)) {
            return;
        }
        event.register(Registries.ATTRIBUTE, helper -> {
            ATTRIBUTES.forEach(helper::register);
            ATTRIBUTES.clear();
        });
    }

    public static AttributeSupplier.Builder addLivingEntityAttributes(AttributeSupplier.Builder builder) {
        return builder.add(EndAttributes.BLINDNESS_RESISTANCE);
    }
}


