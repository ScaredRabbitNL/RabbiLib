package io.github.scaredsmods.rabbilib.api.crafting.recipe;

import io.github.scaredsmods.rabbilib.RabbiLib;
import io.github.scaredsmods.rabbilib.recipe.CraftingRecipe;
import io.github.scaredsmods.rabbilib.registry.RabbiRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;


public interface RabbiRecipeType <T extends RabbiRecipe<?>> {

    RabbiRecipeType<CraftingRecipe> CRAFTING = register("machine_crafting");

    static <T extends RabbiRecipe<?>> RabbiRecipeType<T> register(String string) {
        return Registry.register(RabbiRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(RabbiLib.MOD_ID, string), new RabbiRecipeType<T>() {
            public String toString() {
                return string;
            }
        });
    }
}