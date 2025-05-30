package io.github.scaredsmods.rabbilib.api;


import com.google.common.collect.Lists;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipe;
import io.github.scaredsmods.rabbilib.api.crafting.recipe.RabbiRecipeHolder;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;
import java.util.List;

public class StackedContents {
    private static final int EMPTY = 0;
    public final Int2IntMap contents = new Int2IntOpenHashMap();

    public StackedContents() {
    }

    public void accountSimpleStack(ItemStack itemStack) {
        if (!itemStack.isDamaged() && !itemStack.isEnchanted() && !itemStack.has(DataComponents.CUSTOM_NAME)) {
            this.accountStack(itemStack);
        }

    }

    public void accountStack(ItemStack itemStack) {
        this.accountStack(itemStack, itemStack.getMaxStackSize());
    }

    public void accountStack(ItemStack itemStack, int i) {
        if (!itemStack.isEmpty()) {
            int j = getStackingIndex(itemStack);
            int k = Math.min(i, itemStack.getCount());
            this.put(j, k);
        }

    }

    public static int getStackingIndex(ItemStack itemStack) {
        return BuiltInRegistries.ITEM.getId(itemStack.getItem());
    }

    boolean has(int i) {
        return this.contents.get(i) > 0;
    }

    int take(int i, int j) {
        int k = this.contents.get(i);
        if (k >= j) {
            this.contents.put(i, k - j);
            return i;
        } else {
            return 0;
        }
    }

    void put(int i, int j) {
        this.contents.put(i, this.contents.get(i) + j);
    }

    public boolean canCraft(RabbiRecipe<?> recipe, @Nullable IntList intList) {
        return this.canCraft(recipe, intList, 1);
    }

    public boolean canCraft(RabbiRecipe<?> recipe, @Nullable IntList intList, int i) {
        return (new StackedContents.RecipePicker(recipe)).tryPick(i, intList);
    }

    public int getBiggestCraftableStack(RabbiRecipeHolder<?> recipeHolder, @Nullable IntList intList) {
        return this.getBiggestCraftableStack(recipeHolder, Integer.MAX_VALUE, intList);
    }

    public int getBiggestCraftableStack(RabbiRecipeHolder<?> recipeHolder, int i, @Nullable IntList intList) {
        return (new RecipePicker(recipeHolder.value())).tryPickAll(i, intList);
    }

    public static ItemStack fromStackingIndex(int i) {
        return i == 0 ? ItemStack.EMPTY : new ItemStack(Item.byId(i));
    }

    public void clear() {
        this.contents.clear();
    }

    public class RecipePicker {
        private final RabbiRecipe<?> recipe;
        private final List<Ingredient> ingredients = Lists.newArrayList();
        private final int ingredientCount;
        private final int[] items;
        private final int itemCount;
        private final BitSet data;
        private final IntList path = new IntArrayList();

        public RecipePicker(final RabbiRecipe<?> recipe) {
            this.recipe = recipe;
            this.ingredients.addAll(recipe.getIngredients());
            this.ingredients.removeIf(Ingredient::isEmpty);
            this.ingredientCount = this.ingredients.size();
            this.items = this.getUniqueAvailableIngredientItems();
            this.itemCount = this.items.length;
            this.data = new BitSet(this.ingredientCount + this.itemCount + this.ingredientCount + this.ingredientCount * this.itemCount);

            for(int i = 0; i < this.ingredients.size(); ++i) {
                IntList intList = ((Ingredient)this.ingredients.get(i)).getStackingIds();

                for(int j = 0; j < this.itemCount; ++j) {
                    if (intList.contains(this.items[j])) {
                        this.data.set(this.getIndex(true, j, i));
                    }
                }
            }

        }

        public boolean tryPick(int i, @Nullable IntList intList) {
            if (i <= 0) {
                return true;
            } else {
                int j;
                for(j = 0; this.dfs(i); ++j) {
                    StackedContents.this.take(this.items[this.path.getInt(0)], i);
                    int k = this.path.size() - 1;
                    this.setSatisfied(this.path.getInt(k));

                    for(int l = 0; l < k; ++l) {
                        this.toggleResidual((l & 1) == 0, this.path.get(l), this.path.get(l + 1));
                    }

                    this.path.clear();
                    this.data.clear(0, this.ingredientCount + this.itemCount);
                }

                boolean bl = j == this.ingredientCount;
                boolean bl2 = bl && intList != null;
                if (bl2) {
                    intList.clear();
                }

                this.data.clear(0, this.ingredientCount + this.itemCount + this.ingredientCount);
                int m = 0;

                for(Ingredient ingredient : this.recipe.getIngredients()) {
                    if (bl2 && ingredient.isEmpty()) {
                        intList.add(0);
                    } else {
                        for(int n = 0; n < this.itemCount; ++n) {
                            if (this.hasResidual(false, m, n)) {
                                this.toggleResidual(true, n, m);
                                StackedContents.this.put(this.items[n], i);
                                if (bl2) {
                                    intList.add(this.items[n]);
                                }
                            }
                        }

                        ++m;
                    }
                }

                return bl;
            }
        }

