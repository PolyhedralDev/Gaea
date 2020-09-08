package org.polydev.gaea.structures;

import org.bukkit.Location;
import org.polydev.gaea.structures.features.Feature;
import org.polydev.gaea.structures.spawn.StructureSpawnInfo;

import java.util.List;
import java.util.Random;

public interface Structure {
    NMSStructure getInstance(Location origin, Random r);

    List<Feature> getFeatures();

    StructureSpawnInfo getSpawnInfo();
}
