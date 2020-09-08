package org.polydev.gaea.biome;

import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.world.BlockPalette;

public abstract class BiomeTerrain3 {
    public BiomeTerrain3() {
    }

    /**
     * Gets the 3D noise at a set of coordinates using the provided FastNoise instance.
     *
     * @param gen - The FastNoise instance to use.
     * @param x   - The x coordinate.
     * @param y   - the y coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    public abstract double getNoise(FastNoise gen, int x, int y, int z);

    /**
     * Gets the BlockPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    public abstract BlockPalette getPalette();
}
