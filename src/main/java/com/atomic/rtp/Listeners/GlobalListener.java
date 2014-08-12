package com.atomic.rtp.Listeners;

import com.atomic.rtp.Enums.Value;
import com.atomic.rtp.Handlers.*;
import com.atomic.rtp.RandomTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Sign;

/**
 * Dante Pasionek created: com.atomic.rtp.Listeners on Jun. 06, 2014 *
 */
public class GlobalListener implements Listener {

    RandomTeleport plugin;
    MessageHandler mh;

    public GlobalListener(RandomTeleport plugin) {
        this.plugin = plugin;
        mh = new MessageHandler();
    }

    @EventHandler(priority = EventPriority.NORMAL)

    public void onSignCreate(SignChangeEvent e) {
        Block b = e.getBlock();
        Material blockType = b.getType();
        Sign sign = (Sign) b.getState();

        if(blockType.equals(Material.SIGN) || blockType.equals(Material.SIGN_POST) || blockType.equals(Material.WALL_SIGN)) {
            if(!e.getLine(0).equalsIgnoreCase("[RandomTP]")) {
                return;
            }

            if(!e.getPlayer().hasPermission("randomtp.create.signs")) {
                if(e.getLine(0).equalsIgnoreCase("[RandomTP]")) {
                    mh.error(e.getPlayer(), "Insufficient Permissions!", "Permission Needed: randomtp.create.signs");
                    e.setCancelled(true);
                    e.getBlock().breakNaturally();
                    return;
                }
            }

            if(e.getLine(1) == null || e.getLine(1).equals("")) {
                mh.error(e.getPlayer(), "Improper Setup!", "Line 1: [RandomTP] \nLine 2: World name (or Default)");
                e.setCancelled(true);
                e.getBlock().breakNaturally();
                return;
            }

            if(Bukkit.getWorld(e.getLine(1)) == null && !e.getLine(1).equalsIgnoreCase("default")) {
                mh.error(e.getPlayer(), "Invalid world!", "The world \"" + e.getLine(1) + "\" doesn't exist!");
                e.getPlayer().sendMessage(ChatColor.RED + "Make sure that you spelled the name exactly as on the world folder!");
                e.setCancelled(true);
                e.getBlock().breakNaturally();
                return;
            }

        }

        e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Successfully created RandomTP Sign! \nThis sign will teleport to " + ChatColor.AQUA + e.getLine(1) + ChatColor.DARK_AQUA + "!");
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(!e.isCancelled() && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();

            Block block = e.getClickedBlock();
            Material blockType = block.getType();

            if(blockType.equals(Material.SIGN) || blockType.equals(Material.SIGN_POST) || blockType.equals(Material.WALL_SIGN)) {
                Sign sign = (Sign) block.getState();

                String line = sign.getLine(0);
                if(line.equalsIgnoreCase("[RandomTP]")) {
                    if(!p.hasPermission("randomtp.use.signs")) {
                        mh.error(p, "Insufficient Permissions", "Permission Needed: randomtp.use.signs");
                        return;
                    }


                    ConfigHandler ch = new ConfigHandler(plugin);

                    if((Boolean) ch.get(Value.COOLDOWN_ENABLED)) {
                        if(CooldownHandler.areTherePlayersInTheMap() == false) {
                            CooldownHandler cooldown = CooldownHandler.getCooldown(p);
                            if(!cooldown.check(p) && (cooldown.getTimeLeft(p) * -1) >= 1) {
                                mh.error(p, "You can not teleport yet!", "Please wait " + (cooldown.getTimeLeft(p) * -1) + " seconds!");
                                return;
                            } else if ((cooldown.getTimeLeft(p) * -1) == 0) {
                                mh.message(p, "You can teleport again!", ChatColor.GREEN);
                                cooldown.finalize();
                            } else {
                                mh.message(p, "You can teleport again!", ChatColor.GREEN);
                                cooldown.finalize();
                            }
                        }
                    }

                    World w = e.getClickedBlock().getWorld();
                    if((sign.getLine(1).equalsIgnoreCase("default"))) {
                        w = Bukkit.getWorld((String) ch.get(Value.WORLD));
                    }

                    if(((sign.getLine(1) != null) || (sign.getLine(1) == "")) && !sign.getLine(1).equalsIgnoreCase("default")) {
                        w = Bukkit.getWorld(sign.getLine(1));
                    }

                    if(sign.getLine(1) == null || sign.getLine(1) == "") {
                        w = p.getWorld();
                    }

                    TeleportHandler th = new TeleportHandler(p, w, (Integer) ch.get(Value.MAX_X), (Integer) ch.get(Value.MAX_Z));
                    th.teleport();
                    p.sendMessage(th.getMessage());

                    if((Boolean) ch.get(Value.COST_ENABLED)) {
                        double cost = (Double) ch.get(Value.COST);
                        VaultHandler ico = new VaultHandler(plugin, p);

                        if(ico.check(cost)) {
                            ico.remove((Double) ch.get(Value.COST));
                            p.sendMessage(ico.getMoneyMessage());
                        } else {
                            mh.error(p, "Insufficient Funds!", "You need at least " + cost + " to teleport!");
                            return;
                        }
                    }

                    if((Boolean) ch.get(Value.COOLDOWN_ENABLED)) {
                        CooldownHandler cooldown = new CooldownHandler(plugin, p, (Integer) ch.get(Value.COOLDOWN_TIME));
                        cooldown.start();
                    }
                }
            }
        }
    }
}
