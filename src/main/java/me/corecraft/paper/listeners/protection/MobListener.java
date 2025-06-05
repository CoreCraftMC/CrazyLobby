package me.corecraft.paper.listeners.protection;

import ch.jalu.configme.SettingsManager;
import me.corecraft.paper.configs.ConfigManager;
import me.corecraft.paper.configs.types.ProtectionKeys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobListener implements Listener {

    private final SettingsManager config = ConfigManager.getConfig();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!this.config.getProperty(ProtectionKeys.event_prevent_mob_spawning) || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) return;

        event.setCancelled(true);
    }
}