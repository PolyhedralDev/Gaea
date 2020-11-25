package org.polydev.gaea;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.population.AsyncPopulationManager;
import org.polydev.gaea.population.PopulationManager;

import java.io.File;

public class Gaea extends JavaPlugin {
    private static boolean debug;
    private static Gaea instance;

    public static Gaea getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();
        Metrics metrics = new Metrics(this, 9092);
        saveDefaultConfig();
        reloadConfig();
        FileConfiguration configuration = getConfig();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, AsyncPopulationManager::asyncPopulate, 1, 1);
        debug = configuration.getBoolean("debug", false);
    }

    public static File getGaeaFolder(World w) {
        File f = new File(w.getWorldFolder(), "gaea");
        f.mkdirs();
        return f;
    }

    public static boolean isDebug() {
        return debug;
    }
}
