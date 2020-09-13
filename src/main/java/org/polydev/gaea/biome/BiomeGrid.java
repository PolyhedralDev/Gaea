package org.polydev.gaea.biome;

import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.math.FastNoise;

public abstract class BiomeGrid {
    private final FastNoise biome;
    private final FastNoise climate;
    private final World world;
    private Biome[][] grid;
    private NormalType normal = NormalType.LOOKUP;

    public BiomeGrid(World w, float freq1, float freq2) {
        this.world = w;
        this.biome = new FastNoise((int) w.getSeed());
        this.biome.setNoiseType(normal.getNoiseType());
        this.biome.setFrequency(freq1);
        this.climate = new FastNoise((int) w.getSeed() + 1);
        this.climate.setNoiseType(normal.getNoiseType());
        this.climate.setFrequency(freq2);
    }

    public BiomeGrid(int seed) {
        this.world = null;
        this.biome = new FastNoise(seed);
        this.biome.setNoiseType(normal.getNoiseType());
        this.biome.setFrequency((float) 1 / 256);
        this.climate = new FastNoise(seed + 1);
        this.climate.setNoiseType(FastNoise.NoiseType.Value);
        this.climate.setFrequency((float) 1 / 128);
    }

    public void setNormalType(NormalType n) {
        this.normal = n;
        this.biome.setNoiseType(normal.getNoiseType());
        this.climate.setNoiseType(normal.getNoiseType());
        if(normal.getOctaves() != 0) {
            this.biome.setFractalOctaves(normal.getOctaves());
            this.climate.setFractalOctaves(normal.getOctaves());
        }
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
        float biomeNoise = biome.getNoise((float) x, (float) z);
        float climateNoise = climate.getNoise((float) x, (float) z);
        return grid[normal.normalize(biomeNoise)][normal.normalize(climateNoise)];
    }

    /**
     * Gets the biome at a location.
     *
     * @param l - The location at which to fetch the biome.
     * @return Biome - Biome at the given coordinates.
     */
    public Biome getBiome(Location l) {
        float biomeNoise = biome.getNoise((float) l.getBlockX(), (float) l.getBlockZ());
        float climateNoise = climate.getNoise((float) l.getBlockX(), (float) l.getBlockZ());
        return grid[normal.normalize(biomeNoise)][normal.normalize(climateNoise)];
    }

    public World getWorld() {
        return world;
    }

    public enum NormalType {
        LEGACY {
            @Override
            protected int normalize(double i) {
                if(i > 0) i = Math.pow(i, 0.8125); // Redistribute
                else i = - Math.pow(- i, 0.8125); // Redistribute
                return Math.min((int) Math.floor((i + 1) * 8), 15);
            }

            @Override
            protected FastNoise.NoiseType getNoiseType() {
                return FastNoise.NoiseType.Value;
            }

            @Override
            protected int getOctaves() {
                return 0;
            }
        }, LOOKUP {
            private final double[] lookup = new double[] {- 0.30535656213760376D, - 0.24068142473697662D, - 0.1912580281496048D, - 0.1484183818101883D, - 0.10884492099285126D, - 0.07152662426233292D, - 0.03578951954841614D, - 7.427185773849487E-4D, 0.03426129370927811D, 0.07007939368486404D, 0.10747101902961731D, 0.147150918841362D, 0.19017954170703888D, 0.23992890119552612D, 0.3045589327812195D, 0.6485831141471863D};

            @Override
            protected int normalize(double i) {
                for(int j = 0; j < lookup.length; j++) {
                    if(i < lookup[j]) return j;
                }
                return lookup.length - 1;
            }

            @Override
            protected FastNoise.NoiseType getNoiseType() {
                return FastNoise.NoiseType.SimplexFractal;
            }

            @Override
            protected int getOctaves() {
                return 5;
            }
        };

        /**
         * Takes a noise input and normalizes it to a value between 0 and 15 inclusive.
         *
         * @param i - The noise value to normalize.
         * @return int - The normalized value.
         */
        protected abstract int normalize(double i);
        protected abstract FastNoise.NoiseType getNoiseType();
        protected abstract int getOctaves();
    }
}
