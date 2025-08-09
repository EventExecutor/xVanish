package it.nash.xvanish.managers;

import it.nash.xvanish.XVanish;
import it.nash.xvanish.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class VanishManager {

    private final Set<UUID> vanishedPlayers;
    private final Map<UUID, PlayerState> savedStates;
    private final XVanish plugin;

    public VanishManager(XVanish plugin) {
        this.plugin = plugin;
        this.vanishedPlayers = new HashSet<>();
        this.savedStates = new HashMap<>();
    }

    public void toggleVanish(Player player) {
        if (isVanished(player)) {
            removeVanish(player);
        } else {
            addVanish(player);
        }
    }

    public void addVanish(Player player) {
        UUID playerUUID = player.getUniqueId();
        savePlayerState(player);
        vanishedPlayers.add(playerUUID);
        applyVanishEffects(player);
        hidePlayerFromOthers(player);
        MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("vanish_enabled"));
        MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("vanish_effects_enabled"));
        notifyStaff(player, true);
    }

    public void removeVanish(Player player) {
        UUID playerUUID = player.getUniqueId();
        vanishedPlayers.remove(playerUUID);
        removeVanishEffects(player);
        restorePlayerState(player);
        showPlayerToOthers(player);
        MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("vanish_disabled"));
        notifyStaff(player, false);
    }

    private void savePlayerState(Player player) {
        PlayerState state = new PlayerState();
        state.setAllowFlight(player.getAllowFlight());
        state.setFlying(player.isFlying());
        state.setWalkSpeed(player.getWalkSpeed());
        state.setFlySpeed(player.getFlySpeed());
        savedStates.put(player.getUniqueId(), state);
    }

    private void applyVanishEffects(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setWalkSpeed(0.4f);
        player.setFlySpeed(0.2f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
    }

    private void removeVanishEffects(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    private void restorePlayerState(Player player) {
        PlayerState state = savedStates.get(player.getUniqueId());
        if (state != null) {
            player.setAllowFlight(state.isAllowFlight());
            player.setFlying(state.isFlying());
            player.setWalkSpeed(state.getWalkSpeed());
            player.setFlySpeed(state.getFlySpeed());
            savedStates.remove(player.getUniqueId());
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setWalkSpeed(0.2f);
            player.setFlySpeed(0.1f);
        }
    }

    private void hidePlayerFromOthers(Player vanishedPlayer) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission(plugin.getConfigManager().getPermission("vanish_see")) && !onlinePlayer.equals(vanishedPlayer)) {
                onlinePlayer.hidePlayer(vanishedPlayer);
                if (plugin.getConfigManager().isEventEnabled("hide_from_tablist")) {
                    onlinePlayer.hidePlayer(vanishedPlayer);
                }
            }
        }
    }

    private void showPlayerToOthers(Player unvanishedPlayer) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(unvanishedPlayer)) {
                onlinePlayer.showPlayer(unvanishedPlayer);
                if (plugin.getConfigManager().isEventEnabled("hide_from_tablist")) {
                    onlinePlayer.showPlayer(unvanishedPlayer);
                }
            }
        }
    }

    private void notifyStaff(Player player, boolean vanished) {
        String message = vanished ? plugin.getConfigManager().getMessage("staff_notify_enter") : plugin.getConfigManager().getMessage("staff_notify_exit");
        message = message.replace("{player}", player.getName());
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission(plugin.getConfigManager().getPermission("vanish_see")) && !staff.equals(player)) {
                MessageUtils.sendMessage(staff, message);
            }
        }
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }

    public boolean isVanished(UUID playerUUID) {
        return vanishedPlayers.contains(playerUUID);
    }

    public void handlePlayerJoin(Player player) {
        if (!player.hasPermission(plugin.getConfigManager().getPermission("vanish_see"))) {
            for (UUID vanishedUUID : vanishedPlayers) {
                Player vanishedPlayer = Bukkit.getPlayer(vanishedUUID);
                if (vanishedPlayer != null && vanishedPlayer.isOnline()) {
                    player.hidePlayer(vanishedPlayer);
                    if (plugin.getConfigManager().isEventEnabled("hide_from_tablist")) {
                        player.hidePlayer(vanishedPlayer);
                    }
                }
            }
        } else {
            int vanishedCount = vanishedPlayers.size();
            if (vanishedCount > 0) {
                MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("staff_vanished_count").replace("{count}", String.valueOf(vanishedCount)));
            }
        }
    }

    public void handlePlayerQuit(Player player) {
        if (isVanished(player)) {
            savedStates.remove(player.getUniqueId());
        }
        vanishedPlayers.remove(player.getUniqueId());
    }

    public void disableCleanup() {
        for (UUID uuid : vanishedPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                removeVanishEffects(player);
                restorePlayerState(player);
                showPlayerToOthers(player);
            }
        }
        vanishedPlayers.clear();
        savedStates.clear();
    }

    public int getVanishedCount() {
        return vanishedPlayers.size();
    }
}