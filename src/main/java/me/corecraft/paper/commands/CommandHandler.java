package me.corecraft.paper.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.corecraft.paper.api.enums.Messages;
import me.corecraft.paper.commands.types.AnnotationFeature;
import me.corecraft.paper.commands.types.admin.CommandReload;
import me.corecraft.paper.commands.types.basic.CommandBypass;
import me.corecraft.paper.commands.types.basic.CommandHelp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.injection.ParameterInjectorRegistry;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;

public class CommandHandler {

    private static final List<AnnotationFeature> features = Arrays.asList(
            new CommandReload(),
            new CommandBypass(),
            new CommandHelp()
    );

    private final AnnotationParser<CommandSourceStack> parser;

    public CommandHandler(@NotNull final PaperCommandManager<CommandSourceStack> manager) {
        final ParameterInjectorRegistry<CommandSourceStack> injector = manager.parameterInjectorRegistry();

        injector.registerInjector(CommandSender.class, (context, accessor) -> context.sender().getSender());
        injector.registerInjector(Player.class, (context, accessor) -> {
            final CommandSender sender = context.sender().getSender();

            if (sender instanceof Player player) {
                return player;
            }

            Messages.must_be_a_player.sendMessage(sender);

            return null;
        });

        this.parser = new AnnotationParser<>(manager, CommandSourceStack.class);

        register();
    }

    private void register() {
        features.forEach(feature -> feature.registerFeature(this.parser));
    }
}