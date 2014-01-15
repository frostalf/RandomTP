package com.nedcraft.dpasi314.RandomTP.Handlers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Cooldown {
    private static Set<PlayerCooldown> cooldowns = new HashSet<PlayerCooldown>();

    public static void addCooldown(String cooldownName, String player, long lengthInMillis) {
        PlayerCooldown pc = new PlayerCooldown(cooldownName, player, lengthInMillis);
        Iterator<PlayerCooldown> it = cooldowns.iterator();
        //This section prevents duplicate cooldowns
        while (it.hasNext()) {
            PlayerCooldown iterated = it.next();
            if (iterated.getPlayerName().equalsIgnoreCase(pc.getPlayerName())) {
                if (iterated.getCooldownName().equalsIgnoreCase(pc.getCooldownName())) {
                    it.remove();
                }
            }
        }
        cooldowns.add(pc);
    }

    public static PlayerCooldown getCooldown(String cooldownName, String playerName, long length) {
        Iterator<PlayerCooldown> it = cooldowns.iterator();
        while (it.hasNext()) {
            PlayerCooldown pc = it.next();
            if (pc.getCooldownName().equalsIgnoreCase(cooldownName)) {
                if (pc.getPlayerName().equalsIgnoreCase(playerName)) {
                    return pc;
                }
            }
        }
        return new PlayerCooldown(cooldownName, playerName, length, true);
    }

}

class PlayerCooldown {

    private long startTime;
    private String playerName;
    private String cooldownName;
    private long lengthInMillis;
    private long endTime;
    private boolean overFirst = false;

    PlayerCooldown(String cooldownName, String player, long lengthInMillis) {
        this.cooldownName = cooldownName;
        this.startTime = System.currentTimeMillis();
        this.playerName = player;
        this.lengthInMillis = lengthInMillis;
        this.endTime = startTime + this.lengthInMillis;
    }

    @SuppressWarnings("unused")
    private PlayerCooldown() {
    }

    public PlayerCooldown(String cooldownName, String playerName, long length, boolean over) {
        this(cooldownName, playerName, length);
        overFirst = true;

    }

    public boolean isOver() {
        if (overFirst) {
            overFirst = false;
            return true;
        }
        return endTime < System.currentTimeMillis();
    }

    public int getTimeLeft() {
        return (int) (endTime - System.currentTimeMillis());
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCooldownName() {
        return cooldownName;
    }

    public void reset() {
        startTime = System.currentTimeMillis();
        endTime = startTime + lengthInMillis;
    }
}