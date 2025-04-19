package io.github.scaredsmods.rabbilib.api.crafting.recipe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public record RabbiRecipeHolder<T extends RabbiRecipe<?>>(ResourceLocation id, T value) {
    public static final StreamCodec<RegistryFriendlyByteBuf, RabbiRecipeHolder<?>> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, RabbiRecipeHolder::id, RabbiRecipe.STREAM_CODEC, RabbiRecipeHolder::value, RabbiRecipeHolder::new
    );

    public boolean equals(Object object) {
        return this == object ? true : object instanceof RabbiRecipeHolder<?> recipeHolder && this.id.equals(recipeHolder.id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public String toString() {
        return this.id.toString();
    }
}