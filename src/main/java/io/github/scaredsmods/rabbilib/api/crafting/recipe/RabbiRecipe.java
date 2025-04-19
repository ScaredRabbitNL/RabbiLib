package io.github.scaredsmods.rabbilib.api.crafting.recipe;

import com.mojang.serialization.Codec;
import io.github.scaredsmods.rabbilib.key.RabbiResourceKeys;
import io.github.scaredsmods.rabbilib.registry.RabbiRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public interface RabbiRecipe<T extends RabbiRecipeInput> {

    Codec<RabbiRecipe<?>> CODEC = RabbiRegistries.RECIPE_SERIALIZER.byNameCodec().dispatch(RabbiRecipe::getSerializer, RabbiRecipeSerializer::codec);
    StreamCodec<RegistryFriendlyByteBuf, RabbiRecipe<?>> STREAM_CODEC = ByteBufCodecs.registry(RabbiResourceKeys.RECIPE_SERIALIZER).dispatch(RabbiRecipe::getSerializer, RabbiRecipeSerializer::streamCodec);

    boolean matches(T recipeInput, Level level);

    NonNullList<ItemStack> assemble(T recipeInput, HolderLookup.Provider provider);

    boolean canCraftInDimensions(int i, int j);

    NonNullList<ItemStack> getResults(HolderLookup.Provider provider);

    default NonNullList<ItemStack> getRemainingItems(T recipeInput) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(recipeInput.size(), ItemStack.EMPTY);

        for(int i = 0; i < nonNullList.size(); ++i) {
            Item item = recipeInput.getItem(i).getItem();
            if (item.hasCraftingRemainingItem()) {
                nonNullList.set(i, new ItemStack(item.getCraftingRemainingItem()));
            }
        }

        return nonNullList;
    }

    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }

    default boolean isSpecial() {
        return false;
    }

    default boolean showNotification() {
        return true;
    }

    default String getGroup() {
        return "";
    }

    default ItemStack getToastSymbol() {
        return new ItemStack(Blocks.CRAFTING_TABLE);
    }

    RabbiRecipeSerializer<?> getSerializer();

    RabbiRecipeType<?> getType();

    default boolean isIncomplete() {
        NonNullList<Ingredient> nonNullList = this.getIngredients();
        return nonNullList.isEmpty() || nonNullList.stream().anyMatch((ingredient) -> ingredient.getItems().length == 0);
    }
}
