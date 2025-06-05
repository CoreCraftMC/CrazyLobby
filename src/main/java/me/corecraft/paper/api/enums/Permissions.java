package me.corecraft.paper.api.enums;

import me.corecraft.paper.CrazyLobby;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import java.util.HashMap;
import java.util.Map;

public enum Permissions {

    event_block_interact("event.block.interact", "Ability to place/break blocks", PermissionDefault.OP, true),
    event_item_drop("event.item.drop", "Ability to drop items", PermissionDefault.OP, true),
    event_item_pickup("event.item.pickup", "Ability to pick up items", PermissionDefault.OP, true),

    event_player_pvp("event.player.pvp", "Allows the player to pvp", PermissionDefault.TRUE, false);

    private final String node;
    private final String description;
    private final PermissionDefault isDefault;
    private final Map<String, Boolean> children;

    private final boolean register;

    private final CrazyLobby plugin = CrazyLobby.get();

    private final PluginManager manager = this.plugin.getServer().getPluginManager();

    Permissions(final String node, final String description, final PermissionDefault isDefault, final Map<String, Boolean> children, final boolean register) {
        this.node = node;
        this.description = description;

        this.isDefault = isDefault;

        this.children = children;
        this.register = register;
    }

    Permissions(final String node, final String description, final PermissionDefault isDefault, final boolean register) {
        this.node = node;
        this.description = description;

        this.isDefault = isDefault;
        this.children = new HashMap<>();
        this.register = register;
    }

    public final String getNode() {
        return String.format("%s.%s", this.plugin.getName().toLowerCase(), this.node);
    }

    public final boolean shouldRegister() {
        return this.register;
    }

    public final String getDescription() {
        return this.description;
    }

    public final PermissionDefault isDefault() {
        return this.isDefault;
    }

    public final Map<String, Boolean> getChildren() {
        return this.children;
    }

    public final boolean hasPermission(final Player player) {
        return player.hasPermission(getNode());
    }

    public final boolean isValid() {
        return this.manager.getPermission(getNode()) != null;
    }

    public final Permission getPermission() {
        return new Permission(getNode(), getDescription(), isDefault());
    }

    public void registerPermission() {
        if (isValid()) return;

        this.manager.addPermission(getPermission());
    }

    public void unregisterPermission() {
        if (!isValid()) return;

        this.manager.removePermission(getNode());
    }
}