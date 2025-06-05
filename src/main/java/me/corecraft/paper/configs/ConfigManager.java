package me.corecraft.paper.configs;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.files.FileAction;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.configs.types.ConfigKeys;
import me.corecraft.paper.configs.types.ProtectionKeys;
import me.corecraft.paper.configs.types.locale.ErrorKeys;
import me.corecraft.paper.configs.types.locale.RootKeys;
import me.corecraft.paper.configs.types.locale.ToggleKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private static final YamlFileResourceOptions options = YamlFileResourceOptions.builder().indentationSize(2).build();

    private static final CrazyLobby plugin = CrazyLobby.get();

    private static final Path path = plugin.getDataPath();

    private static final FileManager fileManager = plugin.getFileManager();

    public static void load() {
        // extract first, because I really dislike how ConfigMe formats it.
        FileUtils.extract("locale", path, new ArrayList<>() {{
            add(FileAction.FOLDER);
        }});

        List.of(
                "config.yml",
                "messages.yml"
        ).forEach(file -> FileUtils.extract(file, path, new ArrayList<>()));

        fileManager.addFolder(path.resolve("menus"), FileType.YAML, new ArrayList<>(), null)
                .addFolder(path.resolve("items"), FileType.YAML, new ArrayList<>(), null)
                .addFolder(path.resolve("locale"), builder -> builder.configurationData(RootKeys.class, ErrorKeys.class, ToggleKeys.class), new ArrayList<>(), options)
                .addFile(path.resolve("config.yml"), builder -> builder.configurationData(ConfigKeys.class, ProtectionKeys.class), new ArrayList<>(), options)
                .addFile(path.resolve("messages.yml"), builder -> builder.configurationData(RootKeys.class, ErrorKeys.class, ToggleKeys.class), new ArrayList<>(), options);
    }

    public static void reload() {
        fileManager.refresh(false); // reloads all files!

        // we must run this because of object mapping when accounting for new files.
        fileManager.addFolder(path.resolve("menus"), FileType.YAML, new ArrayList<>(), null)
                .addFolder(path.resolve("locale"), builder -> builder.configurationData(RootKeys.class, ErrorKeys.class, ToggleKeys.class), new ArrayList<>() {{
            add(FileAction.RELOAD);
        }}, options);
    }

    public static SettingsManager getLocale(@NotNull final String locale) {
        @Nullable final JaluCustomFile customFile = fileManager.getJaluFile(path.resolve("locale").resolve(locale));

        if (customFile == null) {
            return getLocale();
        }

        return customFile.getConfiguration();
    }

    public static SettingsManager getConfig() {
        @Nullable final JaluCustomFile customFile = fileManager.getJaluFile(path.resolve("config.yml"));

        if (customFile == null) {
            throw new FusionException("The config.yml file cannot be found.");
        }

        return customFile.getConfiguration();
    }

    public static SettingsManager getLocale() {
        @Nullable final JaluCustomFile customFile = fileManager.getJaluFile(path.resolve("messages.yml"));

        if (customFile == null) {
            throw new FusionException("The messages.yml file cannot be found.");
        }

        return customFile.getConfiguration();
    }
}