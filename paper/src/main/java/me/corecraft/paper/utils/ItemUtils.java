package me.corecraft.paper.utils;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.SkullBuilder;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static ItemBuilder getBuilder(@NotNull final CommentedConfigurationNode child) {
        final ItemBuilder itemBuilder = ItemBuilder.from(child.node("type").getString("stone"));

        final String itemName = child.node("name").getString("");

        final List<String> lore = new ArrayList<>();

        try {
            final List<String> itemLore = child.node("lore").getList(String.class);

            if (itemLore != null) {
                lore.addAll(itemLore);
            }
        } catch (final SerializationException ignored) {}

        itemBuilder.setDisplayName(itemName).withDisplayLore(lore).setAmount(child.node("amount").getInt(1));

        if (child.hasChild("settings", "item-model")) {
            final String namespace = child.node("settings", "item-model", "namespace").getString("");
            final String id = child.node("settings", "item-model", "id").getString("");

            itemBuilder.setItemModel(namespace, id);
        }

        if (child.hasChild("settings", "skull") && itemBuilder.isPlayerHead()) {
            final SkullBuilder builder = itemBuilder.asSkullBuilder();

            if (child.hasChild("settings", "skull", "hdb")) {
                builder.withSkull(child.node("settings", "skull", "hdb").getString(""));
            }

            if (child.hasChild("settings", "skull", "player")) {
                builder.withName(child.node("settings", "skull", "player").getString(""));
            }

            if (child.hasChild("settings", "skull", "texture")) {
                builder.withUrl(child.node("settings", "skull", "texture").getString(""));
            }

            builder.build();
        }

        child.node("settings", "enchantments").childrenMap().keySet().stream().map(Object::toString).forEach(enchantment -> itemBuilder.addEnchantment(enchantment, child.node("settings", "enchantments", enchantment).getInt(1)));

        itemBuilder.setEnchantGlint(child.node("settings", "glowing").getBoolean(false));

        itemBuilder.setTrim(child.node("settings", "trim", "pattern").getString(""), child.node("settings", "trim", "material").getString(""));

        itemBuilder.setItemDamage(child.node("settings", "item-damage").getInt(-1));

        if (child.node("settings", "hide-tool-tip").getBoolean(false)) {
            itemBuilder.hideToolTip();
        }

        try {
            final CommentedConfigurationNode node = child.node("settings", "hidden-components");

            final List<String> components = node.getList(String.class);

            if (components != null) {
                itemBuilder.hideComponents(components);
            }
        } catch (SerializationException exception) {
            throw new FusionException("Failed to serialize " + child.node("hidden-components").path(), exception);
        }

        return itemBuilder;
    }
}