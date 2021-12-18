package com.wizardlybump17.wspawneressence.listener;

import com.wizardlybump17.wlib.util.RandomUtil;
import com.wizardlybump17.wspawneressence.WSpawnerEssence;
import com.wizardlybump17.wspawneressence.api.EntityUtil;
import com.wizardlybump17.wspawneressence.api.Essence;
import com.wizardlybump17.wspawneressence.api.EssenceRecipe;
import com.wizardlybump17.wspawneressence.api.EssenceTier;
import net.minecraft.network.protocol.game.PacketPlayInAutoRecipe;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Optional;

public record EntityListener(WSpawnerEssence plugin) implements Listener {

    private void discoverRecipes(Player player, Essence essence) {
        EssenceRecipe recipe = new EssenceRecipe(essence);
        EssenceRecipe nextRecipe = new EssenceRecipe(new Essence(essence.mob(), essence.tier().nextTier()));

        player.discoverRecipe(recipe.getRecipe().getKey());
        player.discoverRecipe(nextRecipe.getRecipe().getKey());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity && event.getDamager() instanceof Player player))
            return;

        if (EntityUtil.getSpawnableEntities().contains(entity.getType()) && entity.getHealth() - event.getFinalDamage() <= 0 && RandomUtil.checkPercentage(5 + percentage(33, player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS))))
            entity.getWorld().dropItemNaturally(entity.getLocation(), new Essence(event.getEntityType(), EssenceTier.LOW).getItem());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        ItemStack item = event.getItem().getItemStack();

        Optional<Essence> optional = Essence.fromItem(item);
        if (optional.isEmpty() || optional.get().mob() == null)
            return;

        discoverRecipes(player, optional.get());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND)
            return;

        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof LivingEntity entity) || entity.getHealth() <= 0)
            return;

        ItemStack item = player.getInventory().getItemInMainHand();
        Optional<Essence> optional = Essence.fromItem(item);
        if (optional.isEmpty() || optional.get().mob() != null || optional.get().tier() == EssenceTier.BASE)
            return;

        if (EntityUtil.getSpawnableEntities().contains(entity.getType())) {
            entity.damage(Float.MAX_VALUE, player);

            item.setAmount(item.getAmount() - 1);
            if (item.getAmount() <= 0)
                player.getInventory().setItemInMainHand(null);
            HashMap<Integer, ItemStack> left = player.getInventory().addItem(new Essence(entity.getType(), optional.get().tier()).getItem());
            if (!left.isEmpty())
                left.forEach((integer, itemStack) -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
        }
    }

    @EventHandler
    public void prepareCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (recipe == null)
            return;

        if (Essence.fromItem(recipe.getResult()).isPresent())
            return;

        ItemStack[] matrix = event.getInventory().getMatrix();

        for (ItemStack item : matrix) {
            if (Essence.fromItem(item).isPresent()) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void craftItem(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        Essence.fromItem(result).ifPresent(essence -> discoverRecipes((Player) event.getWhoClicked(), essence));
    }

    private static double percentage(double percentage, double number) {
        return percentage / 100 * number;
    }
}
