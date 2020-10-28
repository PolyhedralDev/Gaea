package org.polydev.gaea.math;

import org.bukkit.World;
import org.polydev.gaea.biome.BiomeContainer;
import org.polydev.gaea.biome.BiomeGrid;

public interface ChunkInterpolator {
    double getNoise(double x, double z);

    double getNoise(double x, double y, double z);

    enum InterpolationType {
        BILINEAR, TRILINEAR;

        public ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeContainer container, FastNoiseLite noise) {
            switch(this) {
                case TRILINEAR:
                    return new ChunkInterpolator3(w, chunkX, chunkZ, container, noise);
                case BILINEAR:
                    return new ChunkInterpolator2(w, chunkX, chunkZ, container, noise);
                default:
                    return null;
            }
        }
    }
}
