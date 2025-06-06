package me.corecraft.paper.api;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.api.objects.menus.CustomMenu;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MenuManager {

    private final CrazyLobby plugin = CrazyLobby.get();

    private final Path path = this.plugin.getDataPath();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final Map<String, CustomMenu> customMenus = new HashMap<>();

    public void load() {
        this.customMenus.clear();

        FileUtils.getFiles(this.path.resolve("menus"), ".yml").forEach(path -> {
            final YamlCustomFile file = this.fileManager.getYamlFile(path);

            if (file != null) {
                final CustomMenu menu = new CustomMenu(file);

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