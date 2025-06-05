package me.corecraft.paper.configs.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import me.corecraft.api.enums.Action;
import org.jetbrains.annotations.NotNull;

import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigKeys implements SettingsHolder {

    protected ConfigKeys() {}

    @Override
    public void registerComments(@NotNull CommentsConfiguration configuration) {
        configuration.setComment("root", "The root config section");
    }

    @Comment("The prefix that appears in front of commands!")
    public static final Property<String> command_prefix = newProperty("root.prefix", "<dark_gray>[<red>Core<white>Craft<dark_gray>] <reset>");

    @Comment({
            "This option will tell the plugin to send all messages as action bars or messages in chat.",
            "",
            "send_message -> sends messages in chat.",
            "send_actionbar -> sends messages in actionbar.",
            ""
    })
    public static final Property<Action> message_action = newBeanProperty(Action.class, "root.message-action", Action.send_message);

}