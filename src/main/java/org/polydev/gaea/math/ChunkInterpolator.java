package org.polydev.gaea.math;

import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;

public interface ChunkInterpolator {
    double getNoise(byte x, byte z);
    double getNoise(byte x, int y, byte z);

    enum InterpolationType {
        BILINEAR, TRILINEAR;
        public ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeGrid grid, FastNoise noise) {
            switch(this) {
                case TRILINEAR: return new ChunkInterpolator3(w, chunkX, chunkZ, grid, noise);
                case BILINEAR: return new ChunkInterpolator2(w, chunkX, chunkZ, grid, noise);
                default: return null;
            }
        }
    }
}
