package me.corecraft.common.objects;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.utils.StringUtils;
import me.corecraft.api.interfaces.IMessage;
import me.corecraft.common.CrazyLobby;
import me.corecraft.common.enums.Files;
import me.corecraft.common.registry.UserRegistry;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message implements IMessage {

    private final FusionKyori kyori = (FusionKyori) FusionCore.Provider.get();

    private final CommentedConfigurationNode config = Files.config.getConfig();

    private final UserRegistry userRegistry;
    private final CrazyLobby instance;

    private final String defaultValue;
    private final Object[] path;

    public Message(@NotNull final CrazyLobby instance, @NotNull final UserRegistry userRegistry, @NotNull final String defaultValue, @NotNull final Object... path) {
        this.userRegistry = userRegistry;
        this.instance = instance;

        // config data
        this.defaultValue = defaultValue;
        this.path = path;
    }

    @Override
    public void broadcast(@NotNull final Audience audience, @NotNull final Map<String, String> placeholders) {
        final Component component = getComponent(audience, placeholders);

        if (component.equals(Component.empty())) return;

        this.instance.broadcast(component);
    }

    @Override
    public void broadcast(@NotNull final Audience audience) {
        broadcast(audience, new HashMap<>());
    }

    @Override
    public void send(@NotNull final Audience audience, @NotNull final Map<String, String> placeholders) {
        final Component component = getComponent(audience, placeholders);

        if (component.equals(Component.empty())) return;

        switch (this.config.node("root", "message-action").getString("send_message")) {
            case "send_actionbar" -> audience.sendActionBar(component);
            case "send_message" -> audience.sendMessage(component);
        }
    }

    @Override
    public void send(@NotNull final Audience audience) {
        send(audience, new HashMap<>());
    }

    @Override
    public Component getComponent(@NotNull final Audience audience, @NotNull final Map<String, String> placeholders) {
        final User user = this.userRegistry.getUser(audience);

        final CommentedConfigurationNode node = user.locale().node(this.path);

        return parse(node.isList() ? StringUtils.toString(getStringList(node)) : node.getString(this.defaultValue), audience, placeholders);
    }

    @Override
    public Component getComponent(@NotNull final Audience audience) {
        return getComponent(audience, new HashMap<>());
    }

    private @NotNull Component parse(@NotNull final String message, @NotNull final Audience audience, @NotNull final Map<String, String> placeholders) {
        placeholders.putIfAbsent("{prefix}", this.config.node("root", "prefix").getString("<blue>[<gold>ChatManager<blue>] <reset>"));

        return this.kyori.color(audience, message, placeholders);
    }

    private @NotNull List<String> getStringList(@NotNull final CommentedConfigurationNode node) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : List.of(this.defaultValue);
        } catch (SerializationException exception) {
            throw new FusionException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }
}