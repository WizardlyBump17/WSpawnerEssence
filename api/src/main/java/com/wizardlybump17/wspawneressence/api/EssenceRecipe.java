package com.wizardlybump17.wspawneressence.api;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public record EssenceRecipe(@NotNull Essence base) {

    public void register() {
        if (base.tier() == EssenceTier.LOW) {
            new EssenceRecipe(new Essence(base.mob(), base.tier().nextTier())).register();
            return;
        }

        ShapedRecipe recipe = getRecipe();
        NamespacedKey key = recipe.getKey();

        if (base.tier() != EssenceTier.SUPREME)
            new EssenceRecipe(new Essence(base.mob(), base.tier().nextTier())).register();
        if (Bukkit.getRecipe(key) == null)
            Bukkit.addRecipe(recipe);
        else {
            Bukkit.removeRecipe(key);
            Bukkit.addRecipe(recipe);
        }

        Bukkit.getPlayer("WizardlyBump17").undiscoverRecipe(key);
    }

    public ShapedRecipe getRecipe() {
        NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("WSpawnerEssence"), base.mob().name().toLowerCase() + "_" + base.tier().name().toLowerCase() + "_essence");

        ShapedRecipe recipe = new ShapedRecipe(key, new Essence(base.mob(), base.tier()).getItem());
        recipe.shape("AAA", "ABA", "AAC");
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(new Essence(base.mob(), base.tier().previousTier()).getItem()));
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(EssenceTier.BASE.getBaseItem()));
        recipe.setIngredient('C', new RecipeChoice.MaterialChoice(Material.NETHER_STAR));

        return recipe;
    }
}
