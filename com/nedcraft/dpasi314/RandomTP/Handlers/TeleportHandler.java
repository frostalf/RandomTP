package com.nedcraft.dpasi314.RandomTP.Handlers;

import com.iCo6.system.Account;
import com.nedcraft.dpasi314.RandomTP.RandomTP;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Random;

public class TeleportHandler {

    public static RandomTP plugin;

    public TeleportHandler(RandomTP plugin) {
        TeleportHandler.plugin = plugin;
    }

    public static boolean isTeleportOK = true;

    public static int minHealth() {
        return plugin.getConfig().getInt("RandomTP.AntiCheat.MinimumHealthLevel");

    }

    public static boolean isAntiCheat() {
        Boolean anti = plugin.getConfig().getBoolean("RandomTP.AntiCheat.Enable");
        return anti != null ? anti : false;
    }

    public static long getCooldownTime() {
        return plugin.getConfig().getLong("RandomTP.Cooldown.CooldownTime");
    }

    public static boolean isCoolDownEnabled() {
        Boolean enabled = plugin.getConfig().getBoolean("RandomTP.Cooldown.Enable");
        return (enabled != null) ? enabled : false;
    }

    public static boolean isCurrentWorldTeleportEnabled() {
        Boolean enabled = plugin.getConfig().getBoolean("RandomTP.Teleport.TeleportInCurrentWorld");
        return (enabled != null) ? enabled : false;
    }


    public static int getMaxX() {
        String s = plugin.getConfig().getString("RandomTP.Teleport.CoordsX");
        return Integer.parseInt(s);
    }

    public static int getMaxZ() {
        String s = plugin.getConfig().getString("RandomTP.Teleport.CoordsZ");
        return Integer.parseInt(s);

    }

    public static String getWorld() {
        return plugin.getConfig().getString("RandomTP.Teleport.World");
    }

    public static boolean isPurchaseable() {
        return plugin.getConfig().getBoolean("RandomTP.Cost.Enable");

    }

    public static double getCost() {
        return plugin.getConfig().getDouble("RandomTP.Cost.TeleportCost");
    }

    public static int randomize(int x) {
        Random rand = new Random();
        Random rand1 = new Random();
        x = rand1.nextInt(x);
        int x1 = rand.nextInt(2);

        if (x1 == 1) {
            x = x * -1;
        }

        return x;
    }

    /*public static boolean isHealthOK(Player player){
        boolean health;
        if(player.getHealth() < (double)TeleportHandler.minHealth()){
            health = false;
        } else {
            health = true;
        }

        return health;
    }*/
    public static boolean checkForCheating(Player player) {
        boolean cheating;
        if (player.getLocation().getBlock().getType() == Material.WATER ||
                player.getLocation().getBlock().getType() == Material.LAVA ||
                player.getLocation().getBlock().getType() == Material.AIR) {
            cheating = false;

        } else {

            cheating = true;
        }

        return cheating;

    }

    public static void removeCost(Player player) {
        double cost = TeleportHandler.getCost();
        Account account = new Account(player.getName());

        account.getHoldings().subtract(cost);

        player.sendMessage(ChatColor.GOLD + "A total of " + ChatColor.GREEN + cost + ChatColor.GOLD + " has been removed from your account.");

    }

    public static void setCoolDown(Player player) {
        Cooldown.addCooldown("RandomTP-Cooldown", player.getName(), (TeleportHandler.getCooldownTime() * 1000));
    }

    public static String getCoolDownLeft(Player player) {
        PlayerCooldown pc = Cooldown.getCooldown("RandomTP-Cooldown", player.getName(), (TeleportHandler.getCooldownTime() * 1000));
        Cooldown.getCooldown("RandomTP-Cooldown", player.getName(), pc.getTimeLeft());
        if (pc.isOver()) {
            pc.reset();
            return ChatColor.GREEN + "You can teleport again! Hurray!";
        } else {
            return ChatColor.GOLD + "Sorry, You have to wait " + ChatColor.GREEN + (pc.getTimeLeft() / 1000) + " seconds " + ChatColor.GOLD + "before you can teleport again.";
        }

    }

