package com.atomic.rtp;

import com.atomic.rtp.Commands.RTPCommand;
import com.atomic.rtp.Enums.Value;
import com.atomic.rtp.Handlers.ConfigHandler;
import com.atomic.rtp.Handlers.TeleportHandler;
import com.atomic.rtp.Listeners.GlobalListener;
import com.atomic.rtp.Updater.Updater;
import com.atomic.rtp.util.DependencyGraphUtil;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import org.bukkit.Bukkit;

/**
 * Dante Pasionek created: com.atomic.rtp on Jun. 04, 2014 *
 */
public class RandomTeleport extends JavaPlugin {

    static boolean UPDATE;
    static String NEWVERSION;

    @Override
    public void onEnable() {
        init();
        print("Enabled!");
        try {
        Metrics metrics = new Metrics(this);
        metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    @Override
    public void onDisable() {
        ConfigHandler ch = new ConfigHandler(this);
        print("Disabled!");
    }

    /* Initalize EVERYTHING */
    private void init() {
        registerCommands();
        registerListeners();
        initFiles();
        updateCheck();
        ConfigHandler ch = new ConfigHandler(this);
    }

    private void startMetrics() {
        if(getConfig().get("metrics") == null) {
            getConfig().set("metrics", true);
            saveConfig();
        }
        if(getConfig().getBoolean("metrics")) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
                
                Metrics.Graph dependingPlugins = metrics.createGraph("Depending Plugins");
                synchronized (Bukkit.getPluginManager()) {
                    for (final Plugin otherPlugin : DependencyGraphUtil.getPluginsUnsafe()) {
                        if (!otherPlugin.isEnabled()) {
                            continue;
                        }
                        if (!DependencyGraphUtil.isDepending(otherPlugin, this) && !DependencyGraphUtil.isSoftDepending(otherPlugin, this)) {
                            continue;
                        }
                        dependingPlugins.addPlotter(new Metrics.Plotter(otherPlugin.getName()) {
                            @Override
                            public int getValue() {
                                return 1;
                            }
                        });
                    }
                }
                metrics.addGraph(dependingPlugins);
                } catch (IOException ex) {
                    getLogger().warning("Failed to load metrics :(");
                }
        }
    }
    
    /* Register ALL commands */
    private void registerCommands() {
        List<String> blocks = getConfig().getStringList("RandomTP.Teleport.BlocksNotToSpawnOn");
        List<String> biomes = getConfig().getStringList("RandomTP.Teleport.BiomesNotToSpawnIn");
        TeleportHandler.setBadBiomes(biomes);
        TeleportHandler.setBadBlocks(blocks);
        getCommand("randomtp").setExecutor(new RTPCommand(this));
    }

    /* Register ALL listeners */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GlobalListener(this), this);
    }


    private void initFiles() {
        File file = new File(getDataFolder(), "config.yml");
        if (!(getDataFolder().exists())) getDataFolder().mkdir();
        if (!file.exists()) {
            try {
                file.createNewFile();
                getConfig().options().copyDefaults(true);
                getConfig().save(file);
                getConfig().options().copyDefaults(false);
            } catch (IOException e) {
                e.printStackTrace();
                print("Could not generate a configuration file!", Level.SEVERE);
            }
        }
    }

    // The method will only do something if Auto-Update is set to true
    private void updateCheck() {
        if (getConfig().get("RandomTP.Updater.Auto-Update") == null) {
            getConfig().set("RandomTP.Updater.Auto-Update", true);
            saveConfig();
        }
        if (getConfig().getBoolean("RandomTP.Updater.Auto-Update")) {
            final RandomTeleport plugin = this;
            final File file = this.getFile();
            final Updater.UpdateType updateType = Updater.UpdateType.DEFAULT;
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(plugin, 50736, file, updateType, false);
                    RandomTeleport.UPDATE = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
                    RandomTeleport.NEWVERSION = updater.getLatestName();
                    if (updater.getResult() == Updater.UpdateResult.SUCCESS) {
                        getLogger().log(Level.INFO, "Successfully updated ServerTutorial to version {0} for next restart!", updater.getLatestName());
                    }
                }
            });
        }
        if (!this.getConfig().getBoolean("RandomTP.Updater.Auto-Update")) {
            System.out.println("[RandomTP] Auto-Updater disabled! If you would like to have RandomTP Update itself please set Enable to true!");
        }
    }

    private boolean checkForPlugin(Plugin plugin) {
        if (getServer().getPluginManager().isPluginEnabled(plugin))
            return true;
        else
            return false;
    }

    private void print(String message) {
        String prefix = "[RandomTP] ";
        System.out.println(prefix + message);
    }

    private void print(String message, Level level) {
        String prefix = "[" + level + "]" + "[RandomTP] ";
        System.out.println(prefix + message);
    }
}
