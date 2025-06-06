package me.corecraft.paper.commands.brigadier.types.basic;

import ch.jalu.configme.SettingsManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.api.PaperUserManager;
import me.corecraft.paper.api.enums.Messages;
import me.corecraft.paper.api.enums.types.BypassType;
import me.corecraft.paper.api.objects.PaperUser;
import me.corecraft.paper.configs.types.locale.ToggleKeys;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;

import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBypass extends AbstractPaperCommand {

    private final CrazyLobby plugin = CrazyLobby.get();

    private final PaperUserManager userManager = this.plugin.getUserManager();

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {
        if (!context.isPlayer()) {
            Messages.must_be_a_player.sendMessage(context.getCommandSender());

            return;
        }

        final Player player = context.getPlayer();

        final BypassType type = BypassType.getBypassType(context.getStringArgument("bypass_type"));

        final PaperUser user = this.userManager.getUser(player.getUniqueId());

        final SettingsManager locale = user.locale();

        if (user.activeBypassTypes.contains(type.getName())) {
            user.activeBypassTypes.remove(type.getName());

            Messages.bypass_toggle.sendMessage(player, new HashMap<>() {{
                put("{state}", type.getPrettyName());
                put("{status}", locale.getProperty(ToggleKeys.toggle_disabled));
            }});

            return;
        }

        user.activeBypassTypes.add(type.getName());

        Messages.bypass_toggle.sendMessage(player, new HashMap<>() {{
            put("{state}", type.getPrettyName());
            put("{status}", locale.getProperty(ToggleKeys.toggle_enabled));
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
            execute(new AbstractPaperContext(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1).build();
    }

    @Override
    public @NotNull final PermissionDefault getPermissionMode() {
        return PermissionDefault.OP;
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("crazylobby.bypass");
    }
}