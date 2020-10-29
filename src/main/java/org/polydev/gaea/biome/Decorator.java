package org.polydev.gaea.biome;

import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Flora;

/**
 * Class containing information about how a biome should be decorated
 */
public interface Decorator {
    /**
     * Get the trees to generate in this biome.
     * @return ProbabilityCollection of trees
     */
    ProbabilityCollection<Tree> getTrees();

    /**
     * Get this biome's tree density
     * @return Density of trees in this biome
     */
    int getTreeDensity();

    /**
     * Get the Vanilla biome that represents this biome.
     * @return Vanilla biome
     */
    org.bukkit.block.Biome getVanillaBiome();

    /**
     * Get the Flora to generate in this Biome
     * @return ProbabilityCollection of Flors
     */
    ProbabilityCollection<Flora> getFlora();
}
