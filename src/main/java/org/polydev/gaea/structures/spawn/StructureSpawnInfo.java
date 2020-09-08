package org.polydev.gaea.structures.spawn;

import org.bukkit.Location;
import org.polydev.gaea.structures.NMSStructure;

import java.util.Random;

public interface StructureSpawnInfo {
    Location getSpawnLocation(Location init, Random r);

    boolean isValidSpawn(NMSStructure s);
}
