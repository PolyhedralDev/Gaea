package org.polydev.gaea.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class WorldUtil {
    public static int getHighestValidSpawnAt(Chunk chunk, int x, int z) {
        int y;
        for(y = chunk.getWorld().getMaxHeight() - 1; (chunk.getBlock(x, y, z).getType() != Material.GRASS_BLOCK
                && chunk.getBlock(x, y, z).getType() != Material.GRAVEL
                && chunk.getBlock(x, y, z).getType() != Material.PODZOL
                && chunk.getBlock(x, y, z).getType() != Material.END_STONE
                && chunk.getBlock(x, y, z).getType() != Material.DIRT
                && chunk.getBlock(x, y, z).getType() != Material.STONE
                && chunk.getBlock(x, y, z).getType() != Material.SAND
                && chunk.getBlock(x, y, z).getType() != Material.COARSE_DIRT) && y > 0; y--)
            ;
        return y;
    }

    public static int getHighestBlockAt(Chunk chunk, int x, int z) {
        int y;
        for(y = chunk.getWorld().getMaxHeight() - 1; (chunk.getBlock(x, y, z).getType().isAir()) && y > 0; y--) ;
        return y;
    }

    public static List<Location> getLocationListBetween(Location loc1, Location loc2) {
        int lowX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int lowY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int lowZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        List<Location> locs = new ArrayList<>();
        for(int x = 0; x <= Math.abs(loc1.getBlockX() - loc2.getBlockX()); x++) {
            for(int y = 0; y <= Math.abs(loc1.getBlockY() - loc2.getBlockY()); y++) {
                for(int z = 0; z <= Math.abs(loc1.getBlockZ() - loc2.getBlockZ()); z++) {
                    locs.add(new Location(loc1.getWorld(), (double) lowX + x, (double) lowY + y, (double) lowZ + z));
                }
            }
        }
        return locs;
    }
}
