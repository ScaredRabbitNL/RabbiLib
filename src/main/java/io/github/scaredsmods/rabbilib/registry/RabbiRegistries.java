package io.github.scaredsmods.rabbilib.registry;

import com.mojang.serialization.Lifecycle;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeSerializer;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeType;
import io.github.scaredsmods.rabbilib.key.RabbiResourceKeys;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.function.Supplier;

public class RabbiRegistries {

    public static final Registry<RabbiRecipeType<?>> RECIPE_TYPE = BuiltInRegistries.registerSimple(RabbiResourceKeys.RECIPE_TYPE, registry -> RabbiRecipeType.CRAFTING);
    public static final Registry<RabbiRecipeSerializer<?>> RECIPE_SERIALIZER = BuiltInRegistries.registerSimple(
            RabbiResourceKeys.RECIPE_SERIALIZER, registry -> RabbiRecipeSerializer.SHAPELESS_RECIPE
    );





}
