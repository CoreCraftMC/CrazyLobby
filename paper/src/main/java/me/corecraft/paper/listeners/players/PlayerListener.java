package me.corecraft.paper.listeners.players;

import me.corecraft.common.enums.Files;
import me.corecraft.common.objects.User;
import me.corecraft.common.registry.UserRegistry;
import me.corecraft.paper.CrazyLobbyPlatform;
import me.corecraft.paper.api.enums.Permissions;
import me.corecraft.paper.api.enums.types.BypassType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class PlayerListener implements Listener {

    private final UserRegistry userRegistry;

    public PlayerListener(@NotNull final CrazyLobbyPlatform platform) {
        this.userRegistry = platform.getUserRegistry();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPickupEvent(PlayerAttemptPickupItemEvent event) {
        final Player player = event.getPlayer();

        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "item", "prevent-pickup").getBoolean(true) || Permissions.event_item_pickup.hasPermission(player)) return;

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(BypassType.allow_item_pickup.getName())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "prevent-hunger-change").getBoolean(true)) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "item", "prevent-drop").getBoolean(true) || Permissions.event_item_drop.hasPermission(player)) return;

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(BypassType.allow_item_drop.getName())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "prevent-death-message").getBoolean(true)) return;

        event.deathMessage(null);
    }
}