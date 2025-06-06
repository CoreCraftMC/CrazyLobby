package me.corecraft.paper.listeners;

import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.api.ItemManager;
import me.corecraft.paper.api.objects.items.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import java.util.Map;

public class TrafficListener implements Listener {

    private final CrazyLobby plugin = CrazyLobby.get();

    private final ItemManager itemManager = this.plugin.getItemManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        final Inventory inventory = player.getInventory();

        inventory.clear(); // clear inventory

        final Map<String, CustomItem> customItems = this.itemManager.getCustomItems();

        if (customItems.isEmpty()) return; // can't add any items since none exist.

        customItems.values().forEach(item -> {
            if (item.isForceGive()) {
                item.buildItem(player);
            }
        });
    }

    @EventHandler
    public void onPlayerTeleport(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        player.teleport(player.getWorld().getSpawnLocation().toCenterLocation());
    }
}