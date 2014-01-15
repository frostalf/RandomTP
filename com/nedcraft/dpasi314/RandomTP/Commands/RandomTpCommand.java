package com.nedcraft.dpasi314.RandomTP.Commands;

import com.nedcraft.dpasi314.RandomTP.Handlers.ConfigurationHandler;
import com.nedcraft.dpasi314.RandomTP.Handlers.MapHandler;
import com.nedcraft.dpasi314.RandomTP.Handlers.TeleportHandler;
import com.nedcraft.dpasi314.RandomTP.RandomTP;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RandomTpCommand implements CommandExecutor {
    RandomTP plugin;

    ConfigurationHandler ch;

    public RandomTpCommand(RandomTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,
                             String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "ERROR: A player is expected.");
            return true;
        }

        if (!player.hasPermission("randomtp.teleport")) {
            player.sendMessage(ChatColor.RED + "ERROR: You have insufficient permissions!");
            return true;
        }

        if (args.length > 2 || args.length < 0) {
            player.sendMessage(ChatColor.RED + "ERROR: Invalid Command Syntax!");
            player.sendMessage(ChatColor.RED + "Usage: /randomtp [reload]");
            return true;
        }

        if (args.length == 1 && (args[0].equalsIgnoreCase("saveme") || args[0].equalsIgnoreCase("s"))) {
            TeleportHandler.saveMe(player);
            return true;
        }

        if((args.length == 2 && args[0].equalsIgnoreCase("remove") && player.hasPermission("randomtp.admin")))
        {
            MapHandler.removePlayerFromMap(args[1]);
            player.sendMessage(ChatColor.DARK_AQUA + "The player '" + ChatColor.AQUA + args[1] + ChatColor.DARK_AQUA + "' is allowed to teleport again!");
            return true;
        }

        if((plugin.getConfig().getBoolean("RandomTP.Teleport.OnlyOnce") == true) && (MapHandler.canPlayerTeleport(player.getDisplayName()) == false))
        {
            player.sendMessage(ChatColor.RED + "ERROR: Sorry, you are only allowed to teleport once!");
            return true;
        }


        if (TeleportHandler.isAntiCheat() == true && TeleportHandler.checkForCheating(player) == true) {
            player.sendMessage(ChatColor.RED + "ERROR: You can teleport from this location.");
            player.sendMessage(ChatColor.RED + "Be sure you're not in water, in lava, or jumping!");
            return true;
        }

		/*if(TeleportHandler.isHealthOK(player) == false && TeleportHandler.isAntiCheat() == true){
			player.sendMessage(ChatColor.RED + "ERROR: Your health is too low to be able to teleport!");
			player.sendMessage(ChatColor.RED + "Your health needs to be over: " + ChatColor.AQUA + TeleportHandler.minHealth());
			return true;
		}*/

        if (TeleportHandler.isCoolDownEnabled() == true && TeleportHandler.isCoolDownFinished(player) == false) {
            player.sendMessage(ChatColor.RED + TeleportHandler.getCoolDownLeft(player));
            return true;
        }

        if (TeleportHandler.isCoolDownEnabled() == true) {
            TeleportHandler.setCoolDown(player);
        }


        if (TeleportHandler.isPurchaseable() == true) {
            TeleportHandler.removeCost(player);
        }

        TeleportHandler.sendTeleport(player);

        if(plugin.getConfig().getBoolean("RandomTP.Teleport.OnlyOnce") == true)
        {
            MapHandler.addPlayerToMap(player.getDisplayName());
        }

		if(TeleportHandler.isTeleportOK == false)
        {
            MapHandler.removePlayerFromMap(player.getDisplayName());
        }
		/*if(args[0].equalsIgnoreCase("reload") && player.hasPermission("randomtp.reload"))
		{
			ch.reloadConfiguration();
		}
		
		if(args[0].equalsIgnoreCase("creafteconfig") && player.hasPermission("randomtp.createconfig"))
		{
			ch.loadConfiguration();
		}*/

        return true;
        }
    }


