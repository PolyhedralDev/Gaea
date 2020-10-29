package org.polydev.gaea.math;

import org.bukkit.World;
import org.polydev.gaea.biome.BiomeDistributor;

public interface ChunkInterpolator {
    double getNoise(double x, double z);

    double getNoise(double x, double y, double z);

    enum InterpolationType {
        BILINEAR {
            @Override
            public ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeDistributor container, FastNoiseLite noise) {
                return new ChunkInterpolator2(w, chunkX, chunkZ, container, noise);
            }
        },
        TRILINEAR {
            @Override
            public ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeDistributor container, FastNoiseLite noise) {
                return new ChunkInterpolator3(w, chunkX, chunkZ, container, noise);
            }
        };
        public abstract ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeDistributor container, FastNoiseLite noise);
    }
}
