package me.corecraft.paper.api.objects.menus;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.Gui;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.configs.ConfigManager;
import me.corecraft.paper.configs.types.ConfigKeys;
import me.corecraft.paper.utils.ItemUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class CustomMenu {

    private final CrazyLobby plugin = CrazyLobby.get();

    private final FusionPaper paper = this.plugin.getApi();

    private final SettingsManager config = ConfigManager.getConfig();

    private final String fileName;

    private final String title;
    private final int rows;

    private final boolean isFillerEnabled;
    private final ItemBuilder fillerItem;

    private final List<CustomButton> buttons = new ArrayList<>();

    private final Gui gui;

    public CustomMenu(@NotNull final YamlCustomFile customFile) {
        this.fileName = customFile.getFileName();

        final CommentedConfigurationNode configuration = customFile.getConfiguration();

        this.title = configuration.node("title").getString("");
        this.rows = configuration.node("rows").getInt(6);

        this.isFillerEnabled = configuration.node("filler", "toggle").getBoolean(false);
        this.fillerItem = ItemUtils.getBuilder(configuration.node("filler"));

        configuration.node("buttons").childrenMap().forEach((object, child) -> this.buttons.add(new CustomButton(child)));

        this.gui = Gui.gui(this.plugin).disableInteractions().setTitle(this.title).setRows(this.rows).create();
    }

    public void build(@NotNull final Player player) {
        final String prefix = this.config.getProperty(ConfigKeys.command_prefix);

        if (this.isFillerEnabled) {
            this.gui.getFiller().fill(this.fillerItem.asGuiItem(player));
        }

        this.buttons.forEach(button -> this.gui.setItem(button.getSlot(), button.getItemBuilder().addPlaceholder("{player}", player.getName()).asGuiItem(player, action -> {
            if (!(action.getWhoClicked() instanceof Player clicker)) return;

            final Server server = clicker.getServer();

            button.getCommands().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replaceAll("\\{player}", player.getName())));

            button.getMessages().forEach(message -> this.paper.sendMessage(player, message, new HashMap<>() {{
                put("{player}", player.getName());
                put("{prefix}", prefix);
            }}));

            button.executeSound(player);

            button.sendServer(player);
        })));

        this.gui.open(player);
    }

    public @NotNull final String getFileName() {
        return this.fileName;
    }
}