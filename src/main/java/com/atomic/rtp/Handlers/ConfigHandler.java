package com.atomic.rtp.Handlers;

import com.atomic.rtp.Enums.Value;
import com.atomic.rtp.RandomTeleport;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Dante Pasionek created: com.atomic.rtp.Handlers on Jun. 04, 2014 *
 */
public class ConfigHandler {

    RandomTeleport plugin = RandomTeleport.getInstance();

    public ConfigHandler() {}

    public Object get(Value value) {
        FileConfiguration config = plugin.getConfig();
        return config.get("RandomTP.Teleport." + value);
    }
}
