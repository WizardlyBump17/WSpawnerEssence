package com.wizardlybump17.wspawneressence;

import com.comphenix.protocol.ProtocolLibrary;
import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.holder.BukkitCommandHolder;
import com.wizardlybump17.wspawneressence.api.EntityUtil;
import com.wizardlybump17.wspawneressence.api.Essence;
import com.wizardlybump17.wspawneressence.api.EssenceRecipe;
import com.wizardlybump17.wspawneressence.api.EssenceTier;
import com.wizardlybump17.wspawneressence.command.EssenceCommand;
import com.wizardlybump17.wspawneressence.command.reader.EssenceTierArgsReader;
import com.wizardlybump17.wspawneressence.listener.EntityListener;
import com.wizardlybump17.wspawneressence.listener.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class WSpawnerEssence extends JavaPlugin {

    @Override
    public void onEnable() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketListener(this));

        ArgsReaderRegistry.INSTANCE.add(new EssenceTierArgsReader());
        new CommandManager(new BukkitCommandHolder(this)).registerCommands(new EssenceCommand());
        Bukkit.getPluginManager().registerEvents(new EntityListener(this), this);

        initEssences();
    }

    private void initEssences() {
        List<EntityType> types = getCreativeMobTypes();
        for (EntityType mob : types) {
            EssenceRecipe recipe = new EssenceRecipe(new Essence(mob, EssenceTier.LOW));
            recipe.register();
        }

        EntityUtil.setSpawnableEntities(types);
    }

    private List<EntityType> getCreativeMobTypes() {
        Material[] materials = Material.values();
        List<EntityType> types = new ArrayList<>(materials.length);
        for (Material material : materials) {
            if (material == Material.MOOSHROOM_SPAWN_EGG) {
                types.add(EntityType.MUSHROOM_COW);
                continue;
            }

            String name = material.name();
            if (name.contains("_SPAWN_EGG")) {
                String mobName = name.replace("_SPAWN_EGG", "");
                types.add(EntityType.valueOf(mobName));
            }
        }
        return types;
    }
}
