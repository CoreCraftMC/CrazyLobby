package me.corecraft.paper.api;

import com.ryderbelserion.fusion.core.api.utils.FileUtils;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import me.corecraft.paper.CrazyLobbyPlatform;
import me.corecraft.paper.api.objects.menus.CustomMenu;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MenuManager {

    private final CrazyLobbyPlatform platform;
    private final FileManager fileManager;
    private final Path path;

    public MenuManager(@NotNull final CrazyLobbyPlatform platform) {
        this.fileManager = platform.getFileManager();
        this.path = platform.getPath();
        this.platform = platform;
    }

    private final Map<String, CustomMenu> customMenus = new HashMap<>();

    public void load() {
        this.customMenus.clear();

        FileUtils.getFiles(this.path.resolve("menus"), ".yml").forEach(path -> {
            final YamlCustomFile file = this.fileManager.getYamlFile(path);

            if (file != null) {
                final CustomMenu menu = new CustomMenu(this.platform, file);

                this.customMenus.put(path.getFileName().toString(), menu);
            }
        });
    }

    public @NotNull final CustomMenu getCustomMenu(@NotNull final String name) {
        return this.customMenus.get(name);
    }

    public @NotNull final Map<String, CustomMenu> getCustomMenus() {
        return Collections.unmodifiableMap(this.customMenus);
    }
}