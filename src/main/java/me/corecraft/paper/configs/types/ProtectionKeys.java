package me.corecraft.paper.configs.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import me.corecraft.paper.configs.objects.SoundProperty;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ProtectionKeys implements SettingsHolder {

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        conf.setComment("protection", "Prevents players from wreaking havoc in your lobby!");

        conf.setComment("protection.block", "Block interaction protections");

        conf.setComment("protection.block.prevent-interaction", "Section related to prevent block interactions.");
    }

    @Comment({
            "A list of sounds: https://minecraft.wiki/w/Sounds.json",
            "",
            "Handles the sound to play, when a player cannot build, volume/pitch control, and Custom Sounds are supported!"
    })
    public static final Property<SoundProperty> event_prevent_interact_sound = newBeanProperty(SoundProperty.class, "root.protection.block.prevent-interaction.sound", new SoundProperty());

    @Comment("A list of blocks that can be interacted with.")
    public static final Property<List<String>> event_interactable_items = newListProperty("root.protection.block.interactable-items", List.of(
            "oak_door",
            "acacia_door",
            "birch_door",
            "dark_oak_door",
            "spruce_door",
            "jungle_door",

            "oak_fence_gate",
            "acacia_fence_gate",
            "birch_fence_gate",
            "dark_oak_fence_gate",
            "spruce_fence_gate",
            "jungle_fence_gate",

            "oak_trapdoor",
            "acacia_trapdoor",
            "birch_trapdoor",
            "dark_oak_trapdoor",
            "spruce_trapdoor",
            "jungle_trapdoor",

            "oak_sign",
            "acacia_sign",
            "birch_sign",
            "dark_oak_sign",
            "spruce_sign",
            "jungle_sign",

            "oak_button",
            "acacia_button",
            "birch_button",
            "dark_oak_button",
            "jungle_button",

            "anvil",
            "beacon",

            "red_bed",
            "brewing_stand",
            "daylight_detector",
            "dispenser",
            "hopper",
            "dropper",

            "enchanting_table",
            "furnace",

            "chest_minecart",
            "hopper_minecart",
            "minecart",

            "lever",
            "note_block",
            "comparator",

            "chest",
            "ender_chest",
            "trapped_chest"
    ));

    @Comment("Prevents a player from building, if this is set to true.")
    public static final Property<Boolean> event_prevent_block_interact = newProperty("root.protection.block.prevent-interaction.toggle", true);

    @Comment("Prevents a player from picking up items, if this is set to true.")
    public static final Property<Boolean> event_prevent_item_pickup = newProperty("root.protection.item.prevent-pickup", true);

    @Comment("Prevents a player from loosing hunger, if this is set to true.")
    public static final Property<Boolean> event_prevent_hunger_change = newProperty("root.protection.prevent-hunger-change", true);

    @Comment("Prevents a player from dropping items, if this is set to true.")
    public static final Property<Boolean> event_prevent_item_drop = newProperty("root.protection.item.prevent-drop", true);

    @Comment("Prevent leaf decay, if this is set to true.")
    public static final Property<Boolean> event_prevent_leaf_decay = newProperty("root.protection.block.prevent-leaf-decay", true);

    @Comment("Prevents fire from spreading, if this is set to true.")
    public static final Property<Boolean> event_prevent_fire_spread = newProperty("root.protection.block.prevent-fire-spread", true);

    @Comment("Prevents blocks from burning, if this is set to true.")
    public static final Property<Boolean> event_prevent_block_burn = newProperty("root.protection.block.prevent-block-burn", true);

    @Comment("Prevents mobs from spawning, if this is set to true.")
    public static final Property<Boolean> event_prevent_mob_spawning = newProperty("root.protection.block.prevent-mob-spawning", true);

    @Comment("Prevents a death mess if a player dies, if this is set to true.")
    public static final Property<Boolean> event_prevent_death_message = newProperty("root.protection.prevent-death-message", true);

    @Comment("Prevents void damage, if this is set to true.")
    public static final Property<Boolean> event_prevent_void_damage = newProperty("root.protection.prevent-void-damage", true);

    @Comment("Prevents all damage, if this is set to true.")
    public static final Property<Boolean> event_prevent_all_damage = newProperty("root.protection.prevent-all-damage", false);

    @Comment("Prevents weather changing, if this is set to true.")
    public static final Property<Boolean> event_prevent_weather_change = newProperty("root.protection.prevent-weather-change", true);

}