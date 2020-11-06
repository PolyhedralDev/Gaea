package org.polydev.gaea.generation;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.polydev.gaea.math.ChunkInterpolator;

import java.util.Random;

public abstract class GenerationPopulator {
    public abstract void populate(World world, ChunkGenerator.ChunkData chunk, Random r, int chunkX, int chunkZ, ChunkInterpolator interp);
}
