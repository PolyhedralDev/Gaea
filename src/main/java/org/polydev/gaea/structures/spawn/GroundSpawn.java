package org.polydev.gaea.structures.spawn;

import org.bukkit.Location;
import org.polydev.gaea.structures.NMSStructure;

import java.util.Random;

public class GroundSpawn implements StructureSpawnInfo {
    private final int offset;

    public GroundSpawn(int offset) {
        this.offset = offset;
    }

    @Override
    public Location getSpawnLocation(Location init, Random r) {
        return init.clone().add(0, offset, 0);
    }

    @Override
    public boolean isValidSpawn(NMSStructure s) {
        Location[] bounds = s.getBoundingLocations();
        return (! bounds[0].clone().subtract(0, offset + 1, 0).getBlock().isEmpty())
                && (! new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY() - (offset + 1), bounds[0].getZ()).getBlock().isEmpty())
                && (! new Location(bounds[0].getWorld(), bounds[0].getX(), bounds[0].getY() - (offset + 1), bounds[1].getZ()).getBlock().isEmpty())
                && (! new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY() - (offset + 1), bounds[1].getZ()).getBlock().isEmpty());
    }
}