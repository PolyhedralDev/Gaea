package org.polydev.gaea.biome;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.world.palette.Palette;

/**
 * Class containing information about how a Biome should be generated
 */
public interface Generator {
    /**
     * Gets the 2D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param gen - The FastNoiseLite instance to use.
     * @param x   - The x coordinate.
     * @param z   - The z coordinate.
     * @return Noise value at the specified coordinates.
     */
    double getNoise(FastNoiseLite gen, World w, int x, int z);

    /**
     * Gets the 3D noise at a trio of coordinates using the provided FastNoiseLite instance.
     *
     * @param gen - The FastNoiseLite instance to use.
     * @param x   - The x coordinate.
     * @param y   - The y coordinate.
     * @param z   - The z coordinate.
     * @return Noise value at the specified coordinates.
     */
    double getNoise(FastNoiseLite gen, World w, int x, int y, int z);

    /**
     * Gets the BlockPalette at a Y-value to generate the biome with.
     *
     * @return BlockPalette - The biome's palette.
     */
    Palette<BlockData> getPalette(int y);

    /**
     * Returns true if the biome should be interpolated just once, false to use advanced interpolation + blending.
     * @return Whether biome should use minimal interpolation
     */
    boolean useMinimalInterpolation();
}
