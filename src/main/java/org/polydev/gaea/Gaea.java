package org.polydev.gaea;

import org.bstats.bukkit.Metrics;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.util.PaperUtil;

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
        Metrics metrics = new Metrics(this, 9092);
        saveDefaultConfig();
        reloadConfig();
        FileConfiguration configuration = getConfig();
        debug = configuration.getBoolean("debug", false);
        PaperUtil.checkPaper(this);
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
