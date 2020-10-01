package org.polydev.gaea.world;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public interface Flora {
    List<Block> getValidSpawnsAt(Chunk chunk, int x, int z);
    boolean plant(Location l);
}
