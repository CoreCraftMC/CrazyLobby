package me.corecraft.paper.api;

import me.corecraft.api.interfaces.IUserManager;
import me.corecraft.paper.api.objects.PaperUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaperUserManager implements IUserManager<Player> {

    private final Map<UUID, PaperUser> users = new HashMap<>();

    @Override
    public void addUser(@NotNull final Player player) {
        final PaperUser user = new PaperUser(player);

        user.setLocale(player.locale());

        this.users.put(player.getUniqueId(), user);
    }

    @Override
    public void removeUser(@NotNull final Player player) {
        this.users.remove(player.getUniqueId());
    }

    @Override
    public @NotNull PaperUser getUser(@NotNull final UUID uuid) {
        return this.users.get(uuid);
    }
}