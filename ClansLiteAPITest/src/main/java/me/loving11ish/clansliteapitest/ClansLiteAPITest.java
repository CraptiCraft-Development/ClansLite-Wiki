package me.loving11ish.clansliteapitest;

import com.tcoded.folialib.FoliaLib;
import me.loving11ish.clans.api.ClansLiteAPI;
import me.loving11ish.clansliteapitest.commands.Test;
import me.loving11ish.clansliteapitest.listeners.PlayerConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClansLiteAPITest extends JavaPlugin {

    private static ClansLiteAPITest plugin;
    private static FoliaLib foliaLib;

    // ClansLiteAPI - Always null check before using! This must always be null at the start of the plugin!
    private ClansLiteAPI clansLiteAPI = null;

    @Override
    public void onLoad() {
        // Plugin startup logic
        plugin = this;
        foliaLib = new FoliaLib(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Load ClansLiteAPI
        // ClansLiteAPI is a soft-dependency, so we need to check if it's loaded
        // ClansLiteAPI uses a service provider, so we need to check if it's registered by the ClansLite plugin itself
        // If it's not registered, we need to disable the plugins ability to run any ClansLiteAPI code or errors will occur!
        if (!Bukkit.getPluginManager().isPluginEnabled("ClansLite")) {
            getLogger().severe("ClansLite not found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<ClansLiteAPI> rsp = Bukkit.getServicesManager().getRegistration(ClansLiteAPI.class);
        if (rsp != null) {
            clansLiteAPI = rsp.getProvider();
        } else {
            getLogger().severe("ClansLiteAPI not found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register commands
        this.getCommand("test").setExecutor(new Test());

        // Register events
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);

        // Final startup message
        getLogger().info("ClansLiteAPITest has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Unregister listeners
        HandlerList.unregisterAll(this);

        // Final shutdown message
        getLogger().info("ClansLiteAPITest has been disabled!");

        // Nullify plugin
        plugin = null;
        foliaLib = null;
        clansLiteAPI = null;
    }

    public static ClansLiteAPITest getPlugin() {
        return plugin;
    }

    public static FoliaLib getFoliaLib() {
        return foliaLib;
    }

    public ClansLiteAPI getClansLiteAPI() {
        return clansLiteAPI;
    }
}
