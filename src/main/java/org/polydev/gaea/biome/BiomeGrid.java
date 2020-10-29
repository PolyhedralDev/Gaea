package org.polydev.gaea.biome;

import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.FastNoiseLite;

public class BiomeGrid extends BiomeDistributor {
    private final FastNoiseLite noiseX;
    private final FastNoiseLite noiseZ;
    private final int sizeX;
    private final int sizeZ;
    private Biome[][] grid;


    public BiomeGrid(World w, float xFreq, float zFreq, int sizeX, int sizeZ) {
        super(w);
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;

        this.noiseX = new FastNoiseLite((int) w.getSeed());
        this.noiseX.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        this.noiseX.setFractalType(FastNoiseLite.FractalType.FBm);
        this.noiseX.setFractalOctaves(4);
        this.noiseX.setFrequency(xFreq);

        this.noiseZ = new FastNoiseLite((int) w.getSeed() + 1);
        this.noiseZ.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        this.noiseZ.setFractalType(FastNoiseLite.FractalType.FBm);
        this.noiseZ.setFractalOctaves(4);
        this.noiseZ.setFrequency(zFreq);
    }


    /**
     * Gets the biome at a pair of coordinates.
     *
     * @param x - X-coordinate at which to fetch biome
     * @param z - Z-coordinate at which to fetch biome
     * @return Biome - Biome at the given coordinates.
     */
    @Override
    public Biome getBiome(int x, int z, GenerationPhase phase) {
        return grid[getBiomeNoiseX(x, z)][getBiomeNoiseZ(x, z)];
    }

    public float[] getRawNoise(int x, int z) {
        return new float[] {noiseX.getNoise(x, z), noiseZ.getNoise(x, z)};
    }

    /**
     * Get the raw X-noise for coordinates in the Grid.
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Normalized noise
     */
    public int getBiomeNoiseX(int x, int z) {
        return normalize(noiseX.getNoise((float) x, (float) z), sizeX);
    }

    /**
     * Get the raw Z-noise for coordinates in the Grid.
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Normalized noise
     */
    public int getBiomeNoiseZ(int x, int z) {
        return normalize(noiseZ.getNoise((float) x, (float) z), sizeZ);
    }

    public Biome[][] getGrid() {
        return grid;
    }

    public void setGrid(Biome[][] grid) {
        if(grid.length != sizeX) throw new IllegalArgumentException("Invalid length for grid, expected " + sizeX + ", got " + grid.length);
        for(Biome[] gridLayer : grid) {
            if(gridLayer.length != sizeZ) throw new IllegalArgumentException("Invalid length for grid layer, expected " + sizeZ + ", got " + gridLayer.length);
        }
        this.grid = grid;
    }


    public int getSizeX() {
        return sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    /**
     * Takes a noise input and normalizes it to a value between 0 and 15 inclusive.
     *
     * @param i - The noise value to normalize.
     * @return int - The normalized value.
     */
    protected int normalize(double i, int range) {
        return NormalizationUtil.normalize(i, range, 4);
    }
}
