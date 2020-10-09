package org.polydev.gaea;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Gaea extends JavaPlugin {
    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
    }

    public static File getGaeaFolder(World w) {
        File f = new File(w.getWorldFolder(), "gaea");
        f.mkdirs();
        return f;
    }
}