        private int[] getUniqueAvailableIngredientItems() {
            IntCollection intCollection = new IntAVLTreeSet();

            for(Ingredient ingredient : this.ingredients) {
                intCollection.addAll(ingredient.getStackingIds());
            }

            IntIterator intIterator = intCollection.iterator();

            while(intIterator.hasNext()) {
                if (!StackedContents.this.has(intIterator.nextInt())) {
                    intIterator.remove();
                }
            }

            return intCollection.toIntArray();
        }

        private boolean dfs(int i) {
            int j = this.itemCount;

            for(int k = 0; k < j; ++k) {
                if (StackedContents.this.contents.get(this.items[k]) >= i) {
                    this.visit(false, k);

                    while(!this.path.isEmpty()) {
                        int l = this.path.size();
                        boolean bl = (l & 1) == 1;
                        int m = this.path.getInt(l - 1);
                        if (!bl && !this.isSatisfied(m)) {
                            break;
                        }

                        int n = bl ? this.ingredientCount : j;

                        for(int o = 0; o < n; ++o) {
                            if (!this.hasVisited(bl, o) && this.hasConnection(bl, m, o) && this.hasResidual(bl, m, o)) {
                                this.visit(bl, o);
                                break;
                            }
                        }

                        int o = this.path.size();
                        if (o == l) {
                            this.path.removeInt(o - 1);
                        }
                    }

                    if (!this.path.isEmpty()) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean isSatisfied(int i) {
            return this.data.get(this.getSatisfiedIndex(i));
        }

        private void setSatisfied(int i) {
            this.data.set(this.getSatisfiedIndex(i));
        }

        private int getSatisfiedIndex(int i) {
            return this.ingredientCount + this.itemCount + i;
        }

        private boolean hasConnection(boolean bl, int i, int j) {
            return this.data.get(this.getIndex(bl, i, j));
        }

        private boolean hasResidual(boolean bl, int i, int j) {
            return bl != this.data.get(1 + this.getIndex(bl, i, j));
        }

        private void toggleResidual(boolean bl, int i, int j) {
            this.data.flip(1 + this.getIndex(bl, i, j));
        }

        private int getIndex(boolean bl, int i, int j) {
            int k = bl ? i * this.ingredientCount + j : j * this.ingredientCount + i;
            return this.ingredientCount + this.itemCount + this.ingredientCount + 2 * k;
        }

        private void visit(boolean bl, int i) {
            this.data.set(this.getVisitedIndex(bl, i));
            this.path.add(i);
        }

        private boolean hasVisited(boolean bl, int i) {
            return this.data.get(this.getVisitedIndex(bl, i));
        }

        private int getVisitedIndex(boolean bl, int i) {
            return (bl ? 0 : this.ingredientCount) + i;
        }

        public int tryPickAll(int i, @Nullable IntList intList) {
            int j = 0;
            int k = Math.min(i, this.getMinIngredientCount()) + 1;

            while(true) {
                int l = (j + k) / 2;
                if (this.tryPick(l, (IntList)null)) {
                    if (k - j <= 1) {
                        if (l > 0) {
                            this.tryPick(l, intList);
                        }

                        return l;
                    }

                    j = l;
                } else {
                    k = l;
                }
            }
        }

        private int getMinIngredientCount() {
            int i = Integer.MAX_VALUE;

            for(Ingredient ingredient : this.ingredients) {
                int j = 0;

                int k;
                for(IntListIterator var5 = ingredient.getStackingIds().iterator(); var5.hasNext(); j = Math.max(j, StackedContents.this.contents.get(k))) {
                    k = (Integer)var5.next();
                }

                if (i > 0) {
                    i = Math.min(i, j);
                }
            }

            return i;
        }
    }
}
