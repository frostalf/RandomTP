package com.nedcraft.dpasi314.RandomTP.Handlers;

import com.nedcraft.dpasi314.RandomTP.RandomTP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Dante Pasionek created: com.nedcraft.dpasi314.RandomTP.Handlers on Dec. 31, 2013 *
 */
public class MapHandler {

    public static RandomTP plugin;

    public MapHandler(RandomTP plugin) {
        MapHandler.plugin = plugin;
    }

    public static HashMap<String, Boolean> map = new HashMap<String, Boolean>();

    public static boolean canPlayerTeleport(String playername)
    {
        if(map.get(playername) != null)
        {
            return false;
        }

        if(map.get(playername) == null)
        {
            return true;
        }

        return false;
    }

    public static void addPlayerToMap(String playername) {
        map.put(playername, true);
    }

    public static void removePlayerFromMap(String playername) {
        map.remove(playername);
    }

    public static void loadMap() throws IOException {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        File teleport = new File(plugin.getDataFolder(), "teleport.yml");

        if (!teleport.exists())
            teleport.createNewFile();

        FileConfiguration config = YamlConfiguration.loadConfiguration(teleport);
        Set<String> keys = config.getKeys(true);

        for (String key : keys) {
            map.put(key, config.getBoolean(key));
        }

        config.save(teleport);
    }


    public static void saveMap() throws IOException {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        File teleport = new File(plugin.getDataFolder(), "teleport.yml");
        teleport.delete();
        teleport.createNewFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(teleport);

        for (Entry<String, Boolean> e : map.entrySet()) {
            config.set(e.getKey(), e.getValue());
        }
        config.save(teleport);
    }

}



