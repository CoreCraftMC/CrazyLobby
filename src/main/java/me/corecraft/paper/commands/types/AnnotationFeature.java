package me.corecraft.paper.commands.types;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.api.ItemManager;
import me.corecraft.paper.api.MenuManager;
import me.corecraft.paper.api.PaperUserManager;
import me.corecraft.paper.configs.ConfigManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.jetbrains.annotations.NotNull;

public abstract class AnnotationFeature {

    protected final CrazyLobby plugin = CrazyLobby.get();

    protected final MenuManager menuManager = this.plugin.getMenuManager();

    protected final ItemManager itemManager = this.plugin.getItemManager();

    protected final FusionPaper fusion = this.plugin.getApi();

    protected final FileManager fileManager = this.plugin.getFileManager();

    protected final PaperUserManager userManager = this.plugin.getUserManager();

    protected final SettingsManager locale = ConfigManager.getLocale();

    public abstract void registerFeature(@NotNull final AnnotationParser<CommandSourceStack> parser);

}