package org.polydev.gaea.math;

import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.biome.BiomeTerrain;

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

    /**
     * Instantiates a 3D ChunkInterpolator at a pair of chunk coordinates, with a BiomeGrid and FastNoise instance.
     * @param chunkX X coordinate of the chunk.
     * @param chunkZ Z coordinate of the chunk.
     * @param grid BiomeGrid to use for noise fetching.
     * @param noise FastNoise instance to use.
     */
    public ChunkInterpolator3(int chunkX, int chunkZ, BiomeGrid grid, FastNoise noise) {
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.grid = grid;
        this.noise = noise;
        for(byte x = 0; x < 4; x++) {
            for(byte z = 0; z < 4; z++) {
                for(int y = 0; y < 64; y++) {
                    interpGrid[x][y][z] = new Interpolator3(
                            biomeAvg(xOrigin + x * 4, y*4,  zOrigin + z * 4) * 2.0f,
                            biomeAvg(xOrigin + x * 4 + 4, y*4,  zOrigin + z * 4) * 2.0f,
                            biomeAvg(xOrigin + x * 4, y*4  + 4,  zOrigin + z * 4) * 2.0f,
                            biomeAvg(xOrigin + x * 4 + 4, y*4  + 4,  zOrigin + z * 4) * 2.0f,
                            biomeAvg(xOrigin + x * 4, y*4,  zOrigin + z * 4 + 4) * 2.0f,
                            biomeAvg(xOrigin + x * 4 + 4, y*4,  zOrigin + z * 4 + 4) * 2.0f,
                            biomeAvg(xOrigin + x * 4, y*4 + 4,  zOrigin + z * 4 + 4) * 2.0f,
                            biomeAvg(xOrigin + x * 4 + 4, y*4 + 4,  zOrigin + z * 4 + 4) * 2.0f);
                }
            }
        }
    }

    private double biomeAvg(int x, int y, int z) {
        return (grid.getBiome(x+4,  z).getGenerator().getNoise(noise, x, y,  z)
        + grid.getBiome(x-4,  z).getGenerator().getNoise(noise, x, y,  z)
        + grid.getBiome(x,  z+4).getGenerator().getNoise(noise, x, y,  z)
        + grid.getBiome(x,  z-4).getGenerator().getNoise(noise, x, y,  z))/4D;
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
}
