package me.corecraft.paper.api.objects.items;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class CustomSound {

    private final boolean isEnabled;
    private final Sound sound;

    public CustomSound(final boolean isEnabled, @NotNull final String sound, final double volume, final float pitch, @NotNull final Sound.Source source) {
        this.isEnabled = isEnabled || sound.isEmpty();

        this.sound = this.isEnabled ? Sound.sound(Key.key(sound), source, (float) volume, pitch) : null;
    }

    public CustomSound(@NotNull final CommentedConfigurationNode config, @NotNull final Sound.Source source) {
        this(config.node("toggle").getBoolean(false), config.node("type").getString("ui.button.click"),
                config.node("volume").getDouble(1.0),
                config.node("pitch").getFloat(1.0f), source);
    }

    public void playSound(@NotNull final Player player, @NotNull final Location location) {
        if (!this.isEnabled || this.sound == null) return;

        player.playSound(this.sound, location.x(), location.y(), location.z());
    }

    public void playSound(@NotNull final Player player) {
        playSound(player, player.getLocation());
    }

    public void stopSound(@NotNull final Player player) {
        player.stopSound(this.sound);
    }
}