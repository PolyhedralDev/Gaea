package org.polydev.gaea.biome;

import org.polydev.gaea.structures.features.Feature;

import java.util.List;

/**
 * Interface to be implemented by a custom generator's Biome enum.<br>
 * Represents a custom biome, and contains methods to retrieve information about each type.
 */
public interface Biome {

    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return Biome - The Vanilla biome.
     */
    org.bukkit.block.Biome getVanillaBiome();

    /**
     * Gets the BiomeTerrain instance used to generate the biome.
     *
     * @return BiomeTerrain - The terrain generation instance.
     */
    Generator getGenerator();

    /**
     * Gets a list of Structure Features to be applied to all structures in the biome.
     *
     * @return List&lt;Feature&gt; - The list of Features.
     */
    List<Feature> getStructureFeatures();

    /**
     * Returns the Decorator instance containing information about the population in the biome.
     *
     * @return Decorator - the Decorator instance.
     */
    Decorator getDecorator();
}
