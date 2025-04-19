package io.github.scaredsmods.rabbilib.recipe;

import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeType;
import net.minecraft.world.item.crafting.CraftingBookCategory;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface CraftingRecipe extends RabbiRecipe<CraftingInput> {
    default RabbiRecipeType<?> getType() {
        return RabbiRecipeType.CRAFTING;
    }

    CraftingBookCategory category();
}
