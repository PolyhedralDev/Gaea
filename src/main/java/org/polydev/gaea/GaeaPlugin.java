package org.polydev.gaea;

import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.lang.Language;

public abstract class GaeaPlugin extends JavaPlugin {
    public abstract boolean isDebug();
    public abstract Class<? extends GaeaChunkGenerator> getGeneratorClass();
    public abstract Language getLanguage();
}
