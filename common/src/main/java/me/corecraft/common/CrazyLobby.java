package me.corecraft.common;

import com.ryderbelserion.fusion.core.files.FileAction;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.FileType;
import me.corecraft.api.interfaces.platform.ICrazyLobby;
import me.corecraft.common.registry.MessageRegistry;
import me.corecraft.common.registry.UserRegistry;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.ArrayList;

public abstract class CrazyLobby implements ICrazyLobby {

    private final FileManager fileManager;
    private final Path path;

    public CrazyLobby(@NotNull final Path path, @NotNull final FileManager fileManager) {
        this.fileManager = fileManager;
        this.path = path;
    }

    private MessageRegistry messageRegistry;
    private UserRegistry userRegistry;

    public abstract void broadcast(@NotNull final Component component, @NotNull final String permission);

    public abstract void broadcast(@NotNull final Component component);

    public abstract boolean isConsoleSender(@NotNull final Audience audience);

    @Override
    public void start() {
        this.fileManager.addFolder(path.resolve("menus"), FileType.YAML, new ArrayList<>(), null)
                .addFolder(path.resolve("items"), FileType.YAML, new ArrayList<>(), null)
                .addFolder(path.resolve("locale"), FileType.YAML, new ArrayList<>(), null)
                .addFile(path.resolve("config.yml"), new ArrayList<>(), null)
                .addFile(path.resolve("messages.yml"), new ArrayList<>(), null);

        this.messageRegistry = new MessageRegistry(this, this.userRegistry = new UserRegistry());
        this.messageRegistry.init();
    }

    @Override
    public void reload() {
        this.fileManager.refresh(false);

        this.fileManager.addFolder(path.resolve("items"), FileType.YAML, new ArrayList<>() {{
                    add(FileAction.RELOAD);
                }}, null)
                .addFolder(path.resolve("menus"), FileType.YAML, new ArrayList<>() {{
                    add(FileAction.RELOAD);
                }}, null)
                .addFolder(path.resolve("locale"), FileType.YAML, new ArrayList<>() {{
                    add(FileAction.RELOAD);
                }}, null);

        this.messageRegistry.init();
    }

    @Override
    public void stop() {

    }

    @Override
    public @NotNull final FileManager getFileManager() {
        return this.fileManager;
    }

    @Override
    public @NotNull final UserRegistry getUserRegistry() {
        return this.userRegistry;
    }

    @Override
    public @NotNull final MessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }
}