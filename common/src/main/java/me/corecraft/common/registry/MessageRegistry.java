package me.corecraft.common.registry;

import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import me.corecraft.api.interfaces.IMessage;
import me.corecraft.api.interfaces.registry.IMessageRegistry;
import me.corecraft.common.CrazyLobby;
import me.corecraft.common.constants.MessageKeys;
import me.corecraft.common.objects.Message;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class MessageRegistry implements IMessageRegistry {

    private final FusionCore fusion = FusionProvider.get();

    private final ILogger logger = this.fusion.getLogger();

    private final Map<Key, IMessage> messages = new HashMap<>();

    private final UserRegistry userRegistry;
    private final CrazyLobby instance;

    public MessageRegistry(@NotNull final CrazyLobby instance, @NotNull final UserRegistry userRegistry) {
        this.userRegistry = userRegistry;
        this.instance = instance;
    }

    @Override
    public void addMessage(@NotNull final Key key, @NotNull final IMessage message) {
        this.logger.safe("Registering the message {}", key.asString());

        this.messages.put(key, message);
    }

    @Override
    public void removeMessage(@NotNull final Key key) {
        if (!this.messages.containsKey(key)) {
            this.logger.warn("No message with key {}", key.asString());

            return;
        }

        this.logger.safe("Unregistering the message {}", key.asString());

        this.messages.remove(key);
    }

    @Override
    public @NotNull final IMessage getMessage(@NotNull final Key key) {
        return this.messages.get(key);
    }

    @Override
    public void init() {
        this.messages.clear();

        addMessage(MessageKeys.reload_plugin, new Message(this.instance, this.userRegistry, "{prefix}<yellow>You have reloaded the plugin!", "messages", "reload-plugin"));
        addMessage(MessageKeys.feature_disabled, new Message(this.instance, this.userRegistry, "{prefix}<red>This feature is disabled.", "messages", "feature-disabled"));
        addMessage(MessageKeys.must_be_console_sender, new Message(this.instance, this.userRegistry, "{prefix}<red>You must be using console to use this command.", "messages", "player", "requirements", "must-be-console-sender"));
        addMessage(MessageKeys.must_be_player, new Message(this.instance, this.userRegistry, "{prefix}<red>You must be a player to use this command.", "messages", "player", "requirements", "must-be-player"));
        addMessage(MessageKeys.target_not_online, new Message(this.instance, this.userRegistry, "{prefix}<red>This feature is disabled.", "messages", "player", "target-not-online"));
        addMessage(MessageKeys.target_same_player, new Message(this.instance, this.userRegistry, "{prefix}<red>You cannot use this command on yourself.", "messages", "player", "target-same-player"));
        addMessage(MessageKeys.no_permission, new Message(this.instance, this.userRegistry, "{prefix}<red>You do not have permission to use that command!", "messages", "player", "no-permission"));
        addMessage(MessageKeys.inventory_not_empty, new Message(this.instance, this.userRegistry, "{prefix}<red>Inventory is not empty, Please clear up some room.", "messages", "player", "inventory-not-empty"));

        addMessage(MessageKeys.bypass_toggle, new Message(this.instance, this.userRegistry, "{prefix}<green>{state} has been {status}", "messages", "commands", "bypass", "message"));
        addMessage(MessageKeys.bypass_toggle_enabled, new Message(this.instance, this.userRegistry, "<green>enabled", "messages", "commands", "bypass", "enabled"));
        addMessage(MessageKeys.bypass_toggle_disabled, new Message(this.instance, this.userRegistry, "<red>disabled", "messages", "commands", "bypass", "disabled"));
    }
}