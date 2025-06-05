package me.corecraft.paper.commands.types.admin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.corecraft.paper.api.enums.Messages;
import me.corecraft.paper.commands.types.AnnotationFeature;
import me.corecraft.paper.configs.ConfigManager;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.NotNull;

public class CommandReload extends AnnotationFeature {

    @Override
    public void registerFeature(@NotNull final AnnotationParser<CommandSourceStack> parser) {
        parser.parse(this);
    }

    @Command("crazylobby reload")
    @CommandDescription("Reloads the plugin!")
    @Permission(value = "crazylobby.reload", mode = Permission.Mode.ANY_OF)
    public void reload(final CommandSender sender) {
        this.fusion.reload(false);

        ConfigManager.reload();

        this.menuManager.load();

        this.itemManager.load(false);

        Messages.reload_plugin.sendMessage(sender);
    }
}