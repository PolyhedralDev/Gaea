package org.polydev.gaea.math;

import org.polydev.gaea.biome.BiomeGrid;

public enum InterpolationType {
    BILINEAR, TRILINEAR;
    public ChunkInterpolator getInstance(int chunkX, int chunkZ, BiomeGrid grid, FastNoise noise) {
        switch(this) {
            case TRILINEAR: return new ChunkInterpolator3(chunkX, chunkZ, grid, noise);
            case BILINEAR: return new ChunkInterpolator2(chunkX, chunkZ, grid, noise);
            default: return null;
        }
    }
}
