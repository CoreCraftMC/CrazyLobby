package me.corecraft.paper.commands.brigadier;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.commands.PaperCommandManager;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.commands.brigadier.types.admin.CommandReload;
import me.corecraft.paper.commands.brigadier.types.basic.CommandBypass;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BaseCommand extends AbstractPaperCommand {

    private final CrazyLobby plugin = CrazyLobby.get();

    private final FusionPaper paper = this.plugin.getApi();

    private final PaperCommandManager manager = this.paper.getCommandManager();

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {
        context.getPlayer().sendRichMessage("<red>This is the base command!");
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        return this.manager.hasPermission(source, getPermissionMode(), getPermissions());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        this.manager.registerPermissions(PermissionDefault.OP, getPermissions());

        return literal().createBuilder().build();
    }

    @Override
    public void unregister() {
        this.manager.unregisterPermissions(getPermissions());
    }

    @Override
    public @NotNull final String[] getPermissions() {
        return new String[]{"crazylobby.use"};
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("crazylobby")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new AbstractPaperContext(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<AbstractPaperCommand> getChildren() {
        return List.of(new CommandReload(), new CommandBypass());
    }
}