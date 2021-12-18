package com.wizardlybump17.wspawneressence.command;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.sender.GenericSender;
import com.wizardlybump17.wspawneressence.api.EntityUtil;
import com.wizardlybump17.wspawneressence.api.Essence;
import com.wizardlybump17.wspawneressence.api.EssenceTier;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EssenceCommand {

    @Command(execution = "essence give")
    public void give(GenericSender sender) {
        sender.sendMessage("§e/essence give §7<tier> [mob] <player> <amount>");
    }

    @Command(execution = "essence give <tier> <player> <amount>", permission = "essence.give")
    public void give(GenericSender sender, EssenceTier tier, Player target, int amount) {
        give(sender, tier, null, target, amount);
    }

    @Command(execution = "essence give <tier> <mob> <player> <amount>", permission = "essence.give")
    public void give(GenericSender sender, EssenceTier tier, EntityType mob, Player target, int amount) {
        if (tier == null) {
            sender.sendMessage("§cInvalid tier.");
            return;
        }
        if (target == null) {
            sender.sendMessage("§cInvalid target.");
            return;
        }

        if (tier == EssenceTier.BASE) {
            ItemStack item = EssenceTier.BASE.getBaseItem();
            item.setAmount(amount);
            target.getInventory().addItem(item);

            sender.sendMessage("§aGiven " + amount + " Base Essences§a to " + target.getName());
            target.sendMessage("§aYou received " + amount + " Base Essences§a from " + sender.getName());
            return;
        }

        ItemStack item;
        String name;
        if (mob == null) {
            item = tier.getBaseItem();
            name = tier.getName();
        } else {
            if (!EntityUtil.getSpawnableEntities().contains(mob)) {
                sender.sendMessage("§cInvalid mob.");
                return;
            }

            Essence essence = new Essence(mob, tier);
            item = essence.getItem();
            name = essence.getName();
        }

        item.setAmount(amount);
        target.getInventory().addItem(item);

        sender.sendMessage("§aGiven " + amount + " " + name + "s§a to " + target.getName());
        target.sendMessage("§aYou received " + amount + " " + name + "s§a from " + sender.getName());
    }
}
