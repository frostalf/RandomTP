package rtp.Handlers;

import org.bukkit.configuration.file.FileConfiguration;
import rtp.Enums.Value;
import rtp.RandomTeleport;

/**
 * Dante Pasionek created: com.atomic.rtp.Handlers on Jun. 04, 2014 *
 */
public class ConfigHandler {

    RandomTeleport plugin;

    public ConfigHandler(RandomTeleport plugin) {
        this.plugin = plugin;
    }


    public Object get(Value value) {
        FileConfiguration config = plugin.getConfig();
        return config.get("RandomTP.Teleport." + value);
    }
}
