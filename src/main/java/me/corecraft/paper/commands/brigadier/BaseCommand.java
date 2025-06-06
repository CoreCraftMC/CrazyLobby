package me.corecraft.paper.commands.brigadier;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.corecraft.paper.commands.brigadier.types.admin.CommandReload;
import me.corecraft.paper.commands.brigadier.types.basic.CommandBypass;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BaseCommand extends AbstractPaperCommand {

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {

    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        return source.getSender().hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("crazylobby").requires(this::requirement).build();
    }

    @Override
    public @NotNull final PermissionDefault getPermissionMode() {
        return PermissionDefault.OP;
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("crazylobby.use");
    }

    @Override
    public @NotNull final List<AbstractPaperCommand> getChildren() {
        return List.of(new CommandReload(), new CommandBypass());
    }
}