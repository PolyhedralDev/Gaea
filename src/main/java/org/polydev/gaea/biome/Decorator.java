package org.polydev.gaea.biome;

import org.bukkit.World;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.structures.Structure;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Fauna;

import java.util.HashMap;
import java.util.Map;

public abstract class Decorator {
    private final Map<String, ProbabilityCollection<Structure>> worldStructureProb = new HashMap<>();

    public ProbabilityCollection<Structure> getStructures(World w) {
        return worldStructureProb.containsKey(w.getName()) ? worldStructureProb.get(w.getName()) : new ProbabilityCollection<>();
    }

    public abstract ProbabilityCollection<Tree> getTrees();

    public abstract int getTreeDensity();

    public abstract boolean overrideStructureChance();

    public abstract org.bukkit.block.Biome getVanillaBiome();

    public abstract ProbabilityCollection<Fauna> getFauna();

    public abstract int getFaunaChance();

    /**
     * Sets the structures that are to be generated in a world. Intended to be invoked during subclass instantiation, or by a configuration class.
     * @param structures ProbabilityCollection of Structures
     * @param w World in which the structures are to be generated.
     */
    public void setStructures(ProbabilityCollection<Structure> structures, World w) {
        worldStructureProb.put(w.getName(), structures);
    }

    /**
     * Sets the structures that are to be generated in a world. Intended to be invoked during subclass instantiation, or by a configuration class.
     * @param structures ProbabilityCollection of Structures
     * @param w Name of world in which the structures are to be generated.
     */
    public void setStructures(ProbabilityCollection<Structure> structures, String w) {
        worldStructureProb.put(w, structures);
    }
}
