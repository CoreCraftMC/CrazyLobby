package me.corecraft.paper.listeners.players;

import ch.jalu.configme.SettingsManager;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.api.PaperUserManager;
import me.corecraft.paper.api.enums.Permissions;
import me.corecraft.paper.api.enums.types.BypassType;
import me.corecraft.paper.api.objects.PaperUser;
import me.corecraft.paper.configs.types.ProtectionKeys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerListener implements Listener {

    private final CrazyLobby plugin = CrazyLobby.get();

    private final PaperUserManager userManager = this.plugin.getUserManager();

    private final SettingsManager config = me.corecraft.paper.configs.ConfigManager.getConfig();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPickupEvent(PlayerPickItemEvent event) {
        final Player player = event.getPlayer();

        if (!this.config.getProperty(ProtectionKeys.event_prevent_item_pickup) || Permissions.event_item_pickup.hasPermission(player)) return;

        final PaperUser user = this.userManager.getUser(player.getUniqueId());

        if (user.activeBypassTypes.contains(BypassType.allow_item_pickup.getName())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (!this.config.getProperty(ProtectionKeys.event_prevent_hunger_change)) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        if (!this.config.getProperty(ProtectionKeys.event_prevent_item_drop) || Permissions.event_item_drop.hasPermission(player)) return;

        final PaperUser user = this.userManager.getUser(player.getUniqueId());

        if (user.activeBypassTypes.contains(BypassType.allow_item_drop.getName())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!this.config.getProperty(ProtectionKeys.event_prevent_death_message)) return;

        event.deathMessage(null);
    }
}