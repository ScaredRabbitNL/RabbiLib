package io.github.scaredsmods.rabbilib.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeSerializer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class ShapelessRecipe implements CraftingRecipe {
    final String group;
    final CraftingBookCategory category;
    final NonNullList<ItemStack> result;
    final NonNullList<Ingredient> ingredients;

    public ShapelessRecipe(String string, CraftingBookCategory craftingBookCategory, NonNullList<ItemStack> itemStack, NonNullList<Ingredient> nonNullList) {
        this.group = string;
        this.category = craftingBookCategory;
        this.result = itemStack;
        this.ingredients = nonNullList;
    }

    @Override
    public RabbiRecipeSerializer<?> getSerializer() {
        return RabbiRecipeSerializer.SHAPELESS_RECIPE;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public NonNullList<ItemStack> getResults(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public boolean matches(CraftingInput craftingInput, Level level) {
        if (craftingInput.ingredientCount() != this.ingredients.size()) {
            return false;
        } else {
            return craftingInput.size() == 1 && this.ingredients.size() == 1
                    ? ((Ingredient)this.ingredients.getFirst()).test(craftingInput.getItem(0))
                    : craftingInput.stackedContents().canCraft(this, null);
        }
    }

    public NonNullList<ItemStack> assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        return result;
    }



    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= this.ingredients.size();
    }


    public static class Serializer implements RabbiRecipeSerializer<ShapelessRecipe> {

        public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<ShapelessRecipe> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ShapelessRecipe fromNetwork(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
            String string = registryFriendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = registryFriendlyByteBuf.readEnum(CraftingBookCategory.class);
            int i = registryFriendlyByteBuf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
            ingredients.replaceAll(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.decode(registryFriendlyByteBuf));

            NonNullList<ItemStack> list = NonNullList.create();
            list.forEach(itemStack -> ItemStack.STREAM_CODEC.decode(registryFriendlyByteBuf));

            return new ShapelessRecipe(string, craftingBookCategory, list, ingredients);
        }

        private static void toNetwork(RegistryFriendlyByteBuf registryFriendlyByteBuf, ShapelessRecipe shapelessRecipe) {
            registryFriendlyByteBuf.writeUtf(shapelessRecipe.group);
            registryFriendlyByteBuf.writeEnum(shapelessRecipe.category);
            registryFriendlyByteBuf.writeVarInt(shapelessRecipe.ingredients.size());

            for (Ingredient ingredient : shapelessRecipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(registryFriendlyByteBuf, ingredient);
            }
            shapelessRecipe.result.forEach(itemStack -> ItemStack.STREAM_CODEC.encode(registryFriendlyByteBuf, itemStack));


        }
    }
}
