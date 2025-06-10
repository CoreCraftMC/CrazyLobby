package me.corecraft.paper.listeners.players;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import me.corecraft.common.enums.Files;
import me.corecraft.common.objects.User;
import me.corecraft.common.registry.UserRegistry;
import me.corecraft.paper.CrazyLobbyPlatform;
import me.corecraft.paper.api.enums.Permissions;
import me.corecraft.paper.api.enums.types.BypassType;
import me.corecraft.paper.api.objects.items.CustomSound;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.List;

public class InteractionListener implements Listener {

    private final UserRegistry userRegistry;

    public InteractionListener(@NotNull final CrazyLobbyPlatform platform) {
        this.userRegistry = platform.getUserRegistry();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        final Block block = event.getClickedBlock();

        if (block == null || block.getType().isAir()) return;

        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "block", "prevent-interaction", "toggle").getBoolean(true) || Permissions.event_block_interact.hasPermission(player)) return;

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        final Material material = block.getType();

        // if the material is in this list and is right-clicked, consider it a non-destructive item
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && getStringList(config.node("root", "protection", "block", "interactable-items")).contains(material.getKey().getKey().toLowerCase())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        final Block block = event.getBlock();

        if (block.getType().isAir()) return;

        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "block", "prevent-interaction", "toggle").getBoolean(true) || Permissions.event_block_interact.hasPermission(player)) return;

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        new CustomSound(config.node("root", "protection", "block", "prevent-interaction", "sound"), Sound.Source.PLAYER).playSound(player);

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();

        final Block block = event.getBlock();

        if (block.getType().isAir()) return;

        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "block", "prevent-interaction", "toggle").getBoolean(true) || Permissions.event_block_interact.hasPermission(player)) return;

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        new CustomSound(config.node("root", "protection", "block", "prevent-interaction", "sound"), Sound.Source.PLAYER).playSound(player);

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemFrameDamage(EntityDamageByEntityEvent event) {
        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "block", "prevent-interaction", "toggle").getBoolean(true) || !(event.getEntity() instanceof ItemFrame)) return;

        final Entity damager = event.getDamager();

        if (damager instanceof Player player) {
            if (Permissions.event_block_interact.hasPermission(player)) return;

            final User user = this.userRegistry.getUser(player);

            if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
                return;
            }
        }

        if (damager instanceof Projectile) {
            damager.remove();
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "block", "prevent-interaction", "toggle").getBoolean(true) || !(event.getRightClicked() instanceof ItemFrame)) return;

        final Player player = event.getPlayer();

        if (Permissions.event_block_interact.hasPermission(player)) return;

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        new CustomSound(config.node("root", "protection", "block", "prevent-interaction", "sound"), Sound.Source.PLAYER).playSound(player);

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingEntityDestroy(HangingBreakByEntityEvent event) {
        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "block", "prevent-interaction", "toggle").getBoolean(true)) return;

        final Entity entity = event.getEntity();

        if (!(event.getRemover() instanceof Player player) || !(entity instanceof Painting) || !(entity instanceof ItemFrame)) return;

        if (Permissions.event_block_interact.hasPermission(player)) return;

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(BypassType.allow_block_interact.getName())) {
            return;
        }

        new CustomSound(config.node("root", "protection", "block", "prevent-interaction", "sound"), Sound.Source.PLAYER).playSound(player);

        event.setCancelled(true);
    }

    private @NotNull List<String> getStringList(@NotNull final CommentedConfigurationNode node) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : interactables;
        } catch (SerializationException exception) {
            throw new FusionException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }

    private static final List<String> interactables = List.of(
            "oak_door",
            "acacia_door",
            "birch_door",
            "dark_oak_door",
            "spruce_door",
            "jungle_door",

            "oak_fence_gate",
            "acacia_fence_gate",
            "birch_fence_gate",
            "dark_oak_fence_gate",
            "spruce_fence_gate",
            "jungle_fence_gate",

            "oak_trapdoor",
            "acacia_trapdoor",
            "birch_trapdoor",
            "dark_oak_trapdoor",
            "spruce_trapdoor",
            "jungle_trapdoor",

            "oak_sign",
            "acacia_sign",
            "birch_sign",
            "dark_oak_sign",
            "spruce_sign",
            "jungle_sign",

            "oak_button",
            "acacia_button",
            "birch_button",
            "dark_oak_button",
            "jungle_button",

            "anvil",
            "beacon",

            "red_bed",
            "brewing_stand",
            "daylight_detector",
            "dispenser",
            "hopper",
            "dropper",

            "enchanting_table",
            "furnace",

            "chest_minecart",
            "hopper_minecart",
            "minecart",

            "lever",
            "note_block",
            "comparator",

            "chest",
            "ender_chest",
            "trapped_chest"
    );
}