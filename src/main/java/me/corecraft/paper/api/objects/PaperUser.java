package me.corecraft.paper.api.objects;

import ch.jalu.configme.SettingsManager;
import me.corecraft.api.objects.User;
import me.corecraft.paper.configs.ConfigManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PaperUser extends User {

    public PaperUser(@NotNull final Player player) {
        super(player);
    }

    public final List<String> activeBypassTypes = new ArrayList<>();

    public boolean isCombatEnabled = false;

    @Override
    public @NotNull final SettingsManager locale() {
        return ConfigManager.getLocale(getLocale());
    }
}