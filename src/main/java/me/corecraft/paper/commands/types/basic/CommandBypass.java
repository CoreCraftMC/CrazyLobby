package me.corecraft.paper.commands.types.basic;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.corecraft.paper.api.enums.Messages;
import me.corecraft.paper.api.enums.types.BypassType;
import me.corecraft.paper.api.objects.PaperUser;
import me.corecraft.paper.commands.types.AnnotationFeature;
import me.corecraft.paper.configs.types.locale.ToggleKeys;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;

public class CommandBypass extends AnnotationFeature {

    @Command(value = "crazylobby bypass <type>", requiredSender = Player.class)
    @Permission(value = "crazylobby.bypass", mode = Permission.Mode.ANY_OF)
    public void bypass(final Player player, @Argument("type") @NotNull final BypassType type) {
        final PaperUser user = this.userManager.getUser(player.getUniqueId());

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
    public void registerFeature(@NotNull final AnnotationParser<CommandSourceStack> parser) {
        parser.parse(this);
    }
}