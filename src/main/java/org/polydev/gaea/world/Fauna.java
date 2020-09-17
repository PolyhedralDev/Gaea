package org.polydev.gaea.world;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public interface Fauna {
    Block getHighestValidSpawnAt(Chunk chunk, int x, int z);
    boolean plant(Location l);
}
