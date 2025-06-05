package me.corecraft.paper.api.objects;

import ch.jalu.configme.SettingsManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.configs.ConfigManager;
import me.corecraft.paper.configs.objects.help.HelpProperty;
import me.corecraft.paper.configs.objects.help.HighlightProperty;
import me.corecraft.paper.configs.types.ConfigKeys;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PaperHelp {

    private final CrazyLobby plugin = CrazyLobby.get();

    private final PaperCommandManager<CommandSourceStack> manager = this.plugin.getManager();

    private final SettingsManager config = ConfigManager.getConfig();

    private MinecraftHelp<CommandSourceStack> help;

    public void init() {
        final HelpProperty property = this.config.getProperty(ConfigKeys.help_property);

        final TextColor primaryColor = getColor(property.getPrimaryColor());

        final HighlightProperty highlightProperty = property.getHighlight();

        final TextColor highlightColor = getColor(highlightProperty.getHighlightColor());
        final TextColor highlightColorAlternative = getColor(highlightProperty.getHighlightColorAlternative());
        final TextColor textColor = getColor(property.getTextColor());
        final TextColor accentColor = getColor(property.getAccentColor());

        this.help = MinecraftHelp.<CommandSourceStack>builder()
                .commandManager(this.manager)
                .audienceProvider(CommandSourceStack::getSender)
                .commandPrefix("/crazylobby help")
                .colors(MinecraftHelp.helpColors(
                        primaryColor != null ? primaryColor : NamedTextColor.GOLD,
                        highlightColor != null ? highlightColor : NamedTextColor.GREEN,
                        highlightColorAlternative != null ? highlightColorAlternative : NamedTextColor.YELLOW,
                        textColor != null ? textColor : NamedTextColor.GRAY,
                        accentColor != null ? accentColor : NamedTextColor.DARK_GRAY
                ))
                .maxResultsPerPage(property.getResultsPerPage())
                .build();
    }

    public final MinecraftHelp<CommandSourceStack> getHelp() {
        return this.help;
    }

    public @Nullable TextColor getColor(@NotNull final String color) {
        if (color.isEmpty()) return null;

        final String[] rgb = color.split(",");

        if (rgb.length != 3) {
            return null;
        }

        return TextColor.color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }
}