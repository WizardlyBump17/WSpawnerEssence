package com.wizardlybump17.wspawneressence.api;

import com.wizardlybump17.wlib.item.Item;
import com.wizardlybump17.wlib.util.StringUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public record Essence(@Nullable EntityType mob, @NotNull EssenceTier tier) {

    public ItemStack getItem() {
        return Item.fromItemStack(tier.getItem())
                .displayName(getName())
                .nbtTag("WSpawnerEssence",
                        mob == null ?
                                Map.of("tier", tier.name()) :
                                Map.of("mob", mob.name(), "tier", tier.name())
                )
                .build();
    }

    public String getName() {
        if (mob == null)
            return tier.getName();
        return tier.getColor() + StringUtil.getName(tier) + " Tier " + StringUtil.getName(mob) + " Essence";
    }

    @SuppressWarnings("unchecked")
    public static Optional<Essence> fromItem(ItemStack item) {
        Item.ItemBuilder builder = Item.fromItemStack(item);
        if (!builder.hasNbtTag("WSpawnerEssence"))
            return Optional.empty();

        Map<String, String> nbt = (Map<String, String>) builder.getNbtTag("WSpawnerEssence");
        return Optional.of(new Essence(
                nbt.containsKey("mob") ? EntityType.valueOf(nbt.get("mob")) : null,
                EssenceTier.valueOf(nbt.get("tier")))
        );
    }
}
