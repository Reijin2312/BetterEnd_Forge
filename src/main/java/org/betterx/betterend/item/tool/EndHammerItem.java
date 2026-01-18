package org.betterx.betterend.item.tool;

import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.interfaces.ItemModelProvider;
import org.betterx.bclib.interfaces.TagProvider;
import org.betterx.bclib.util.MHelper;
import org.betterx.worlds.together.tag.v3.CommonBlockTags;
import org.betterx.worlds.together.tag.v3.CommonItemTags;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.UUID;

public class EndHammerItem extends DiggerItem implements ItemModelProvider, TagProvider {
    public final static UUID ATTACK_KNOCKBACK_MODIFIER_ID = Mth.createInsecureUUID(MHelper.RANDOM_SOURCE);

    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public EndHammerItem(Tier material, float attackDamage, float attackSpeed, double knockback, Properties settings) {
        super(attackDamage, attackSpeed, material, CommonBlockTags.MINABLE_WITH_HAMMER, settings);

        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        BASE_ATTACK_DAMAGE_UUID,
                        "Weapon modifier",
                        attackDamage + material.getAttackDamageBonus(),
                        AttributeModifier.Operation.ADDITION
                )
        );
        builder.put(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        BASE_ATTACK_SPEED_UUID,
                        "Weapon modifier",
                        attackSpeed,
                        AttributeModifier.Operation.ADDITION
                )
        );
        builder.put(
                Attributes.ATTACK_KNOCKBACK,
                new AttributeModifier(
                        ATTACK_KNOCKBACK_MODIFIER_ID,
                        "Weapon modifier",
                        knockback,
                        AttributeModifier.Operation.ADDITION
                )
        );
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player miner) {
        return state.is(CommonBlockTags.MINABLE_WITH_HAMMER)
                || state.is(Blocks.DIAMOND_BLOCK)
                || state.is(Blocks.EMERALD_BLOCK)
                || state.is(Blocks.LAPIS_BLOCK)
                || state.is(Blocks.REDSTONE_BLOCK);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, ((entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND)));

        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getDestroySpeed(world, pos) != 0.0F) {
            stack.hurtAndBreak(1, miner, ((entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
        }

        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        //usually Glass Blocks
        if (state.instrument() == NoteBlockInstrument.HAT) {
            return this.getTier().getSpeed() * 2.0F;
        }
        if (isCorrectToolForDrops(state)) {
            float mult;
            if (state.is(Blocks.DIAMOND_BLOCK)
                    || state.is(Blocks.EMERALD_BLOCK)
                    || state.is(Blocks.LAPIS_BLOCK)
                    || state.is(Blocks.REDSTONE_BLOCK)) {
                mult = this.getTier().getSpeed();
            } else {
                mult = this.getTier().getSpeed() / 2.0F;
            }
            return Math.max(mult, 1.0F);
        }
        return 1.0F;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockModel getItemModel(ResourceLocation resourceLocation) {
        return ModelsHelper.createHandheldItem(resourceLocation);
    }

    @Override
    public void addTags(List<TagKey<Block>> blockTags, List<TagKey<Item>> itemTags) {
        itemTags.add(CommonItemTags.HAMMERS);
    }
}

