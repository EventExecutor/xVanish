package it.nash.xvanish.commands;

import it.nash.xvanish.XVanish;
import it.nash.xvanish.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private final XVanish plugin;

    public VanishCommand(XVanish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!isValidCommand(command)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, plugin.getConfigManager().getMessage("not_player"));
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(plugin.getConfigManager().getPermission("vanish_use"))) {
            MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("no_permission"));
            return true;
        }
        switch (args.length) {
            case 0:
                plugin.getVanishManager().toggleVanish(player);
                break;
            case 1:
                if (args[0].equalsIgnoreCase("list")) {
                    handleListCommand(player);
                } else {
                    sendUsage(player);
                }
                break;
            default:
                sendUsage(player);
                break;
        }
        return true;
    }

    private void handleListCommand(Player player) {
        if (!player.hasPermission(plugin.getConfigManager().getPermission("vanish_list"))) {
            MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("no_permission"));
            return;
        }
        StringBuilder vanishedList = new StringBuilder();
        int count = 0;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (plugin.getVanishManager().isVanished(onlinePlayer)) {
                if (count > 0) {
                    vanishedList.append("&7, ");
                }
                vanishedList.append("&e").append(onlinePlayer.getName());
                count++;
            }
        }
        String message = count == 0 ? plugin.getConfigManager().getMessage("vanish_list_none") :
                plugin.getConfigManager().getMessage("vanish_list_header").replace("{count}", String.valueOf(count)) + vanishedList.toString();
        MessageUtils.sendMessage(player, message);
    }

    private void sendUsage(Player player) {
        MessageUtils.sendMessage(player, "&7&m----------&r &8(&b&lXVANISH&8) Help &7&m----------");
        MessageUtils.sendMessage(player, "&e/vanish &7- Attiva/disattiva vanish");
        MessageUtils.sendMessage(player, "&e/vanish list &7- Mostra player vanishati");
        MessageUtils.sendMessage(player, "&7&m------------------------------------");
    }

    private boolean isValidCommand(Command command) {
        String cmdName = command.getName().toLowerCase();
        return cmdName.equals("vanish") || cmdName.equals("v");
    }
}