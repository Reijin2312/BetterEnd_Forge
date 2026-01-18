package org.betterx.betterend.item.material;

import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum EndToolMaterial implements Tier {
    THALLASIUM(Tiers.IRON.getLevel(), 320, 7.0F, 1.5F, 12, () -> {
        return Ingredient.of(EndBlocks.THALLASIUM.ingot);
    }), TERMINITE(Tiers.DIAMOND.getLevel(), 1230, 8.5F, 3.0F, 14, () -> {
        return Ingredient.of(EndBlocks.TERMINITE.ingot);
    }), AETERNIUM(5, 2196, 10.0F, 4.5F, 18, () -> {
        return Ingredient.of(EndItems.AETERNIUM_INGOT);
    });

    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int miningLevel;
    private final int enchantability;
    @SuppressWarnings("deprecation")
    private final LazyLoadedValue<Ingredient> repairIngredient;

    @SuppressWarnings("deprecation")
    EndToolMaterial(
            int miningLevel,
            int durability,
            float miningSpeed,
            float attackDamage,
            int enchantability,
            Supplier<Ingredient> repairIngredient
    ) {
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    @Override
    public int getUses() {
        return this.durability;
    }

    @Override
    public float getSpeed() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

}
