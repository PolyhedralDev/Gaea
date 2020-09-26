package org.polydev.gaea;

import org.bukkit.World;

import java.io.File;

public class Gaea {
    public static File getGaeaFolder(World w) {
        File f = new File(w.getWorldFolder(), "gaea");
        f.mkdirs();
        return f;
    }
}
