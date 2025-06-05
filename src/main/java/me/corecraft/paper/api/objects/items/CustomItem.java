package me.corecraft.paper.api.objects.items;

import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.fusion.paper.api.enums.Scheduler;
import com.ryderbelserion.fusion.paper.api.scheduler.FoliaScheduler;
import me.corecraft.paper.CrazyLobby;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class CustomItem {

    private final CrazyLobby plugin = CrazyLobby.get();

    private final MenuManager menuManager = this.plugin.getMenuManager();

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

    public CustomItem(final YamlCustomFile customFile) {
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

    public void execute(final Player player) {
        if (this.customMenu != null) {
            this.customMenu.build(player);
        }

        executeCommands(player);
        executeMessages(player);
        executeSound(player);

        sendServer(player);
    }

    public void buildItem(final Player player) {
        final Inventory inventory = player.getInventory();

        final ItemBuilder itemBuilder = this.itemBuilder.addPlaceholder("{player}", player.getName());

        final ItemStack itemStack = itemBuilder.asItemStack(player);

        itemStack.editMeta(itemMeta -> {
            final PersistentDataContainer container = itemMeta.getPersistentDataContainer();

            container.set(ItemKeys.basic_item.getNamespacedKey(), PersistentDataType.STRING, this.fileName);
        });

        inventory.setItem(this.slot, itemStack);
    }

    public void sendServer(final Player player) {
        MiscUtils.sendServer(player, this.isVelocity, this.server);
    }

    public void executeCommands(final Player player) {
        if (this.commands.isEmpty()) return;

        final String name = player.getName();

        final Server server = this.plugin.getServer();

        this.commands.forEach(command -> {
            final String output = command.replaceAll("\\{player}", name);

            new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
                @Override
                public void run() {
                    server.dispatchCommand(server.getConsoleSender(), output);
                }
            }.runNow();
        });
    }

    public void executeMessages(final Player player) {
        if (this.messages.isEmpty()) return;

        final String name = player.getName();

        final FusionPaper fusion = this.plugin.getApi();

        this.messages.forEach(command -> fusion.sendMessage(player, command, new HashMap<>() {{
            put("{player}", name);
        }}));
    }

    public void executeSound(final Player player) {
        if (this.sound == null) return;

        this.sound.playSound(player, player.getLocation());
    }
}