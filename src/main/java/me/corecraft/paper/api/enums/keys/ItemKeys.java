package me.corecraft.paper.api.enums.keys;

import me.corecraft.paper.CrazyLobby;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public enum ItemKeys {

    basic_item("basic_item", PersistentDataType.STRING);

    private final CrazyLobby plugin = CrazyLobby.get();

    private final String NamespacedKey;
    private final PersistentDataType type;

    ItemKeys(@NotNull final String NamespacedKey, @NotNull final PersistentDataType type) {
        this.NamespacedKey = NamespacedKey;
        this.type = type;
    }

    public @NotNull final org.bukkit.NamespacedKey getNamespacedKey() {
        return new NamespacedKey(this.plugin, this.plugin.getName().toLowerCase() + "_" + this.NamespacedKey);
    }

    public @NotNull final PersistentDataType getType() {
        return this.type;
    }
}