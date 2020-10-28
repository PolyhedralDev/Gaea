package org.polydev.gaea.biome;

import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;

/**
 * Class to hold biomes and determine their distribution in the world.
 * @see Biome
 */
public abstract class BiomeContainer {

    private final World world;
    public BiomeContainer(World world) {
        this.world = world;
    }

    /**
     * Get the Biome at a pair of coordinates
     * @param x X coordinate
     * @param z Z coordinate
     * @param phase Phase of generation (some implementations may return different biomes for different phases)
     * @return Biome at coordinates
     */
    public abstract Biome getBiome(int x, int z, GenerationPhase phase);

    /**
     * Get the Biome at a location
     * @param l Location at which to get Biome
     * @param phase Phase of generation (some implementations may return different biomes for different phases)
     * @return Biome at location
     */
    public final Biome getBiome(Location l, GenerationPhase phase) {
        return getBiome(l.getBlockX(), l.getBlockZ(), phase);
    }

    /**
     * Get the Biome at a pair of coordinates
     * @param x X coordinate
     * @param z Z coordinate
     * @return Biome at coordinates
     * This implementation defaults to the POST_GEN GenerationPhase.
     * @see BiomeContainer#getBiome(int, int, GenerationPhase)
     */
    public Biome getBiome(int x, int z) { // Not final, so people can override and change default GenerationPhase
        return getBiome(x, z, GenerationPhase.POST_GEN);
    }

    /**
     * Get the Biome at a location
     * @param l Location at which to get Biome
     * @return Biome at location
     * This implementation defaults to the POST_GEN GenerationPhase.
     * @see BiomeContainer#getBiome(int, int, GenerationPhase)
     */
    public final Biome getBiome(Location l) {
        return getBiome(l.getBlockX(), l.getBlockZ());
    }

    /**
     * Get the World this BiomeContainer operates in
     * @return World assigned to BiomeContainer
     */
    public World getWorld() {
        return world;
    }
}
