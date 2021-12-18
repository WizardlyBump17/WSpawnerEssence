package com.wizardlybump17.wspawneressence.api;

import com.wizardlybump17.wlib.item.Item;
import com.wizardlybump17.wlib.util.StringUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;

@Getter
@RequiredArgsConstructor
public enum EssenceTier {

//    BASE(ChatColor.WHITE, Item.builder().type(Material.COCOA_BEANS).customModelData(9).build()),
//    LOW(ChatColor.WHITE, Item.builder().type(Material.COCOA_BEANS).customModelData(10).build()),
//    NORMAL(ChatColor.GREEN, Item.builder().type(Material.LIME_DYE).customModelData(11).build()),
//    HIGH(ChatColor.of(new Color(141, 183, 241)), Item.builder().type(Material.LIGHT_BLUE_DYE).customModelData(12).build()),
//    EXTREME(ChatColor.GOLD, Item.builder().type(Material.ORANGE_DYE).customModelData(13).build()),
//    SUPREME(ChatColor.RED, Item.builder().type(Material.RED_DYE).customModelData(14).build());
    BASE(ChatColor.WHITE, new ItemStack(Material.COCOA_BEANS)),
    LOW(ChatColor.WHITE, new ItemStack(Material.COCOA_BEANS)),
    NORMAL(ChatColor.GREEN, new ItemStack(Material.LIME_DYE)),
    SUPREME(ChatColor.WHITE, new ItemStack(Material.RED_DYE));

    private final ChatColor color;
    private final ItemStack item;

    public ItemStack getBaseItem() {
        ItemStack item = new ItemStack(this.item);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName());
        meta.getPersistentDataContainer().set(
                new NamespacedKey(Bukkit.getPluginManager().getPlugin("WSpawnerEssence"), "tier"),
                PersistentDataType.STRING,
                name()
        );
        item.setItemMeta(meta);
        return item;
//        return Item.fromItemStack(item).displayName(getName()).nbtTag("WSpawnerEssence", Map.of("tier", name())).build();
    }

    public EssenceTier previousTier() {
        if (this == BASE)
            return this;
        return values()[ordinal() - 1];
    }

    public EssenceTier nextTier() {
        if (this == SUPREME)
            return this;
        return values()[ordinal() + 1];
    }

    public String getName() {
        return color + StringUtil.getName(this) + " Tier Essence";
    }
}
