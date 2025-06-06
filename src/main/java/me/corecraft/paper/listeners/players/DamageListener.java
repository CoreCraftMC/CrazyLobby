package me.corecraft.paper.listeners.players;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.paper.api.enums.Scheduler;
import com.ryderbelserion.fusion.paper.api.scheduler.FoliaScheduler;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.api.PaperUserManager;
import me.corecraft.paper.api.enums.Permissions;
import me.corecraft.paper.api.objects.PaperUser;
import me.corecraft.paper.configs.ConfigManager;
import me.corecraft.paper.configs.types.ProtectionKeys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class DamageListener implements Listener {

    private final CrazyLobby plugin = CrazyLobby.get();

    private final PaperUserManager userManager = this.plugin.getUserManager();

    private final SettingsManager config = ConfigManager.getConfig();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player receiver) || !(event.getDamager() instanceof Player sender)) return;

        if (!Permissions.event_player_pvp.hasPermission(sender) || !Permissions.event_player_pvp.hasPermission(receiver)) return;

        final PaperUser target = this.userManager.getUser(receiver.getUniqueId());
        final PaperUser damager = this.userManager.getUser(sender.getUniqueId());

        if (target.isCombatEnabled && damager.isCombatEnabled) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        final PaperUser user = this.userManager.getUser(player.getUniqueId());

        final EntityDamageEvent.DamageCause cause = event.getCause();

        if (user.isCombatEnabled) { // return as they should take all forms of damage.
            return;
        }

        if (this.config.getProperty(ProtectionKeys.event_prevent_all_damage)) {
            if (cause == EntityDamageEvent.DamageCause.VOID) {
                teleport(player);
            }

            event.setCancelled(true);

            return;
        }

        if (cause == EntityDamageEvent.DamageCause.VOID && this.config.getProperty(ProtectionKeys.event_prevent_void_damage)) {
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