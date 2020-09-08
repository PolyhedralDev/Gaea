package org.polydev.gaea.world;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;

public enum Fauna {
    TALL_GRASS(true, Material.TALL_GRASS),
    TALL_FERN(true, Material.LARGE_FERN),
    GRASS(false, Material.GRASS),
    FERN(false, Material.FERN),
    AZURE_BLUET(false, Material.AZURE_BLUET),
    LILY_OF_THE_VALLEY(false, Material.LILY_OF_THE_VALLEY),
    BLUE_ORCHID(false, Material.BLUE_ORCHID),
    POPPY(false, Material.POPPY),
    DANDELION(false, Material.DANDELION),
    WITHER_ROSE(false, Material.WITHER_ROSE);

    private final List<BlockData> data = new ArrayList<>();


    Fauna(boolean tall, Material type) {
        if(tall) {
            data.add(Bukkit.createBlockData(type));
            data.add(Bukkit.createBlockData(type, "[half=upper]"));
        } else data.add(type.createBlockData());
    }

    public static Block getHighestValidSpawnAt(Chunk chunk, int x, int z) {
        int y;
        for(y = chunk.getWorld().getMaxHeight() - 1; (chunk.getBlock(x, y, z).getType() != Material.GRASS_BLOCK) && y > 0; y--)
            ;
        if(y <= 0) return null;
        return chunk.getBlock(x, y, z);
    }

    public boolean plant(Location l) {
        for(int i = 1; i < data.size() + 1; i++) {
            if(! l.clone().add(0, i, 0).getBlock().isEmpty()) return false;
        }
        for(int i = 1; i < data.size() + 1; i++) {
            l.clone().add(0, i, 0).getBlock().setBlockData(data.get(i - 1), false);
        }
        return true;
    }
}
