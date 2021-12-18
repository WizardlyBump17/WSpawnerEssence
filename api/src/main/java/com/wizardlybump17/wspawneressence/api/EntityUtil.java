package com.wizardlybump17.wspawneressence.api;

import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.List;

public class EntityUtil {

    private static List<EntityType> spawnableEntities;

    public static List<EntityType> getSpawnableEntities() {
        return Collections.unmodifiableList(spawnableEntities);
    }

    public static void setSpawnableEntities(List<EntityType> spawnableEntities) {
        if (EntityUtil.spawnableEntities == null)
            EntityUtil.spawnableEntities = spawnableEntities;
    }
}
