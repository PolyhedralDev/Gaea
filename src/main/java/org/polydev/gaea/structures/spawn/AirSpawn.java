package org.polydev.gaea.structures.spawn;

import org.bukkit.Location;
import org.polydev.gaea.structures.NMSStructure;

import java.util.Random;

public class AirSpawn implements StructureSpawnInfo {
    private final int height;
    private final int deviation;

    public AirSpawn(int height, int deviation) {
        this.height = height;
        this.deviation = deviation;
    }

    @Override
    public Location getSpawnLocation(Location init, Random r) {
        return new Location(init.getWorld(), init.getX(), height + ((deviation <= 0) ? 0 : r.nextInt(deviation) - ((double) deviation / 2)), init.getZ());
    }

    @Override
    public boolean isValidSpawn(NMSStructure s) {
        return true;
    }
}
