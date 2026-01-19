package org.betterx.betterend.recipe.builders;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.interfaces.UnknownReceipBookCategory;
import org.betterx.bclib.recipes.AbstractSingleInputRecipeBuilder;
import org.betterx.bclib.recipes.BCLRecipeManager;
import org.betterx.bclib.util.ItemUtil;
import org.betterx.betterend.BetterEnd;
import org.betterx.betterend.rituals.InfusionRitual;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.function.Consumer;

public class InfusionRecipe implements Recipe<InfusionRitual>, UnknownReceipBookCategory {
    public final static String GROUP = "infusion";
    public final static RecipeType<InfusionRecipe> TYPE = BCLRecipeManager.registerType(BetterEnd.MOD_ID, GROUP);
    public final static Serializer SERIALIZER = BCLRecipeManager.registerSerializer(
            BetterEnd.MOD_ID,
            GROUP,
            new Serializer()
    );

    private final ResourceLocation id;
    private final Ingredient[] catalysts;
    private Ingredient input;
    private ItemStack output;
    private int time = 1;
    private String group;

    private InfusionRecipe(ResourceLocation id) {
        this(id, null, null);
    }

    private InfusionRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.catalysts = new Ingredient[]{
                Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY,
                Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY
        };
    }

    public static Builder create(String id, ItemLike output) {
        return create(BetterEnd.makeID(id), output);
    }

    public static Builder create(ResourceLocation id, ItemLike output) {
        return new Builder(id, output);
    }

    public static Builder create(String id, ItemStack output) {
        return create(BetterEnd.makeID(id), output);
    }

    public static Builder create(ResourceLocation id, ItemStack output) {
        return new Builder(id, output);
    }

    public static Builder create(String id, Enchantment enchantment, int level) {
        return create(BetterEnd.makeID(id), enchantment, level);
    }

    public static Builder create(ResourceLocation id, Enchantment enchantment, int level) {
        return new Builder(id, createEnchantedBook(enchantment, level));
    }

    public static ItemStack createEnchantedBook(Enchantment enchantment, int level) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(book, new EnchantmentInstance(enchantment, level));
        return book;
    }

    public int getInfusionTime() {
        return this.time;
    }

    @Override
    public boolean matches(InfusionRitual inv, Level world) {
        boolean valid = this.input.test(inv.getItem(0));
        if (!valid) return false;
        for (int i = 0; i < 8; i++) {
            valid &= this.catalysts[i].test(inv.getItem(i + 1));
        }
        return valid;
    }

    @Override
    public ItemStack assemble(InfusionRitual ritual, RegistryAccess acc) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> defaultedList = NonNullList.create();
        defaultedList.add(input);
        defaultedList.addAll(Arrays.asList(catalysts));
        return defaultedList;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess acc) {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getGroup() {
        return this.group;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public static class Builder extends AbstractSingleInputRecipeBuilder<Builder, InfusionRecipe> {
        private final Ingredient[] catalysts;
        private int time;

        protected Builder(ResourceLocation id, ItemLike output) {
            this(id, new ItemStack(output, 1));
        }

        protected Builder(ResourceLocation id, ItemStack output) {
            super(id, output);
            this.catalysts = new Ingredient[]{
                    Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY,
                    Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY
            };
            this.time = 1;
        }

        @Override
        public Builder setGroup(String group) {
            return super.setGroup(group);
        }

        /**
         * @param input
         * @return
         * @deprecated use {@link #setPrimaryInput(ItemLike...)}
         */
        @Deprecated(forRemoval = true)
        public Builder setInput(ItemLike input) {
            super.setPrimaryInputAndUnlock(input);
            return this;
        }

        public Builder setTime(int time) {
            this.time = time;
            return this;
        }

        public Builder addCatalyst(Catalysts slot, ItemLike... items) {
            this.catalysts[slot.index] = Ingredient.of(items);
            return this;
        }

        public Builder addCatalyst(Catalysts slot, ItemStack stack) {
            this.catalysts[slot.index] = Ingredient.of(stack);
            return this;
        }

        public Builder addCatalyst(Catalysts slot, TagKey<Item> tag) {
            this.catalysts[slot.index] = Ingredient.of(tag);
            return this;
        }

        @Override
        protected boolean checkRecipe() {
            if (time < 0) {
                BCLib.LOGGER.warning(
                        "Time should be positive, recipe {} will be ignored!",
                        id
                );
                return false;
            }
            return super.checkRecipe();
        }

        @Override
        protected void serializeRecipeData(JsonObject root) {
            super.serializeRecipeData(root);

            if (time != 1) {
                root.addProperty("time", time);
            }

            JsonObject catalystObject = new JsonObject();
            for (var cat : Catalysts.values()) {
                if (catalysts[cat.index] != null && !catalysts[cat.index].isEmpty()) {
                    catalystObject.add(cat.key, ItemUtil.toJsonIngredientWithNBT(catalysts[cat.index]));
                }
            }
            root.add("catalysts", catalystObject);
        }

        @Override
        protected void buildRecipe(Consumer<FinishedRecipe> cc) {
            if (getSerializer() == null) {
                BCLib.LOGGER.warning("Skipping recipe {}: serializer is null", id);
                return;
            }
            setupAdvancementForResult();
            ResourceLocation advancementId = createAdvancementId();
            cc.accept(new FinishedRecipe() {
                @Override
                public ResourceLocation getId() {
                    return Builder.this.getId();
                }

                @Override
                public RecipeSerializer<?> getType() {
                    return getSerializer();
                }

                @Override
                public JsonObject serializeAdvancement() {
                    return advancement.serializeToJson();
                }

                @Override
                public ResourceLocation getAdvancementId() {
                    return advancementId;
                }

                @Override
                public void serializeRecipeData(JsonObject root) {
                    Builder.this.serializeRecipeData(root);
                }
            });
        }

        @Override
        protected RecipeSerializer<InfusionRecipe> getSerializer() {
            return SERIALIZER;
        }
    }

    public enum Catalysts {
        NORTH(0, "north"),
        NORTH_EAST(1, "north_east"),
        EAST(2, "east"),
        SOUTH_EAST(3, "south_east"),
        SOUTH(4, "south"),
        SOUTH_WEST(5, "south_west"),
        WEST(6, "west"),
        NORTH_WEST(7, "north_west");

        public final int index;
        public final String key;

        Catalysts(int index, String key) {
            this.index = index;
            this.key = key;
        }
    }

    public static class Serializer implements RecipeSerializer<InfusionRecipe> {
        private Ingredient readIngredient(JsonObject obj, String key) {
            if (obj.has(key)) {
                JsonElement el = obj.get(key);
                if (el.isJsonObject()) {
                    JsonObject o = el.getAsJsonObject();
                    //directly read as ingredient
                    if (o.has("tag")) {
                        final Ingredient res = ItemUtil.fromJsonIngredientWithNBT(o);
                        if (res == null) return Ingredient.EMPTY;
                        return res;
                    } else {
                        final Ingredient res = Ingredient.of(ItemUtil.fromJsonRecipeWithNBT(o));
                        if (res == null) return Ingredient.EMPTY;
                        return res;
                    }
                } else if (el.isJsonArray()) {
                    //this is an Ingredient-Array, so read as such
                    final Ingredient res = Ingredient.fromJson(el);
                    if (res == null) return Ingredient.EMPTY;
                    return res;
                } else if (obj.isJsonPrimitive()) {
                    String s = GsonHelper.getAsString(obj, key, "");
                    ItemStack catalyst = ItemUtil.fromStackString(s);
                    return (catalyst != null && !catalyst.isEmpty())
                            ? Ingredient.of(catalyst.getItem())
                            : Ingredient.EMPTY;
                } else {
                    throw new IllegalStateException("Invalid catalyst ingredient for " + key + ": " + el.toString());
                }
            }
            return Ingredient.EMPTY;
        }

        @Override
        public InfusionRecipe fromJson(ResourceLocation id, JsonObject json) {
            InfusionRecipe recipe = new InfusionRecipe(id);
            JsonObject inputObject = GsonHelper.getAsJsonObject(json, "input");
            recipe.input = ItemUtil.fromJsonIngredientWithNBT(inputObject);

            JsonObject result = GsonHelper.getAsJsonObject(json, "result");
            recipe.output = ItemUtil.fromJsonRecipeWithNBT(result);
            if (recipe.output == null) {
                throw new IllegalStateException("Output item does not exists!");
            }
            recipe.group = GsonHelper.getAsString(json, "group", GROUP);
            recipe.time = GsonHelper.getAsInt(json, "time", 1);

            JsonObject catalysts = GsonHelper.getAsJsonObject(json, "catalysts");
            for (var cat : Catalysts.values()) {
                recipe.catalysts[cat.index] = readIngredient(catalysts, cat.key);
            }

            return recipe;
        }

        @Override
        public InfusionRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            InfusionRecipe recipe = new InfusionRecipe(id);
            recipe.input = Ingredient.fromNetwork(buffer);
            recipe.output = buffer.readItem();
            recipe.group = buffer.readUtf();
            recipe.time = buffer.readVarInt();
            for (int i = 0; i < 8; i++) {
                recipe.catalysts[i] = Ingredient.fromNetwork(buffer);
            }
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, InfusionRecipe recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.time);
            for (int i = 0; i < 8; i++) {
                recipe.catalysts[i].toNetwork(buffer);
            }
        }
    }

    public static void register() {
        //we call this to make sure that TYPE is initialized
    }
}

