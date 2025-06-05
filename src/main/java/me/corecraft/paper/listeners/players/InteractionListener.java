package me.corecraft.paper.listeners.players;

import ch.jalu.configme.SettingsManager;
import me.corecraft.paper.LobbyPlus;
import me.corecraft.paper.api.PaperUserManager;
import me.corecraft.paper.api.enums.Permissions;
import me.corecraft.paper.api.enums.types.BypassType;
import me.corecraft.paper.api.objects.PaperUser;
import me.corecraft.paper.configs.ConfigManager;
import me.corecraft.paper.configs.types.ProtectionKeys;
import me.corecraft.paper.utils.MiscUtils;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener {

    private final LobbyPlus plugin = LobbyPlus.get();

    private final PaperUserManager userManager = this.plugin.getUserManager();

    private final SettingsManager config = ConfigManager.getConfig();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        final Block block = event.getBlock();

        if (block.getType().isAir()) return;

        if (!this.config.getProperty(ProtectionKeys.event_prevent_block_interact) || Permissions.event_block_interact.hasPermission(player)) return;

        final PaperUser user = this.userManager.getUser(player.getUniqueId());

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        MiscUtils.play(player, block.getLocation(), this.config.getProperty(ProtectionKeys.event_prevent_interact_sound), Sound.Source.PLAYER);

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();

        final Block block = event.getBlock();

        if (block.getType().isAir()) return;

        if (!this.config.getProperty(ProtectionKeys.event_prevent_block_interact) || Permissions.event_block_interact.hasPermission(player)) return;

        final PaperUser user = this.userManager.getUser(player.getUniqueId());

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        MiscUtils.play(player, block.getLocation(), this.config.getProperty(ProtectionKeys.event_prevent_interact_sound), Sound.Source.PLAYER);

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        final Block block = event.getClickedBlock();

        if (block == null || block.getType().isAir()) return;

        if (!this.config.getProperty(ProtectionKeys.event_prevent_block_interact) || Permissions.event_block_interact.hasPermission(player)) return;

        final PaperUser user = this.userManager.getUser(player.getUniqueId());

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        final Material material = block.getType();

        // if the material is in this list, consider it a non-destructive item
        if (this.config.getProperty(ProtectionKeys.event_interactable_items).contains(material.getKey().getKey().toLowerCase())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemFrameDamage(EntityDamageByEntityEvent event) {
        if (!this.config.getProperty(ProtectionKeys.event_prevent_block_interact) || !(event.getEntity() instanceof ItemFrame)) return;

        if (event.getDamager() instanceof Player player) {
            if (Permissions.event_block_interact.hasPermission(player)) return;

            final PaperUser user = this.userManager.getUser(player.getUniqueId());

            if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
                return;
            }
        }

        if (event.getDamager() instanceof Projectile) {
            event.getDamager().remove();
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
        if (!this.config.getProperty(ProtectionKeys.event_prevent_block_interact) || !(event.getRightClicked() instanceof ItemFrame)) return;

        final Player player = event.getPlayer();

        if (Permissions.event_block_interact.hasPermission(player)) return;

        final PaperUser user = this.userManager.getUser(player.getUniqueId());

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingEntityDestroy(HangingBreakByEntityEvent event) {
        if (!this.config.getProperty(ProtectionKeys.event_prevent_block_interact)) return;

        final Entity entity = event.getEntity();

        if (!(event.getRemover() instanceof Player player) || !(entity instanceof Painting) || !(entity instanceof ItemFrame)) return;

        if (Permissions.event_block_interact.hasPermission(player)) return;

        final PaperUser user = this.userManager.getUser(player.getUniqueId());

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        MiscUtils.play(player, entity.getLocation(), this.config.getProperty(ProtectionKeys.event_prevent_interact_sound), Sound.Source.PLAYER);

        event.setCancelled(true);
    }
}