package org.polydev.gaea.math;

import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.biome.BiomeTerrain;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to abstract away the 16 Interpolators needed to generate a chunk.<br>
 *     Contains method to get interpolated noise at a coordinate within the chunk.
 */
public class ChunkInterpolator3 implements ChunkInterpolator {
    private final Interpolator3[][][] interpGrid = new Interpolator3[4][64][4];
    private final int chunkX;
    private final int chunkZ;
    private final BiomeGrid grid;
    private final FastNoise noise;
    private final int xOrigin;
    private final int zOrigin;
    private final World w;

    /**
     * Instantiates a 3D ChunkInterpolator at a pair of chunk coordinates, with a BiomeGrid and FastNoise instance.
     * @param chunkX X coordinate of the chunk.
     * @param chunkZ Z coordinate of the chunk.
     * @param grid BiomeGrid to use for noise fetching.
     * @param noise FastNoise instance to use.
     */
    public ChunkInterpolator3(World w, int chunkX, int chunkZ, BiomeGrid grid, FastNoise noise) {
        this.xOrigin = chunkX << 4;
        this.zOrigin = chunkZ << 4;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.grid = grid;
        this.noise = noise;
        this.w = w;
        BiomeTerrain[][] gridTemp = new BiomeTerrain[8][8];

        for(int x = -2; x < 6; x++) {
            for(int z = -2; z < 6; z++) {
                gridTemp[x+2][z+2] = grid.getBiome(xOrigin + x * 4, zOrigin + z * 4).getGenerator();
            }
        }

        for(byte x = 0; x < 4; x++) {
            for(byte z = 0; z < 4; z++) {
                for(int y = 0; y < 64; y++) {
                    interpGrid[x][y][z] = new Interpolator3(
                            biomeAvg(x, y*4,  z, gridTemp) * 2.0f,
                            biomeAvg(x+1, y*4,  z, gridTemp) * 2.0f,
                            biomeAvg(x, y*4 + 4,  z, gridTemp) * 2.0f,
                            biomeAvg(x+1, y*4 + 4,  z, gridTemp) * 2.0f,
                            biomeAvg(x, y*4,  z + 1, gridTemp) * 2.0f,
                            biomeAvg(x + 1, y*4,  z + 1, gridTemp) * 2.0f,
                            biomeAvg(x, y*4 + 4,  z + 1, gridTemp) * 2.0f,
                            biomeAvg(x + 1, y*4 + 4,  z + 1, gridTemp) * 2.0f);
                }
            }
        }
    }

    private double biomeAvg(int x, int y, int z, BiomeTerrain[][] g) {
        return (g[x+3][z+2].getNoise(noise, w, x*4+xOrigin, y,  z*4+zOrigin)
        + g[x+1][z+2].getNoise(noise, w, x*4+xOrigin, y,  z*4+zOrigin)
        + g[x+2][z+3].getNoise(noise, w, x*4+xOrigin, y,  z*4+zOrigin)
        + g[x+2][z+1].getNoise(noise, w, x*4+xOrigin, y,  z*4+zOrigin))/4D;
    }

    @Override
    public double getNoise(byte x, byte z) {
        return getNoise(x, 0, z);
    }

    /**
     * Gets the noise at a pair of internal chunk coordinates.
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     * @return double - The interpolated noise at the coordinates.
     */
    public double getNoise(byte x, int y, byte z) {
        return interpGrid[x / 4][y / 4][z / 4].trilerp((float) (x % 4) / 4, (float) (y % 4) /4,  (float) (z % 4) / 4);
    }

    private static class CoordinatePair {
        private final int x;
        private final int z;
        public CoordinatePair(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }


        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof CoordinatePair)) return false;
            CoordinatePair other = (CoordinatePair) obj;
            return this.x == other.getX() && this.z == other.getZ();
        }

        @Override
        public String toString() {
            return x+":"+z;
        }
    }
}
