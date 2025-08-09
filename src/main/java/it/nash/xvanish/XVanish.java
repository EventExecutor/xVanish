package it.nash.xvanish;

import it.nash.xvanish.commands.VanishCommand;
import it.nash.xvanish.config.ConfigManager;
import it.nash.xvanish.listeners.PlayerListener;
import it.nash.xvanish.managers.VanishManager;
import it.nash.xvanish.tasks.HashRateTask;
import it.nash.xvanish.utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class XVanish extends JavaPlugin {

    private static XVanish instance;
    private VanishManager vanishManager;
    private HashRateTask hashRateTask;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        vanishManager = new VanishManager(this);
        startTasks();
        registerCommands();
        registerListeners();
        MessageUtils.log(configManager.getMessage("plugin_enabled"));
        MessageUtils.log("&aCreated by EventExecutor");
    }

    @Override
    public void onDisable() {
        if (hashRateTask != null) {
            hashRateTask.cancel();
        }
        if (vanishManager != null) {
            vanishManager.disableCleanup();
        }
        MessageUtils.log(configManager.getMessage("plugin_disabled"));
    }

    private void startTasks() {
        hashRateTask = new HashRateTask(this);
        hashRateTask.runTaskTimer(this, 20L, 20L);
    }

    private void registerCommands() {
        getCommand("vanish").setExecutor(new VanishCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public static XVanish getInstance() {
        return instance;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}