package me.corecraft.api.interfaces;

import me.corecraft.api.objects.User;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public interface IUserManager<P> {

    void addUser(@NotNull final P user);

    void removeUser(@NotNull final P user);

    @NotNull User getUser(@NotNull final UUID uuid);

}