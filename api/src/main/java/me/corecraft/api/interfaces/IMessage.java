package me.corecraft.api.interfaces;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IMessage {

    void broadcast(@NotNull final Audience audience, @NotNull final Map<String, String> placeholders);

    void broadcast(@NotNull final Audience audience);

    void send(@NotNull final Audience audience, @NotNull final Map<String, String> placeholders);

    void send(@NotNull final Audience audience);

    Component asComponent(@NotNull final Audience audience, @NotNull final Map<String, String> placeholders);

    Component asComponent(@NotNull final Audience audience);

    String asString(@NotNull final Audience audience);

}