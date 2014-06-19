package rtp.Commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rtp.Enums.Value;
import rtp.Handlers.ConfigHandler;
import rtp.Handlers.MessageHandler;
import rtp.Handlers.TeleportHandler;
import rtp.Handlers.iConomyHandler;
import rtp.RandomTeleport;

import java.util.logging.Level;

/**
 * Dante Pasionek created: com.atomic.rtp.Commands on Jun. 04, 2014 *
 */
public class RTPCommand implements CommandExecutor {

    RandomTeleport plugin;
    MessageHandler mh;
    ConfigHandler ch;

    public RTPCommand(RandomTeleport plugin) {
        this.plugin = plugin;
        mh = new MessageHandler();
        ch = new ConfigHandler(plugin);

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

        TeleportHandler tp = new TeleportHandler(player, (World)ch.get(Value.WORLD),  (Double) ch.get(Value.MAX_X), (Double) ch.get(Value.MAX_Z));
        tp.teleport();
        player.sendMessage(tp.getMessage());

        if((Boolean) ch.get(Value.COST_ENABLED)) {
            double cost = (Double) ch.get(Value.COST);
            iConomyHandler ico = new iConomyHandler(plugin, player);

            if(ico.check(cost)) {
                ico.remove((Double) ch.get(Value.COST));
                player.sendMessage(ico.getMoneyMessage());
            } else {
                mh.error(player, "Insufficient Funds!", "You need at least " + cost + " to teleport!");
                return true;
            }
        }

        if((Boolean) ch.get(Value.TELEPORT_ONCE)) {
            if(tp.alreadyTeleported() == false)
                tp.addToList();
            else {
                mh.error(player, "You've already teleported!", "Players are only allowed to teleport ONCE!");
                return true;
            }
        }
        return true;
    }
}
