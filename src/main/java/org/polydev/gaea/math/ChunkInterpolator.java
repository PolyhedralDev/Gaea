package org.polydev.gaea.math;

import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;

public interface ChunkInterpolator {
    double getNoise(double x, double z);

    double getNoise(double x, double y, double z);

    enum InterpolationType {
        /**
         * 2D (Bilinear) interpolation
         */
        BILINEAR {
            @Override
            public ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeGrid grid, FastNoiseLite noise) {
                return new ChunkInterpolator2(w, chunkX, chunkZ, grid, noise);
            }
        },
        /**
         * 3D (Trilinear) interpolation
         */
        TRILINEAR {
            @Override
            public ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeGrid grid, FastNoiseLite noise) {
                return new ChunkInterpolator3(w, chunkX, chunkZ, grid, noise);
            }
        };
        public abstract ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeGrid grid, FastNoiseLite noise);
    }
}
