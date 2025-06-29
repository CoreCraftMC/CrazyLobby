package me.corecraft.paper.listeners;

import io.papermc.paper.persistence.PersistentDataContainerView;
import me.corecraft.common.objects.User;
import me.corecraft.common.registry.UserRegistry;
import me.corecraft.paper.CrazyLobbyPlatform;
import me.corecraft.paper.api.ItemManager;
import me.corecraft.paper.api.enums.Permissions;
import me.corecraft.paper.api.enums.keys.ItemKeys;
import me.corecraft.paper.api.enums.types.BypassType;
import me.corecraft.paper.api.objects.items.CustomItem;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ItemListener implements Listener {

    private final UserRegistry userRegistry;
    private final ItemManager itemManager;

    public ItemListener(@NotNull final CrazyLobbyPlatform platform) {
        this.userRegistry = platform.getUserRegistry();
        this.itemManager = platform.getItemManager();
    }

    @EventHandler()
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        final ItemStack item = event.getItem();

        final CustomItem customItem = isValid(item);

        if (customItem == null) return;

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(BypassType.allow_item_interact.getName()) || Permissions.event_item_interact.hasPermission(player)) return;

        customItem.execute(player);

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final ItemStack item = event.getCurrentItem();

        final CustomItem customItem = isValid(item);

        if (customItem == null) return;

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(BypassType.allow_item_interact.getName()) || Permissions.event_item_interact.hasPermission(player)) return;

        event.setCancelled(true);
    }

    private CustomItem isValid(final ItemStack item) {
        if (item == null) return null;

        final PersistentDataContainerView container = item.getPersistentDataContainer();

        final NamespacedKey key = ItemKeys.basic_item.getNamespacedKey();

        if (!container.has(key)) return null;

        final String fileName = container.get(key, PersistentDataType.STRING);

        if (fileName == null) return null;

        return this.itemManager.getCustomItem(fileName);
    }
}