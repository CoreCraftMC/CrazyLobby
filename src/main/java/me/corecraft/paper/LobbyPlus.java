package me.corecraft.paper;

import com.ryderbelserion.fusion.core.api.plugins.interfacers.IPlugin;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import me.corecraft.paper.api.ItemManager;
import me.corecraft.paper.api.MenuManager;
import me.corecraft.paper.api.PaperUserManager;
import me.corecraft.paper.api.enums.Permissions;
import me.corecraft.paper.commands.CommandHandler;
import me.corecraft.paper.configs.ConfigManager;
import me.corecraft.paper.listeners.CacheListener;
import me.corecraft.paper.listeners.ItemListener;
import me.corecraft.paper.listeners.TrafficListener;
import me.corecraft.paper.listeners.players.DamageListener;
import me.corecraft.paper.listeners.players.InteractionListener;
import me.corecraft.paper.listeners.players.PlayerListener;
import me.corecraft.paper.listeners.protection.DecayListener;
import me.corecraft.paper.listeners.protection.MiscListener;
import me.corecraft.paper.listeners.protection.MobListener;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.List;

public class LobbyPlus extends JavaPlugin {

    public static LobbyPlus get() {
        return JavaPlugin.getPlugin(LobbyPlus.class);
    }

    private FusionPaper api = null;

    private FileManager fileManager;

    private PaperUserManager userManager;
    private ItemManager itemManager;
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        final Server server = getServer();

        server.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        final ComponentLogger logger = getComponentLogger();

        this.api = new FusionPaper(logger, getDataPath());
        this.api.enable(this);

        this.fileManager = this.api.getFileManager();

        ConfigManager.load(); // load config

        this.userManager = new PaperUserManager();

        final PluginManager pluginManager = server.getPluginManager();

        final boolean isVerbose = this.api.isVerbose();

        for (final Permissions value : Permissions.values()) {
            if (!value.shouldRegister()) continue;

            final String node = value.getNode();

            if (pluginManager.getPermission(node) == null) {
                final String description = value.getDescription();
                final PermissionDefault permissionDefault = value.isDefault();

                final Permission permission = new Permission(node, description, permissionDefault);

                pluginManager.addPermission(permission);

                if (isVerbose) {
                    logger.info("Successfully added permission {} to the server! Default: {}", node, permissionDefault);
                }
            } else {
                if (isVerbose) {
                    logger.warn("The permission {} is already added to the server!", node);
                }
            }
        }

        this.menuManager = new MenuManager();
        this.menuManager.load();

        this.itemManager = new ItemManager();
        this.itemManager.load(true);

        List.of(
                new CacheListener(),

                new InteractionListener(),
                new DamageListener(),
                new PlayerListener(),
                new DecayListener(),
                new MiscListener(),
                new MobListener(),

                new TrafficListener(),
                new ItemListener()
        ).forEach(listener -> pluginManager.registerEvents(listener, this));

        new CommandHandler(PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(this));
    }

    @Override
    public @NotNull Path getDataPath() {
        return super.getDataPath();
    }

    @Override
    public void onDisable() {
        final Server server = getServer();

        if (this.api != null) {
            this.api.disable();

            final IPlugin packets = this.api.getPluginBuilder().getPlugin("packetevents");

            if (packets != null) {
                packets.stop();
            }
        }

        final PluginManager pluginManager = server.getPluginManager();
        final ComponentLogger logger = getComponentLogger();
        final boolean isVerbose = this.api.isVerbose();

        // this is to account for plugman oddities
        for (final Permissions value : Permissions.values()) {
            final String node = value.getNode();

            if (pluginManager.getPermission(node) != null) {
                pluginManager.removePermission(node);

                if (isVerbose) {
                    logger.info("Successfully removed permission {} from the server, since we are disabling.", node);
                }
            } else {
                if (isVerbose) {
                    logger.warn("The permission {} was not removed from the server since it was not found!", node);
                }
            }
        }

        this.fileManager.purge();

        server.getGlobalRegionScheduler().cancelTasks(this);
        server.getAsyncScheduler().cancelTasks(this);
    }

    public final PaperUserManager getUserManager() {
        return this.userManager;
    }

    public final FileManager getFileManager() {
        return this.fileManager;
    }

    public final MenuManager getMenuManager() {
        return this.menuManager;
    }

    public final ItemManager getItemManager() {
        return this.itemManager;
    }

    public final FusionPaper getApi() {
        return this.api;
    }
}