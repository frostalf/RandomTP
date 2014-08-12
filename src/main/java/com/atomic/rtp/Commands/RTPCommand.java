package com.atomic.rtp.Commands;

import com.atomic.rtp.Enums.Value;
import com.atomic.rtp.Handlers.*;
import com.atomic.rtp.RandomTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Dante Pasionek created: com.atomic.rtp.Commands on Jun. 04, 2014 *
 */
public class RTPCommand implements CommandExecutor {

    RandomTeleport plugin = RandomTeleport.getInstance();
    MessageHandler mh;
    ConfigHandler ch;

    public RTPCommand() {
        mh = new MessageHandler();
        ch = new ConfigHandler();

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            mh.error(sender, "ERROR: A player is expected!", Level.WARNING);
            return true;
        }

        Player player = (Player) sender;

        /*
         * Command = /randomtp
         */

        if(!(player.hasPermission("randomtp.tp"))) {
            mh.error(player, "Insufficient permissions!", "Permissions Needed: randomtp.tp");
            return true;
        }

        if(args.length > 0) {
            mh.error(player, "Invalid Command Syntax!", "Usage: /randomtp");
            return true;
        }

        if((Boolean) ch.get(Value.COOLDOWN_ENABLED)) {
            if(CooldownHandler.areTherePlayersInTheMap() == false) {
                CooldownHandler cooldown = CooldownHandler.getCooldown(player);
                if(!cooldown.check(player) && (cooldown.getTimeLeft(player) * -1) >= 1) {
                    mh.error(player, "You can not teleport yet!", "Please wait " + (cooldown.getTimeLeft(player) * -1) + " seconds!");
                    return true;
                } else if ((cooldown.getTimeLeft(player) * -1) == 0) {
                    mh.message(player, "You can teleport again!", ChatColor.GREEN);
                    cooldown.finalize();
                } else {
                    mh.message(player, "You can teleport again!", ChatColor.GREEN);
                    cooldown.finalize();
                }
            }
        }

        if((Boolean) ch.get(Value.COST_ENABLED)) {
            double cost = (Double) ch.get(Value.COST);
            VaultHandler ico = new VaultHandler(plugin, player);

            if(ico.check(cost)) {
                ico.remove((Double) ch.get(Value.COST));
                player.sendMessage(ico.getMoneyMessage());
            } else {
                mh.error(player, "Insufficient Funds!", "You need at least " + cost + " to teleport!");
                return true;
            }
        }

        TeleportHandler tp = new TeleportHandler(player, Bukkit.getWorld((String) ch.get(Value.WORLD)),  (Integer) ch.get(Value.MAX_X), (Integer) ch.get(Value.MAX_Z));
        tp.teleport();
        player.sendMessage(tp.getMessage());


        if((Boolean) ch.get(Value.COOLDOWN_ENABLED)) {
            CooldownHandler cooldown = new CooldownHandler(plugin, player, (Integer) ch.get(Value.COOLDOWN_TIME));
            cooldown.start();
        }
        return true;
    }
}