    public static boolean isCoolDownFinished(Player player) {
        PlayerCooldown pc = Cooldown.getCooldown("RandomTP-Cooldown", player.getName(), (TeleportHandler.getCooldownTime() * 1000));
        if (pc.isOver()) {
            return true;
        } else {
            return false;
        }
    }

    public static void saveMe(Player player) {
        Location loc = player.getLocation();
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();
        int newY = loc.getWorld().getHighestBlockYAt(loc);
        Location saveLoc = new Location(Bukkit.getWorld(TeleportHandler.getWorld()), x, newY + 1, z);
        player.teleport(saveLoc);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Ta-Da!");
    }

    public static void sendTeleport(Player player) {
        World world;

        if (isCurrentWorldTeleportEnabled() == true) {
            world = player.getWorld();
        } else {
            world = Bukkit.getWorld(TeleportHandler.getWorld());
        }

        int X = TeleportHandler.getMaxX();
        int Z = TeleportHandler.getMaxZ();
        int x = TeleportHandler.randomize(X);
        int z = TeleportHandler.randomize(Z);
        int y = world.getHighestBlockYAt(x, z);
        Location loc = new Location(world, x, y, z);
        loc.getChunk().load();
        Chunk c = loc.getChunk();
        world.loadChunk(c);
        world.refreshChunk(c.getX(), c.getZ());
        loc.setY(loc.getWorld().getHighestBlockYAt(loc));

        if (checkLocation(player, loc) == false) {
            player.sendMessage(ChatColor.GOLD + "Couldn't find a safe place to teleport you! Try again!");

            if (isCoolDownEnabled() == true) {
                PlayerCooldown pc = Cooldown.getCooldown("RandomTP-Cooldown", player.getName(), (TeleportHandler.getCooldownTime() * 1000));
                pc.reset();
            }

            isTeleportOK = false;

        } else {

            player.teleport(loc);
            player.sendMessage(ChatColor.DARK_AQUA + "Teleported to: ");
            player.sendMessage(ChatColor.DARK_AQUA + "X: " + ChatColor.AQUA + x);
            player.sendMessage(ChatColor.DARK_AQUA + "Y: " + ChatColor.AQUA + loc.getY());
            player.sendMessage(ChatColor.DARK_AQUA + "Z: " + ChatColor.AQUA + z);
            player.sendMessage(ChatColor.DARK_AQUA + "World: " + ChatColor.AQUA + player.getWorld().getName());
            //For Debug:   player.sendMessage(ChatColor.DARK_AQUA + "Eye: " + player.getEyeLocation().getBlock().getType());

            isTeleportOK = true;

        }

    }

    public static boolean checkLocation(Player player, Location loc) {
        if (loc.getBlock().getBiome() == Biome.OCEAN || loc.getBlock().getBiome() == Biome.DEEP_OCEAN || loc.getBlock().getBiome() == Biome.RIVER) {
            player.sendMessage(ChatColor.RED + "ERROR: The location " + ChatColor.GOLD + "(" + ChatColor.RED + loc.getX() + ChatColor.GOLD + ", " + ChatColor.RED + loc.getY() + ChatColor.GOLD + ", " + ChatColor.RED + loc.getZ() + ChatColor.GOLD + ")" + ChatColor.RED + " is a " + loc.getBlock().getBiome() + " biome!");
            return false;
        }

         if (loc.getBlock().getType() != Material.AIR || loc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.LAVA) {
	            player.sendMessage(ChatColor.RED + "ERROR: The location " + ChatColor.GOLD + "(" + ChatColor.RED + loc.getX() + ChatColor.GOLD + ", " + ChatColor.RED + loc.getY() + ChatColor.GOLD + ", " + ChatColor.RED + loc.getZ() + ChatColor.GOLD + ")" + ChatColor.RED + " could cause you to die!!");
	            return false;
	        }
        return true;
    }
}

