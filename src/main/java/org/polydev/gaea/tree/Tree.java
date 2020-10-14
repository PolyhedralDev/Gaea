package org.polydev.gaea.tree;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public interface Tree {
    boolean plant(Location l, Random r, JavaPlugin main);
}
