package com.nedcraft.dpasi314.RandomTP.Handlers;

import com.nedcraft.dpasi314.RandomTP.RandomTP;

public class ConfigurationHandler {

    RandomTP plugin;

    public ConfigurationHandler(RandomTP plugin) {
        this.plugin = plugin;
    }

    public void loadConfiguration() {
        String teleportPath = "RandomTP.Teleport";
        String costPath = "RandomTP.Cost";
        String anticheatPath = "RandomTP.AntiCheat";
        String cooldownPath = "RandomtTP.Cooldown";

        // Set default information for the config
        plugin.getConfig().addDefault(teleportPath + ".CoordsX", 5000);
        plugin.getConfig().addDefault(teleportPath + ".CoordsY", 5000);
        plugin.getConfig().addDefault(teleportPath + ".SafeTeleport", false);
        plugin.getConfig().addDefault(teleportPath + ".World", "world");

        // Set default cost information
        plugin.getConfig().addDefault(costPath + ".Enable", false);
        plugin.getConfig().addDefault(costPath + ".TeleportCost", 0.0);

        // Set default AntiCheat information
        plugin.getConfig().addDefault(anticheatPath + ".Enable", false);
        plugin.getConfig().addDefault(anticheatPath + ".MinimumHealthLevel", 5);

        // Set default cool-down information
        plugin.getConfig().addDefault(cooldownPath + ".Enable", false);
        plugin.getConfig().addDefault(cooldownPath + ".CooldownTime", 200);

        // Save the config and its information
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();

    }

    public void reloadConfiguration() {
        plugin.reloadConfig();
    }
}
