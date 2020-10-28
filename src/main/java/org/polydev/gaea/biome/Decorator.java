package org.polydev.gaea.biome;

import org.bukkit.World;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.structures.Structure;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Flora;

import java.util.HashMap;
import java.util.Map;

public interface Decorator {
    ProbabilityCollection<Tree> getTrees();

    int getTreeDensity();

    boolean overrideStructureChance();

    org.bukkit.block.Biome getVanillaBiome();

    ProbabilityCollection<Flora> getFlora();

    int getFloraChance();
}
