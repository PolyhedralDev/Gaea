package org.polydev.gaea.population;

import org.bukkit.generator.BlockPopulator;

public abstract class GaeaBlockPopulator extends BlockPopulator {
    private final PopulationManager manager;
    public GaeaBlockPopulator(PopulationManager manager) {
        this.manager = manager;
    }
}
