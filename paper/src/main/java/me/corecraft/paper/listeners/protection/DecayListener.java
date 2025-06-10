package me.corecraft.paper.listeners.protection;

import me.corecraft.common.enums.Files;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class DecayListener implements Listener {

    private final CommentedConfigurationNode config = Files.config.getConfig();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        if (!this.config.node("root", "protection", "block", "prevent-block-burn").getBoolean(true)) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (!this.config.node("root", "protection", "block", "prevent-fire-spread").getBoolean(true)) return;

        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLeafDecay(LeavesDecayEvent event) {
        if (!this.config.node("root", "protection", "block", "prevent-leaf-decay").getBoolean(true)) return;

        event.setCancelled(true);
    }
}