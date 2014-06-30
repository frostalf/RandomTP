package com.atomic.rtp;

import com.atomic.rtp.Commands.RTPCommand;
import com.atomic.rtp.Enums.Value;
import com.atomic.rtp.Handlers.ConfigHandler;
import com.atomic.rtp.Handlers.TeleportHandler;
import com.atomic.rtp.Listeners.GlobalListener;
import com.atomic.rtp.Updater.Updater;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Dante Pasionek created: com.atomic.rtp on Jun. 04, 2014 *
 */
public class RandomTeleport extends JavaPlugin {

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

    /* Register ALL commands */
    private void registerCommands() {
        getCommand("randomtp").setExecutor(new RTPCommand(this));
    }

    /* Register ALL listeners */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GlobalListener(this), this);
    }


    private void setAllPlayers() {
        File file = new File(getDataFolder(), "players.txt");
        List<String> list = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                list.add(line);
            }

        } catch (IOException e) {

        }
        TeleportHandler th = new TeleportHandler(null, null, -1, -1);
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
        if (this.getConfig().getBoolean("RandomTP.Updater.Auto-Update") == true) {
            Updater updater = new Updater(this, 50736, this.getFile(), Updater.UpdateType.DEFAULT, false);

            Updater.UpdateResult result = updater.getResult();

            switch (result) {
                case SUCCESS:
                    System.out.println("[RandomTP] A new version of RandomTP is available - Will download at next restart (" + updater.getLatestName() + ")");
                    break;
                case NO_UPDATE:
                    System.out.println("[RandomTP] Your version of RandomTP is up to date!");
                    break;
                case UPDATE_AVAILABLE:
                    System.out.println("[RandomTP] There is a new version of RandomTP available! - Downloading disabled!");
                    break;
                default:
                    System.out.println("[RandomTP] Uh-oh! Something went wrong! Please check the API Key, Plugin ID, or DBO status!");
                    break;
            }
        }

        if (this.getConfig().getBoolean("RandomTP.Updater.Auto-Update") == false) {
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
