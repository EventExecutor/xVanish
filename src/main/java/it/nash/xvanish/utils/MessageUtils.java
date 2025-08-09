package it.nash.xvanish.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageUtils {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize(message));
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(colorize(message));
    }

    public static void broadcast(String message, String permission) {
        String colorizedMessage = colorize(message);
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(colorizedMessage));
    }
}