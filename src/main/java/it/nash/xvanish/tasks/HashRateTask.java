package it.nash.xvanish.tasks;

import it.nash.xvanish.XVanish;
import it.nash.xvanish.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HashRateTask extends BukkitRunnable {

    private final XVanish plugin;
    private final Map<UUID, Boolean> previousVanishStates;

    public HashRateTask(XVanish plugin) {
        this.plugin = plugin;
        this.previousVanishStates = new HashMap<>();
    }

    @Override
    public void run() {
        Map<UUID, Boolean> currentVanishStates = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerUUID = player.getUniqueId();
            boolean isCurrentlyVanished = plugin.getVanishManager().isVanished(player);
            currentVanishStates.put(playerUUID, isCurrentlyVanished);
            if (isCurrentlyVanished && !previousVanishStates.getOrDefault(playerUUID, false)) {
                notifyNewVanishPlayer(player);
            }
        }
        previousVanishStates.entrySet().removeIf(entry -> Bukkit.getPlayer(entry.getKey()) == null);
        previousVanishStates.putAll(currentVanishStates);
    }

    private void notifyNewVanishPlayer(Player player) {
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission(plugin.getConfigManager().getPermission("vanish_see")) && !staff.equals(player)) {
                MessageUtils.sendMessage(staff, plugin.getConfigManager().getMessage("staff_notify_enter").replace("{player}", player.getName()));
            }
        }
    }
}