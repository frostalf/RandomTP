package rtp;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Dante Pasionek created: com.atomic.rtp on Jun. 04, 2014 *
 */
public class RandomTeleport extends JavaPlugin {

    public void onEnable() {
        init();
        print("Enabled!");
    }

    public void onDisable() {
        print("Disabled!");
    }

    /* Initalize EVERYTHING */
    private void init() {
        registerCommands();
        registerListeners();
        initFiles();
    }

    /* Register ALL commands */
    private void registerCommands() {

    }

    /* Register ALL listeners */
    private void registerListeners() {

    }

    private void initFiles() {
        File file = new File(getDataFolder(), "config.yml");
        if(!(getDataFolder().exists())) getDataFolder().mkdir();
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

    private void loadPlayerFile() {

    }

    private void writePlayerFile() {
        File file = new File(getDataFolder(), ".yml");
        if(!(getDataFolder().exists())) getDataFolder().mkdir();
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

    private void print(String message) {
        String prefix =  "[RandomTP] ";
        System.out.println(prefix + message);
    }

    private void print(String message, Level level) {
        String prefix = "[" +level + "]" + "[RandomTP] ";
        System.out.println(prefix + message);
    }
}
