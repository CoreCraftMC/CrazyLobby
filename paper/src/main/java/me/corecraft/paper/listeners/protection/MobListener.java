package me.corecraft.paper.listeners.protection;

import me.corecraft.common.enums.Files;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class MobListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "block", "prevent-mob-spawning").getBoolean(true) || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplosion(EntityExplodeEvent event) {
        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "block", "prevent-entity-explosion").getBoolean(true)) return;

        event.setCancelled(true);
    }
}