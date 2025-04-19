package io.github.scaredsmods.rabbilib.api.blockentity;

import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeHolder;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public abstract class RabbiCraftingBlockEntity extends BlockEntity {
    public RabbiCraftingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    abstract public void craftItem();
    abstract public void resetProgress();
    abstract public boolean hasCraftingFinished();
    abstract public void increaseCraftingProgress();
    abstract protected boolean hasRecipe();
    abstract public <T extends RabbiRecipeInput, R extends RabbiRecipe<T>> Optional<RabbiRecipeHolder<R>> getCurrentRecipe();
    abstract public boolean canInsertItemIntoOutputSlot(ItemStack output);
    abstract public boolean canInsertAmountIntoOutputSlot(int count);

}
