package me.corecraft.paper.api.enums;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.utils.StringUtils;
import me.corecraft.api.enums.Action;
import me.corecraft.api.objects.User;
import me.corecraft.paper.LobbyPlus;
import me.corecraft.paper.api.PaperUserManager;
import me.corecraft.paper.configs.ConfigManager;
import me.corecraft.paper.configs.types.ConfigKeys;
import me.corecraft.paper.configs.types.locale.ErrorKeys;
import me.corecraft.paper.configs.types.locale.RootKeys;
import me.corecraft.paper.configs.types.locale.ToggleKeys;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public enum Messages {

    must_be_console_sender(RootKeys.must_be_console_sender),
    inventory_not_empty(RootKeys.inventory_not_empty),
    must_be_a_player(RootKeys.must_be_a_player),
    feature_disabled(RootKeys.feature_disabled),
    unknown_command(RootKeys.unknown_command),
    player_not_found(RootKeys.not_online),
    no_permission(RootKeys.no_permission),
    reload_plugin(RootKeys.reload_plugin),
    correct_usage(RootKeys.correct_usage),
    same_player(RootKeys.same_player),

    internal_error(ErrorKeys.internal_error),
    message_empty(ErrorKeys.message_empty),

    bypass_toggle(ToggleKeys.bypass_toggle),
    toggle_enabled(ToggleKeys.toggle_enabled),
    toggle_disabled(ToggleKeys.toggle_disabled);

    private Property<String> property;

    private Property<List<String>> properties;
    private boolean isList = false;

    Messages(@NotNull final Property<String> property) {
        this.property = property;
    }

    Messages(@NotNull final Property<List<String>> properties, final boolean isList) {
        this.properties = properties;
        this.isList = isList;
    }
    
    private final LobbyPlus plugin = LobbyPlus.get();

    private final Server server = this.plugin.getServer();

    private final PaperUserManager userManager = this.plugin.getUserManager();

    private final SettingsManager config = ConfigManager.getConfig();

    private final SettingsManager locale = ConfigManager.getLocale();

    private final FusionKyori api = (FusionKyori) FusionCore.Provider.get();

    public void sendMessage(@NotNull final Audience audience, @NotNull final Action action, @NotNull final Map<String, String> placeholders) {
        final Component component = parse(audience, placeholders);

        if (component.equals(Component.empty())) return;

        switch (action) {
            case send_message -> audience.sendMessage(component);
            case send_actionbar -> audience.sendActionBar(component);
        }
    }

    public void sendMessage(@NotNull final Audience audience, @NotNull final String placeholder, @NotNull final String replacement) {
        sendMessage(audience, new HashMap<>() {{
            put(placeholder, replacement);
        }});
    }

    public void sendMessage(@NotNull final Audience audience, @NotNull final Map<String, String> placeholders) {
        sendMessage(audience, this.config.getProperty(ConfigKeys.message_action), placeholders);
    }

    public void sendMessage(@NotNull final Audience audience) {
        sendMessage(audience, this.config.getProperty(ConfigKeys.message_action), new HashMap<>());
    }

    public void broadcast(@NotNull final Audience audience, @NotNull final Map<String, String> placeholders) {
        sendMessage(audience, placeholders);

        for (final Audience value : this.server.getOnlinePlayers()) {
            sendMessage(value, placeholders);
        }
    }

    public void broadcast(@NotNull final Audience audience) {
        broadcast(audience, new HashMap<>());
    }

    private Component parse(@NotNull final Audience audience, @NotNull final Map<String, String> placeholders) {
        placeholders.putIfAbsent("{prefix}", this.config.getProperty(ConfigKeys.command_prefix));

        return this.api.color(audience, this.isList ? StringUtils.toString(getList(audience)) : getString(audience), placeholders);
    }

    private String getString(@NotNull final Audience audience) {
        final UUID uuid = audience.getOrDefault(Identity.UUID, null);

        if (uuid != null) {
            final User user = this.userManager.getUser(uuid);

            return user.locale().getProperty(property);
        }

        return this.locale.getProperty(this.property);
    }

    private List<String> getList(@NotNull final Audience audience) {
        if (!isList()) {
            return List.of(getString(audience));
        }

        final Optional<UUID> uuid = audience.get(Identity.UUID);

        if (uuid.isPresent()) {
            final User user = this.userManager.getUser(uuid.get());

            return user.locale().getProperty(this.properties);
        }

        return this.locale.getProperty(this.properties);
    }

    public final boolean isList() {
        return this.isList;
    }
}