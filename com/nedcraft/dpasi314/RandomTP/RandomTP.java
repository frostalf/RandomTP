package com.nedcraft.dpasi314.RandomTP;

import com.nedcraft.dpasi314.RandomTP.Commands.RandomTpCommand;
import com.nedcraft.dpasi314.RandomTP.Handlers.TeleportHandler;
import com.nedcraft.dpasi314.RandomTP.Listeners.BlockListener;
import com.nedcraft.dpasi314.RandomTP.Updater.Updater;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RandomTP extends JavaPlugin {

    String LOG_PREFIX = "[RandomTP] ";
    Logger log = Logger.getLogger("Minecraft");
    public final BlockListener blockListener = (new BlockListener(this));

    public void onEnable() {

        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getCommand("randomtp").setExecutor(new RandomTpCommand(this));
        new TeleportHandler(this);


       if(this.getConfig().getBoolean("RandomTP.Updater.Enable") == true)
       {
        Updater updater = new Updater(this, 50736, this.getFile(), Updater.UpdateType.DEFAULT, false);

        Updater.UpdateResult result = updater.getResult();

        switch(result)
        {
            case SUCCESS:
                System.out.println("[RandomTP] A new version of RandomTP is available - Will download at next restart (" + updater.getLatestName() + ")");
                break;
            case NO_UPDATE:
                System.out.println("[RandomTP] Your current version of RandomTP is up to date!");
                break;
            case UPDATE_AVAILABLE:
                System.out.println("[RandomTP] There is a new version of RandomTP available! - Downloading disabled!");
                break;
            default:
                System.out.println("[RandomTP] Uh-oh! Something went wrong! Please check the API Key, Plugin ID, or DBO status!");
                break;
        }
       }

        if(this.getConfig().getBoolean("RandomTP.Updater.Enable") == false)
        {
            System.out.println("[RandomTP] Auto-Updater disabled! If you would like to have RandomTP Update itself please set Enable to true!");

        }


       if(this.getConfig().getBoolean("RandomTP.Teleport.OnlyOnce") == true)
       {
        File teleport = new File(getDataFolder(), "telport.yml");

        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        if (!teleport.exists())
        {
            try
            {
                teleport.createNewFile();
                getConfig().options().copyDefaults(true);
                getConfig().save(teleport);
                getConfig().options().copyDefaults(false);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("[RandomTP] Uh-oh! Couldn't create a teleport.yml file for one time teleports. ");
            }
        }
       }

        File file = new File(getDataFolder(), "config.yml");

        if (!getDataFolder().exists()) getDataFolder().mkdir();
        if (!file.exists()) {
            try {
                file.createNewFile();
                getConfig().options().copyDefaults(true);
                saveDefaultConfig();
                getConfig().save(file);
                getConfig().options().copyDefaults(false);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("[" + Level.SEVERE + "] " + "[RandomTP] ERROR: RandomTP has encountered an error: Could not create configuration file.");
            }
        }
        info("RandomTP has been enabled!");
    }

    public void onDisable() {
        try {
            this.getConfig().save("config.yml");
            info("RandomTP has been disabled!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("[" + Level.SEVERE + "] " + "[RandomTP] ERROR: RandomTP has encountered and error: Could not save configuration file!");
        }
    }

    public void info(String msg) {
        this.log.log(Level.INFO, "[RandomTP] " + msg);
    }
}