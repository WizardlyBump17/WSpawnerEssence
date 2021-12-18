package com.wizardlybump17.wspawneressence.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.wizardlybump17.wspawneressence.WSpawnerEssence;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import net.minecraft.network.protocol.game.PacketPlayInAutoRecipe;
import net.minecraft.network.protocol.game.PacketPlayOutAutoRecipe;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ShapedRecipe;

public class PacketListener extends PacketAdapter {

    public PacketListener(WSpawnerEssence plugin) {
        super(
                plugin,
                PacketType.fromClass(PacketPlayInAutoRecipe.class),
                PacketType.fromClass(PacketPlayOutAutoRecipe.class)
        );
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketPlayInAutoRecipe packet = (PacketPlayInAutoRecipe) event.getPacket().getHandle();
        NamespacedKey key = CraftNamespacedKey.fromMinecraft(packet.c());
        if (!key.getNamespace().equalsIgnoreCase("WSpawnerEssence"))
            return;

//        event.setCancelled(true);

        ShapedRecipe recipe = (ShapedRecipe) Bukkit.getRecipe(key);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        System.out.println(((PacketPlayOutAutoRecipe) event.getPacket().getHandle()).b());
    }
}
