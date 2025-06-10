package me.corecraft.paper.listeners.players;

import com.ryderbelserion.fusion.paper.api.enums.Scheduler;
import com.ryderbelserion.fusion.paper.api.scheduler.FoliaScheduler;
import me.corecraft.common.enums.Files;
import me.corecraft.common.objects.User;
import me.corecraft.common.registry.UserRegistry;
import me.corecraft.paper.CrazyLobbyPlatform;
import me.corecraft.paper.CrazyLobbyPlugin;
import me.corecraft.paper.api.enums.Permissions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class DamageListener implements Listener {

    private final UserRegistry userRegistry;
    private final CrazyLobbyPlugin plugin;

    public DamageListener(@NotNull final CrazyLobbyPlatform platform) {
        this.userRegistry = platform.getUserRegistry();
        this.plugin = platform.getPlugin();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;

        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "prevent-mob-damage").getBoolean(true)) {
            return;
        }

        if (event.getEntity() instanceof Player player) {
            final User user = this.userRegistry.getUser(player);

            if (user.isCombatEnabled && Permissions.event_player_pvp.hasPermission(player)) {
                return;
            }
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player receiver) || !(event.getDamager() instanceof Player sender)) return;

        if (!Permissions.event_player_pvp.hasPermission(sender) || !Permissions.event_player_pvp.hasPermission(receiver)) return;

        final User target = this.userRegistry.getUser(receiver);
        final User damager = this.userRegistry.getUser(sender);

        if (target.isCombatEnabled && damager.isCombatEnabled) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        final User user = this.userRegistry.getUser(player);

        final EntityDamageEvent.DamageCause cause = event.getCause();

        if (user.isCombatEnabled) { // return as they should take all forms of damage.
            return;
        }

        final CommentedConfigurationNode config = Files.config.getConfig();

        if (config.node("root", "protection", "prevent-all-damage").getBoolean(true)) {
            if (cause == EntityDamageEvent.DamageCause.VOID) {
                teleport(player);
            }

            event.setCancelled(true);

            return;
        }

        if (cause == EntityDamageEvent.DamageCause.VOID && config.node("root", "protection", "prevent-void-damage").getBoolean(true)) {
            teleport(player);

            event.setCancelled(true);
        }
    }

    private void teleport(@NotNull final Player player) {
        player.setFallDistance(0.0F);

        new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
            @Override
            public void run() {
                player.teleport(player.getWorld().getSpawnLocation().toCenterLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }.runDelayed(3L);
    }
}