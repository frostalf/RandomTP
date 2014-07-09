package com.atomic.rtp.Handlers;

import com.atomic.rtp.RandomTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Dante Pasionek created: com.atomic.rtp.Handlers on Jun. 04, 2014 *
 */
public class TeleportHandler {

    RandomTeleport plugin;
    Player player = null;
    World world = null;
    int xCoord = -1;
    int zCoord = -1;
    int xF = 0, yF = 0, zF = 0;
    String uuid;
    static List<String> biomes = null;
    static List<String> blocks = null;

    public TeleportHandler(Player player, World world, int xCoord, int zCoord) {
        this.player = player;
        this.world = world;
        uuid = player.getUniqueId().toString();
        this.xCoord = xCoord;
        this.zCoord = zCoord;
    }

    public void teleport() {
        Location location = getLocation();
        player.teleport(location);
    }

    public int getX() { return xF; }
    public int getY() { return yF; }
    public int getZ() { return zF; }

    private void set(double x, double y, double z) {
        xF = (int) x;
        yF = (int) y;
        zF = (int) z;
    }

    public String getMessage() {
        String m1 = ChatColor.DARK_AQUA + "Teleported to the Location:";
        String m2 = ChatColor.DARK_AQUA + "X: " + ChatColor.AQUA + getX();
        String m3 = ChatColor.DARK_AQUA + "Y: " + ChatColor.AQUA + getY();
        String m4 = ChatColor.DARK_AQUA + "Z: " + ChatColor.AQUA + getZ();
        String m5 = ChatColor.DARK_AQUA + "World: " + ChatColor.AQUA + world.getName();
        String N = "\n";

        return m1 + N + m2 + N + m3 + N + m4 + N + m5;
    }

    protected Location getLocation() {
        Random random = new Random();
        int x = random.nextInt(xCoord);
        int z = random.nextInt(zCoord);
        x = randomizeType(x);
        z = randomizeType(z);
        int y = 63;

        int spawnX = (int) this.world.getSpawnLocation().getX();
        int spawnY = (int) this.world.getSpawnLocation().getY();
        
        Location loc = safeize(new Location(world, x + spawnX, y + spawnY, z));
        set(loc.getX(), loc.getY(), loc.getZ());

        return loc;
    }

    public static void setBadBiomes(List<String> badBiomes) {
        biomes = badBiomes;
    }

    public static void setBadBlocks(List<String> badBlocks) {
        blocks = badBlocks;
    }

    protected Location safeize(Location location) {
        //Attempt to make the Y the highest block so players don't die.
        location.setY(location.getWorld().getHighestBlockYAt(location));

        while(location.getBlock().getType() != Material.AIR) {
             location.setX(location.getX() + 1);
             location.setZ(location.getZ() + 1);
             location.setY(location.getY() + 1);
        }

        while(player.getEyeLocation().getBlock().getType() != Material.AIR) {
            location.setX(location.getX() + 1);
            location.setY(location.getY() + 1);
            location.setZ(location.getZ() + 1);
        }

        String block = location.getBlock().getType().name();
        String biome = location.getBlock().getBiome().name();

        if(biomes.contains(biome)) {
            location = getLocation();
        }

        if(blocks.contains(block)) {
            location = getLocation();
        }

        // Do it again just in case Y gets fucked over in the code above.
        location.setY(location.getWorld().getHighestBlockYAt(location));

        return location;
    }

    protected int randomizeType(int i) {
        Random random = new Random();
        int j = random.nextInt(2);
        switch(j) {
            case 0:
                return (i = i * -1);
            case 1:
                return i;
            default:
                return (i = i * -1);
        }
    }
}
