package me.corecraft.paper.listeners.protection;

import ch.jalu.configme.SettingsManager;
import me.corecraft.paper.configs.ConfigManager;
import me.corecraft.paper.configs.types.ProtectionKeys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class DecayListener implements Listener {

    private final SettingsManager config = ConfigManager.getConfig();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        if (!this.config.getProperty(ProtectionKeys.event_prevent_block_burn)) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (!this.config.getProperty(ProtectionKeys.event_prevent_fire_spread)) return;

        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLeafDecay(LeavesDecayEvent event) {
        if (!this.config.getProperty(ProtectionKeys.event_prevent_leaf_decay)) return;

        event.setCancelled(true);
    }
}