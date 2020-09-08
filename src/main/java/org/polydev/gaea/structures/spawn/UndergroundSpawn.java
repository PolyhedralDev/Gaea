package org.polydev.gaea.structures.spawn;

import org.bukkit.Location;
import org.polydev.gaea.structures.NMSStructure;

import java.util.Random;

public class UndergroundSpawn implements StructureSpawnInfo {
    private final int depth;

    public UndergroundSpawn(int depth) {
        this.depth = depth;
    }

    @Override
    public Location getSpawnLocation(Location init, Random r) {
        return init.clone().subtract(0, - r.nextInt(depth / 2) - ((double) depth / 2), 0);
    }

    @Override
    public boolean isValidSpawn(NMSStructure s) {
        Location[] bounds = s.getBoundingLocations();
        return (! bounds[0].getBlock().isPassable())
                && (! bounds[1].getBlock().isPassable())
                && (! new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[0].getZ()).getBlock().isPassable())
                && (! new Location(bounds[0].getWorld(), bounds[0].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable())
                && (! new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[0].getY(), bounds[1].getZ()).getBlock().isPassable())
                && (! new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[1].getY(), bounds[0].getZ()).getBlock().isPassable())
                && (! new Location(bounds[0].getWorld(), bounds[0].getX(), bounds[1].getY(), bounds[1].getZ()).getBlock().isPassable())
                && (! new Location(bounds[0].getWorld(), bounds[1].getX(), bounds[1].getY(), bounds[1].getZ()).getBlock().isPassable());
    }
}