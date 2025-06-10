package me.corecraft.paper;

import com.ryderbelserion.fusion.kyori.components.KyoriLogger;
import com.ryderbelserion.fusion.paper.FusionPaper;
import me.corecraft.common.CrazyLobby;
import me.corecraft.paper.api.ItemManager;
import me.corecraft.paper.api.MenuManager;
import me.corecraft.paper.api.enums.Permissions;
import me.corecraft.paper.commands.brigadier.BaseCommand;
import me.corecraft.paper.listeners.CacheListener;
import me.corecraft.paper.listeners.ItemListener;
import me.corecraft.paper.listeners.TrafficListener;
import me.corecraft.paper.listeners.players.DamageListener;
import me.corecraft.paper.listeners.players.InteractionListener;
import me.corecraft.paper.listeners.players.PlayerListener;
import me.corecraft.paper.listeners.protection.DecayListener;
import me.corecraft.paper.listeners.protection.MiscListener;
import me.corecraft.paper.listeners.protection.MobListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.List;

public class CrazyLobbyPlatform extends CrazyLobby {

    private final CrazyLobbyPlugin plugin;
    private final FusionPaper fusion;
    private final KyoriLogger logger;
    private final Server server;
    private final Path path;

    public CrazyLobbyPlatform(@NotNull final CrazyLobbyPlugin plugin, @NotNull final FusionPaper fusion) {
        super(fusion.getPath(), fusion.getFileManager());

        this.fusion = fusion;

        this.plugin = plugin;

        fusion.enable(this.plugin);

        this.logger = this.fusion.getLogger();

        this.server = plugin.getServer();

        this.path = fusion.getPath();
    }

    private MenuManager menuManager;
    private ItemManager itemManager;

    @Override
    public void start() {
        super.start();

        final PluginManager pluginManager = this.server.getPluginManager();

        for (final Permissions value : Permissions.values()) {
            if (!value.shouldRegister()) continue;

            final String node = value.getNode();

            if (pluginManager.getPermission(node) == null) {
                final String description = value.getDescription();
                final PermissionDefault permissionDefault = value.isDefault();

                final Permission permission = new Permission(node, description, permissionDefault);

                pluginManager.addPermission(permission);

                this.logger.safe("Successfully added permission {} to the server! Default: {}", node, permissionDefault);
            } else {
                this.logger.warn("The permission {} is already added to the server!", node);
            }
        }

        this.menuManager = new MenuManager(this);
        this.menuManager.load();

        this.itemManager = new ItemManager(this);
        this.itemManager.load(true);

        List.of(
                new CacheListener(this),

                new InteractionListener(this),
                new DamageListener(this),
                new PlayerListener(this),
                new DecayListener(),
                new MiscListener(),
                new MobListener(),

                new TrafficListener(this),
                new ItemListener(this)
        ).forEach(event -> pluginManager.registerEvents(event, this.plugin));

        this.fusion.getCommandManager().enable(new BaseCommand(this), "Tu Tu!", List.of());
    }

    @Override
    public void reload() {
        this.fusion.reload(false);

        super.reload();

        this.menuManager.load();
        this.itemManager.load(false);
    }

    @Override
    public void stop() {
        super.stop();

        this.server.getGlobalRegionScheduler().cancelTasks(this.plugin);
        this.server.getAsyncScheduler().cancelTasks(this.plugin);
    }

    @Override
    public void broadcast(@NotNull final Component component, @NotNull final String permission) {
        if (permission.isEmpty()) {
            this.server.broadcast(component);

            return;
        }

        this.server.broadcast(component, permission);
    }

    @Override
    public void broadcast(@NotNull Component component) {
        broadcast(component, "");
    }

    public @NotNull final MenuManager getMenuManager() {
        return this.menuManager;
    }

    public @NotNull final ItemManager getItemManager() {
        return this.itemManager;
    }

    public @NotNull final CrazyLobbyPlugin getPlugin() {
        return this.plugin;
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }

    public @NotNull final Path getPath() {
        return this.path;
    }
}