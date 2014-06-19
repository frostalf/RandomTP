package com.atomic.rtp.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Dante Pasionek created: com.atomic.rtp.Handlers on Jun. 04, 2014 *
 */
public class MessageHandler {

    public void message(Player player, String message, ChatColor color) {
        player.sendMessage(color + message);
    }

    public void message(Player player, String message) {
        player.sendMessage(message);
    }

    public void error(Player player, String error, String usage) {
        player.sendMessage(ChatColor.RED + "ERROR: " + error + "\n" + usage);
    }

    public void error(CommandSender sender, String error, Level level) {
        String prefix = "[" + level + "]" + "[RandomTP] ";
        sender.sendMessage(prefix + error);
    }
}
