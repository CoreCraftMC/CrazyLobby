package me.corecraft.paper;

import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazyLobbyPlugin extends JavaPlugin {

    public static CrazyLobbyPlugin getPlugin() {
        return JavaPlugin.getPlugin(CrazyLobbyPlugin.class);
    }

    private CrazyLobbyPlatform platform;

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord"); // register bungee channel

        this.platform = new CrazyLobbyPlatform(this, new FusionPaper(getComponentLogger(), getDataPath()));
        this.platform.start();
    }

    @Override
    public void onDisable() {
        this.platform.stop();
    }
}