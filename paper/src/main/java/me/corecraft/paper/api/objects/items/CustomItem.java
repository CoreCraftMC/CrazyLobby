package me.corecraft.paper.api.objects.items;

import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.fusion.paper.api.enums.Scheduler;
import com.ryderbelserion.fusion.paper.api.scheduler.FoliaScheduler;
import me.corecraft.paper.CrazyLobbyPlatform;
import me.corecraft.paper.CrazyLobbyPlugin;
import me.corecraft.paper.api.MenuManager;
import me.corecraft.paper.api.enums.keys.ItemKeys;
import me.corecraft.paper.api.objects.menus.CustomMenu;
import me.corecraft.paper.utils.ItemUtils;
import me.corecraft.paper.utils.MiscUtils;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class CustomItem {

    private final CrazyLobbyPlugin plugin;
    private final MenuManager menuManager;
    private final FusionPaper fusion;

    private final ItemBuilder itemBuilder;

    private final String fileName;

    private final boolean forceGive;

    private final boolean isVelocity;
    private final String server;

    private final List<String> commands;
    private final List<String> messages;

    private final int slot;

    private final CustomSound sound;

    private final CustomMenu customMenu;

    public CustomItem(@NotNull final CrazyLobbyPlatform platform, @NotNull final YamlCustomFile customFile) {
        this.menuManager = platform.getMenuManager();
        this.plugin = platform.getPlugin();
        this.fusion = platform.getFusion();

        this.fileName = customFile.getFileName();

        final CommentedConfigurationNode configuration = customFile.getConfiguration();

        this.itemBuilder = ItemUtils.getBuilder(configuration.node("item"));

        this.forceGive = configuration.node("force-give").getBoolean(false);

        this.isVelocity = configuration.node("velocity", "enabled").getBoolean(false);
        this.server = configuration.node("velocity", "server").getString("");

        this.slot = customFile.getIntValue("item", "slot");

        this.commands = customFile.getStringList("commands");

        this.messages = customFile.getStringList("messages");

        this.sound = new CustomSound(configuration.node("sound"), Sound.Source.MASTER);

        this.customMenu = this.menuManager.getCustomMenu(configuration.node("open-menu").getString(""));
    }

    public final boolean isForceGive() {
        return this.forceGive;
    }

    public void execute(@NotNull final Player player) {
        if (this.customMenu != null) {
            this.customMenu.build(player);
        }

        executeCommands(player);
        executeMessages(player);
        executeSound(player);

        sendServer(player);
    }

    public void buildItem(@NotNull final Player player) {
        final Inventory inventory = player.getInventory();

        final ItemBuilder itemBuilder = this.itemBuilder.addPlaceholder("{player}", player.getName());

        final ItemStack itemStack = itemBuilder.asItemStack(player);

        itemStack.editPersistentDataContainer(container -> container.set(ItemKeys.basic_item.getNamespacedKey(), PersistentDataType.STRING, this.fileName));

        inventory.setItem(this.slot, itemStack);
    }

    public void sendServer(@NotNull final Player player) {
        MiscUtils.sendServer(player, this.isVelocity, this.server);
    }

    public void executeCommands(@NotNull final Player player) {
        if (this.commands.isEmpty()) return;

        final String name = player.getName();

        final Server server = this.plugin.getServer();

        new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
            @Override
            public void run() {
                commands.forEach(command -> {
                    final String output = command.replaceAll("\\{player}", name);

                    server.dispatchCommand(server.getConsoleSender(), output);
                });
            }
        }.runNow();
    }

    public void executeMessages(@NotNull final Player player) {
        if (this.messages.isEmpty()) return;

        final String name = player.getName();

        this.messages.forEach(command -> this.fusion.sendMessage(player, command, new HashMap<>() {{
            put("{player}", name);
        }}));
    }

    public void executeSound(@NotNull final Player player) {
        if (this.sound == null) return;

        this.sound.playSound(player, player.getLocation());
    }
}