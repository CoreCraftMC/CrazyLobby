package me.corecraft.paper.api.objects.menus;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import me.corecraft.paper.api.objects.items.CustomSound;
import me.corecraft.paper.utils.ItemUtils;
import me.corecraft.paper.utils.MiscUtils;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.ArrayList;
import java.util.List;

public class CustomButton {

    private final List<String> commands = new ArrayList<>();
    private final List<String> messages = new ArrayList<>();

    private final ItemBuilder itemBuilder;

    private final CustomSound customSound;

    private final boolean isVelocity;
    private final String server;

    private final int slot;

    public CustomButton(final CommentedConfigurationNode child) {
        this.itemBuilder = ItemUtils.getBuilder(child);

        this.slot = child.node("slot").getInt(-1);

        this.isVelocity = child.node("velocity", "enabled").getBoolean(false);
        this.server = child.node("velocity", "server").getString("");

        try {
            final List<String> commands = child.node("commands").getList(String.class);

            if (commands != null) {
                this.commands.addAll(commands);
            }
        } catch (final SerializationException exception) {
            throw new FusionException("Failed to serialize " + child.node("commands").path(), exception);
        }

        try {
            final List<String> messages = child.node("messages").getList(String.class);

            if (messages != null) {
                this.messages.addAll(messages);
            }
        } catch (SerializationException exception) {
            throw new FusionException("Failed to serialize " + child.node("messages").path(), exception);
        }

        this.customSound = new CustomSound(child.node("sound"), Sound.Source.MASTER);
    }

    public final ItemBuilder getItemBuilder() {
        return this.itemBuilder;
    }

    public void sendServer(final Player player) {
        MiscUtils.sendServer(player, this.isVelocity, this.server);
    }

    public void executeSound(final Player player) {
        if (this.customSound == null) return;

        this.customSound.playSound(player);
    }

    public final List<String> getCommands() {
        return this.commands;
    }

    public final List<String> getMessages() {
        return this.messages;
    }

    public final int getSlot() {
        return this.slot;
    }
}