package com.atomic.rtp.Handlers;

import com.atomic.rtp.RandomTeleport;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Dante Pasionek created: com.atomic.rtp.Handlers on Jun. 06, 2014 *
 */
public class CooldownHandler {

    RandomTeleport plugin;
    Player player = null;
    int coolDownTime = -1;

    Map<String, Long> map = new HashMap<String, Long>();
    static Map<String, CooldownHandler> cooldownMap = new HashMap<String, CooldownHandler>();


    public CooldownHandler(RandomTeleport plugin, Player player, int coolDownTime) {
        this.plugin = plugin;
        this.player = player;
        this.coolDownTime = coolDownTime;
    }

    public void start() {
        map.put(player.getUniqueId().toString(), System.currentTimeMillis());
        cooldownMap.put(player.getUniqueId().toString(), this);
    }

    public void finalize() {
        map.remove(player.getUniqueId().toString());
        cooldownMap.remove(player.getUniqueId().toString());
    }

    public long getTimeLeft(Player player) {
        long startTime = (System.currentTimeMillis() - map.get(player.getUniqueId().toString())) / 1000;
        return startTime - coolDownTime;
    }

    public boolean check(Player player) {
        if(((map.get(player.getUniqueId().toString()) - System.currentTimeMillis()) / 1000) < coolDownTime)
            return false;
        else
            return true;
    }

    public static CooldownHandler getCooldown(Player player) {
        return cooldownMap.get(player.getUniqueId().toString());
    }

    public static boolean areTherePlayersInTheMap() {
       return cooldownMap.isEmpty();
    }
}
