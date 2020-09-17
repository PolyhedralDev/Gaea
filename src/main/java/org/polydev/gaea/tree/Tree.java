package org.polydev.gaea.tree;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public interface Tree {
    void plant(Location l, Random r, boolean doSpawnCheck, JavaPlugin main);
}
