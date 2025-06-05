package me.corecraft.paper.configs.types.locale;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ToggleKeys implements SettingsHolder {

    @Comment({
            "A list of available placeholders:",
            "",
            "{state} which returns whatever argument was supplied in /lobbytools bypass <state>",
            "",
            "{status} returns enabled or disabled, which can be configured below."
    })
    public static final Property<String> bypass_toggle = newProperty("messages.commands.bypass.message", "{prefix}<green>{state} has been {status}");

    public static final Property<String> toggle_enabled = newProperty("messages.commands.bypass.enabled", "<green>enabled");

    public static final Property<String> toggle_disabled = newProperty("messages.commands.bypass.disabled", "<red>disabled");
}