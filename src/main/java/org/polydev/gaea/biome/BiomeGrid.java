package org.polydev.gaea.biome;

import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.math.FastNoise;

public abstract class BiomeGrid {
    private final FastNoise biome;
    private final FastNoise climate;
    private final World world;
    private Biome[][] grid;


    public BiomeGrid(World w, float freq1, float freq2) {
        this.world = w;
        this.biome = new FastNoise((int) w.getSeed());
        this.biome.setNoiseType(FastNoise.NoiseType.Value);
        this.biome.setFrequency(freq1);
        this.climate = new FastNoise((int) w.getSeed() + 1);
        this.climate.setNoiseType(FastNoise.NoiseType.Value);
        this.climate.setFrequency(freq2);
    }

    public BiomeGrid(int seed) {
        this.world = null;
        this.biome = new FastNoise(seed);
        this.biome.setNoiseType(FastNoise.NoiseType.Value);
        this.biome.setFrequency((float) 1 / 256);
        this.climate = new FastNoise(seed + 1);
        this.climate.setNoiseType(FastNoise.NoiseType.Value);
        this.climate.setFrequency((float) 1 / 128);
    }

    /**
     * Takes a noise input and normalizes it to a value between 0 and 15 inclusive.
     *
     * @param i - The noise value to normalize.
     * @return int - The normalized value.
     */
    private static int normalize(double i) {
        if(i > 0) i = Math.pow(i, 0.8125); // Redistribute
        else i = -Math.pow(-i, 0.8125); // Redistribute
        return Math.min((int) Math.floor((i+1)*8), 15);
    }

    public void setGrid(Biome[][] grid) {
        this.grid = grid;
    }

    /**
     * Gets the biome at a pair of coordinates.
     *
     * @param x - X-coordinate at which to fetch biome
     * @param z - Z-coordinate at which to fetch biome
     * @return Biome - Biome at the given coordinates.
     */
    public Biome getBiome(int x, int z) {
        float biomeNoise = biome.getValue((float) x, (float) z);
        float climateNoise = climate.getValue((float) x, (float) z);
        return grid[normalize(biomeNoise)][normalize(climateNoise)];
    }

    /**
     * Gets the biome at a location.
     *
     * @param l - The location at which to fetch the biome.
     * @return Biome - Biome at the given coordinates.
     */
    public Biome getBiome(Location l) {
        float biomeNoise = biome.getValue((float) l.getBlockX(), (float) l.getBlockZ());
        float climateNoise = climate.getValue((float) l.getBlockX(), (float) l.getBlockZ());
        return grid[normalize(biomeNoise)][normalize(climateNoise)];
    }

    public World getWorld() {
        return world;
    }
}
