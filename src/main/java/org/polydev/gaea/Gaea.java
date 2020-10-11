package org.polydev.gaea;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Gaea extends JavaPlugin {
    private static boolean debug;

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
        reloadConfig();
        FileConfiguration configuration = getConfig();
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
