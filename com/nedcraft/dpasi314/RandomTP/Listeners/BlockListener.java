package com.nedcraft.dpasi314.RandomTP.Listeners;

import com.nedcraft.dpasi314.RandomTP.Handlers.TeleportHandler;
import com.nedcraft.dpasi314.RandomTP.RandomTP;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

    RandomTP plugin;

    public BlockListener(RandomTP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)


    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!e.isCancelled() && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();

            Block block = e.getClickedBlock();

            if (block.getType().equals(Material.STONE_BUTTON) || block.getType().equals(Material.WOOD_BUTTON)) {
                Sign sign = null;

                Block sign1 = block.getRelative(2, 0, 0);
                Block sign2 = block.getRelative(-2, 0, 0);
                Block sign3 = block.getRelative(0, 0, 2);
                Block sign4 = block.getRelative(0, 0, -2);

                Block[] signs = {sign1, sign2, sign3, sign4};

                for (Block s : signs) {
                    if (s.getState() instanceof Sign)
                        sign = (Sign) s.getState();
                }

                try {
                    if (sign.getLine(1).equalsIgnoreCase("[RandomTP]")) {

                        if(!p.hasPermission("randomtp.use.signs"))
                        {
                            p.sendMessage(ChatColor.RED + "ERROR: You don't have the permission '" + ChatColor.DARK_RED + "randomtp.use.signs" + ChatColor.RED + "'");
                            return;
                        }

                        if (TeleportHandler.isCoolDownEnabled() == true && TeleportHandler.isCoolDownFinished(p) == false) {
                            p.sendMessage(ChatColor.RED + TeleportHandler.getCoolDownLeft(p));
                            return;
                        }

                        if (TeleportHandler.isCoolDownEnabled() == true) {

                            TeleportHandler.setCoolDown(p);
                        }

                        if (TeleportHandler.isPurchaseable() == true) {
                            TeleportHandler.removeCost(p);
                        }

                        TeleportHandler.sendTeleport(p);
                    }

                } catch (NullPointerException err) { /* I should probably do something here */ }
            }
        }
    }
}
