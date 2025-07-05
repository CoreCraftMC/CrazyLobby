package me.corecraft.paper.commands.brigadier.types.admin;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.corecraft.common.constants.MessageKeys;
import me.corecraft.common.registry.MessageRegistry;
import me.corecraft.paper.CrazyLobbyPlatform;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CommandReload extends PaperCommand {

    private final CrazyLobbyPlatform platform;
    private final MessageRegistry messageRegistry;

    public CommandReload(@NotNull final CrazyLobbyPlatform platform) {
        this.platform = platform;
        this.messageRegistry = platform.getMessageRegistry();
    }

    @Override
    public void execute(@NotNull final PaperCommandContext context) {
        this.platform.reload();

        this.messageRegistry.getMessage(MessageKeys.reload_plugin).send(context.getCommandSender());
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        return source.getSender().hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("reload")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new PaperCommandContext(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("crazylobby.reload");
    }
}