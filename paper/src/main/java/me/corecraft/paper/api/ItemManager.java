package me.corecraft.paper.api;

import com.ryderbelserion.fusion.core.api.utils.FileUtils;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import me.corecraft.paper.CrazyLobbyPlatform;
import me.corecraft.paper.api.objects.items.CustomItem;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    private final CrazyLobbyPlatform platform;
    private final FileManager fileManager;
    private final Server server;
    private final Path path;

    public ItemManager(@NotNull final CrazyLobbyPlatform platform) {
        this.server = platform.getPlugin().getServer();
        this.fileManager = platform.getFileManager();
        this.path = platform.getPath();
        this.platform = platform;
    }

    private final Map<String, CustomItem> customItems = new HashMap<>();

    public void load(final boolean isStartup) {
        this.customItems.clear();

        final Collection<? extends Player> players = this.server.getOnlinePlayers();

        FileUtils.getFiles(this.path.resolve("items"), ".yml").forEach(path -> {
            final YamlCustomFile file = this.fileManager.getYamlFile(path);

            if (file != null) {
                final CustomItem item = new CustomItem(this.platform, file);

                if (!isStartup) {
                    for (final Player player : players) {
                        player.getInventory().clear(); // clear inventory

                        if (item.isForceGive()) {
                            item.buildItem(player);
                        }
                    }
                }

                this.customItems.put(path.getFileName().toString(), item);
            }
        });
    }

    public @NotNull final CustomItem getCustomItem(@NotNull final String name) {
        return this.customItems.get(name);
    }

    public @NotNull final Map<String, CustomItem> getCustomItems() {
        return Collections.unmodifiableMap(this.customItems);
    }
}