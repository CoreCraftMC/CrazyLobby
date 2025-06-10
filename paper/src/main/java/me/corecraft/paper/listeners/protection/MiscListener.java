package me.corecraft.paper.listeners.protection;

import me.corecraft.common.enums.Files;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class MiscListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        final CommentedConfigurationNode config = Files.config.getConfig();

        if (!config.node("root", "protection", "prevent-weather-change").getBoolean(true)) return;

        event.setCancelled(event.toWeatherState());
    }
}