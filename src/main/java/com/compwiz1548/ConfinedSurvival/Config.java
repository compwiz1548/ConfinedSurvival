package com.compwiz1548.ConfinedSurvival;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private static ConfinedSurvival plugin;
    private static FileConfiguration cfg = null;
    private static Logger csLog = null;
    private static String currentWorldName = null;
    private static Runtime rt = Runtime.getRuntime();

    private static boolean netherUnlocked = false;
    private static int numKilled = 0;
    private static int killGoal = 0;

    public static void log(Level lvl, String text) {
        csLog.log(lvl, text);
    }

    public static void log(String text) {
        log(Level.INFO, text);
    }

    public static void logWarn(String text) {
        log(Level.WARNING, text);
    }

    public static void logConfig(String text) {
        log(Level.INFO, "[CONFIG] " + text);
    }

    public static boolean HasPermission(Player player, String request) {
        return HasPermission(player, request, true);
    }

    public static boolean HasPermission(Player player, String request, boolean notify) {
        if (player == null)                // console, always permitted
            return true;

        if (player.hasPermission("confinedsurvival." + request))    // built-in Bukkit superperms
            return true;

        if (notify)
            player.sendMessage("You do not have sufficient permissions.");

        return false;
    }

    public static void load(ConfinedSurvival master, boolean logIt) {
        plugin = master;
        csLog = plugin.getLogger();

        plugin.reloadConfig();
        cfg = plugin.getConfig();

        ConfigurationSection worlds = cfg.getConfigurationSection("worlds");
        currentWorldName = plugin.getServer().getWorlds().get(0).getName();
        if (worlds != null) {
            ConfigurationSection current = worlds.getConfigurationSection(currentWorldName);
            netherUnlocked = current.getBoolean("nether-unlocked", false);
            numKilled = current.getInt("zombies-killed", 0);
            killGoal = current.getInt("number-to-unlock-nether", 3);
        }
        if (logIt)
            logConfig("Configuration loaded.");
        save(false);
    }

    public static void save(boolean logIt) {
        if (cfg == null) {
            return;
        }
        cfg.set("worlds", null);

        cfg.set("worlds." + currentWorldName + ".nether-unlocked", netherUnlocked);
        cfg.set("worlds." + currentWorldName + ".zombies-killed", numKilled / 4);
        cfg.set("worlds." + currentWorldName + ".number-to-unlock-nether", killGoal);

        plugin.saveConfig();
        if (logIt)
            logConfig("Configuration saved.");
    }

    public static void incrementKilled() {
        numKilled++;
        if (numKilled / 4 >= killGoal)
            netherUnlocked = true;
        save(false);
    }

    public static boolean nether() {
        return netherUnlocked;
    }

    public static int numLeft() {
        return killGoal - (numKilled / 4);
    }

    public static void setNetherLock(boolean lock) {
        netherUnlocked = lock;
        save(false);
    }

    public static void setNumKilled(int num) {
        numKilled = num;
        save(false);
    }

    public static String worldName() {
        return currentWorldName;
    }
}
