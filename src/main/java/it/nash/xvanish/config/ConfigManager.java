package it.nash.xvanish.config;

import it.nash.xvanish.XVanish;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private final XVanish plugin;
    private File configFile;
    private FileConfiguration config;

    public ConfigManager(XVanish plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public String getMessage(String key) {
        return config.getString("messages." + key, "&cMessage not found: " + key);
    }

    public String getPermission(String key) {
        return config.getString("permissions." + key, "xvanish." + key);
    }

    public boolean isEventEnabled(String event) {
        return config.getBoolean("events." + event, true);
    }
}