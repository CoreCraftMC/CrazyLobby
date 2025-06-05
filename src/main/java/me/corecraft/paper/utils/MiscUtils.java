package me.corecraft.paper.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.corecraft.paper.CrazyLobby;
import me.corecraft.paper.api.enums.Messages;
import me.corecraft.paper.api.objects.items.CustomSound;
import me.corecraft.paper.configs.objects.SoundProperty;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MiscUtils {

    private static final CrazyLobby plugin = CrazyLobby.get();

    private static final ComponentLogger logger = plugin.getComponentLogger();

    public static void play(final Player player, final Location location, final String sound, double volume, float pitch, Sound.Source source) {
        new CustomSound(sound.isEmpty(), sound, volume, pitch, source).playSound(player, location);
    }

    public static void play(final Player player, final Location location, final SoundProperty property, final Sound.Source source) {
        play(player, location, property.value, property.volume, property.pitch, source);
    }

    public static void play(final Player player, final String sound, double volume, float pitch, Sound.Source source) {
        play(player, player.getLocation(), sound, volume, pitch, source);
    }

    public static void sendServer(final Player player, final boolean isVelocity, final String server) {
        if (!isVelocity || server.isEmpty()) return;

        try {
            final ByteArrayDataOutput output = ByteStreams.newDataOutput();

            output.writeUTF("Connect");
            output.writeUTF(server);

            player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
        } catch (final Exception exception) {
            Messages.internal_error.sendMessage(player);

            logger.warn("Failed to connect to {}", server, exception);
        }
    }
}