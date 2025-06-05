package me.corecraft.paper.api;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import me.corecraft.paper.LobbyPlus;
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

    private final LobbyPlus plugin = LobbyPlus.get();

    private final Path path = this.plugin.getDataPath();

    private final Server server = this.plugin.getServer();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final Map<String, CustomItem> customItems = new HashMap<>();

    public void load(final boolean isStartup) {
        this.customItems.clear();

        final Collection<? extends Player> players = this.server.getOnlinePlayers();

        FileUtils.getFiles(this.path.resolve("items"), ".yml").forEach(path -> {
            final YamlCustomFile file = this.fileManager.getYamlFile(path);

            if (file != null) {
                final CustomItem item = new CustomItem(file);

                if (!isStartup) {
                    for (final Player player : players) {
                        if (item.isForceGive()) {
                            item.buildItem(player);
                        }
                    }
                }

                this.customItems.put(path.getFileName().toString(), item);
            }
        });
    }

    public final CustomItem getCustomItem(@NotNull final String name) {
        return this.customItems.get(name);
    }

    public final Map<String, CustomItem> getCustomItems() {
        return Collections.unmodifiableMap(this.customItems);
    }
}