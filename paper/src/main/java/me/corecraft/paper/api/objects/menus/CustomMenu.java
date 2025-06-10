package me.corecraft.paper.api.objects.menus;

import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.Gui;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import me.corecraft.common.enums.Files;
import me.corecraft.paper.CrazyLobbyPlatform;
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

    private final FusionPaper fusion;

    private final String fileName;

    private final String title;
    private final int rows;

    private final boolean isFillerEnabled;
    private final ItemBuilder fillerItem;

    private final List<CustomButton> buttons = new ArrayList<>();

    private final Gui gui;

    public CustomMenu(@NotNull final CrazyLobbyPlatform platform, @NotNull final YamlCustomFile customFile) {
        this.fusion = platform.getFusion();

        this.fileName = customFile.getFileName();

        final CommentedConfigurationNode configuration = customFile.getConfiguration();

        this.title = configuration.node("title").getString("");
        this.rows = configuration.node("rows").getInt(6);

        this.isFillerEnabled = configuration.node("filler", "toggle").getBoolean(false);
        this.fillerItem = ItemUtils.getBuilder(configuration.node("filler"));

        configuration.node("buttons").childrenMap().forEach((object, child) -> this.buttons.add(new CustomButton(child)));

        this.gui = Gui.gui(platform.getPlugin()).disableInteractions().setTitle(this.title).setRows(this.rows).create();
    }

    private final CommentedConfigurationNode config = Files.config.getConfig();

    public void build(@NotNull final Player player) {
        final String prefix = this.config.node("root", "prefix").getString("<dark_gray>[<red>Core<white>Craft<dark_gray>] <reset>");

        if (this.isFillerEnabled) {
            this.gui.getFiller().fill(this.fillerItem.asGuiItem(player));
        }

        this.buttons.forEach(button -> this.gui.setItem(button.getSlot(), button.getItemBuilder().addPlaceholder("{player}", player.getName()).asGuiItem(player, action -> {
            if (!(action.getWhoClicked() instanceof Player clicker)) return;

            final Server server = clicker.getServer();

            button.getCommands().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replaceAll("\\{player}", player.getName())));

            button.getMessages().forEach(message -> this.fusion.sendMessage(player, message, new HashMap<>() {{
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