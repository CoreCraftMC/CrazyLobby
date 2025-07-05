package me.corecraft.paper.commands.brigadier.types.basic;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.corecraft.common.constants.MessageKeys;
import me.corecraft.common.objects.User;
import me.corecraft.common.registry.MessageRegistry;
import me.corecraft.common.registry.UserRegistry;
import me.corecraft.paper.CrazyLobbyPlatform;
import me.corecraft.paper.api.enums.types.BypassType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBypass extends PaperCommand {

    private final MessageRegistry messageRegistry;
    private final UserRegistry userRegistry;

    public CommandBypass(@NotNull final CrazyLobbyPlatform platform) {
        this.messageRegistry = platform.getMessageRegistry();
        this.userRegistry = platform.getUserRegistry();
    }

    @Override
    public void execute(@NotNull final PaperCommandContext context) {
        if (!context.isPlayer()) {
            this.messageRegistry.getMessage(MessageKeys.must_be_player).send(context.getCommandSender());

            return;
        }

        final Player player = context.getPlayer();

        final BypassType type = BypassType.getBypassType(context.getStringArgument("bypass_type"));

        final User user = this.userRegistry.getUser(player);

        if (user.activeBypassTypes.contains(type.getName())) {
            user.activeBypassTypes.remove(type.getName());

            this.messageRegistry.getMessage(MessageKeys.bypass_toggle).send(player, new HashMap<>() {{
                put("{state}", type.getPrettyName());
                put("{status}", messageRegistry.getMessage(MessageKeys.bypass_toggle_disabled).asString(player));
            }});

            return;
        }

        user.activeBypassTypes.add(type.getName());

        this.messageRegistry.getMessage(MessageKeys.bypass_toggle).send(player, new HashMap<>() {{
            put("{state}", type.getPrettyName());
            put("{status}", messageRegistry.getMessage(MessageKeys.bypass_toggle_enabled).asString(player));
        }});
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        return source.getSender().hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("bypass").requires(this::requirement);

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = argument("bypass_type", StringArgumentType.string()).suggests((ctx, builder) -> {
            for (final BypassType value : BypassType.values()) {
                builder.suggest(value.getName());
            }

            return builder.buildFuture();
        }).executes(context -> {
            execute(new PaperCommandContext(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1).build();
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("crazylobby.bypass");
    }
}