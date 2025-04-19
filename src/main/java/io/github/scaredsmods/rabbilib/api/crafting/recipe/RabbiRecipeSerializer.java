package io.github.scaredsmods.rabbilib.api.crafting.recipe;

import com.mojang.serialization.MapCodec;
import io.github.scaredsmods.rabbilib.recipe.ShapelessRecipe;
import io.github.scaredsmods.rabbilib.registry.RabbiRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;



public interface RabbiRecipeSerializer<T extends RabbiRecipe<?>>  {

    RabbiRecipeSerializer<ShapelessRecipe> SHAPELESS_RECIPE =  register("crafting_shapeless", new ShapelessRecipe.Serializer());

    MapCodec<T> codec();

    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

    static <S extends RabbiRecipeSerializer<T>, T extends RabbiRecipe<?>> S register(String string, S recipeSerializer) {
        return Registry.register(RabbiRegistries.RECIPE_SERIALIZER, string, recipeSerializer);
    }
}
