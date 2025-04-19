package io.github.scaredsmods.rabbilib.key;

import io.github.scaredsmods.rabbilib.RabbiLib;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeSerializer;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;



public class RabbiResourceKeys {

    public static final ResourceKey<Registry<RabbiRecipeSerializer<?>>> RECIPE_SERIALIZER = createRegistryKey("recipe_serializer");
    public static final ResourceKey<Registry<RabbiRecipeType<?>>> RECIPE_TYPE = createRegistryKey("rabbi_recipe_type");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String string) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(RabbiLib.MOD_ID, string));
    }
}
