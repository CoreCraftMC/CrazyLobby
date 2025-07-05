package me.corecraft.paper.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import me.corecraft.paper.CrazyLobbyPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MiscUtils {

    private static final CrazyLobbyPlugin plugin = CrazyLobbyPlugin.getPlugin();

    private static final ILogger logger = FusionProvider.get().getLogger();

    public static void sendServer(@NotNull final Player player, final boolean isVelocity, @NotNull final String server) {
        if (!isVelocity || server.isEmpty()) return;

        try {
            final ByteArrayDataOutput output = ByteStreams.newDataOutput();

            output.writeUTF("Connect");
            output.writeUTF(server);

            player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
        } catch (final Exception exception) {
            logger.warn("Failed to connect to {}", server, exception);
        }
    }
}