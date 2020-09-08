package org.polydev.gaea.world.carving;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CarvingData {
    private final int chunkX;
    private final int chunkZ;
    List<Vector> carvedBlocks = new ArrayList<>();

    public CarvingData(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void carve(int x, int y, int z) {
        if(x > 15 || z > 15 || y > 255 || x < 0 || z < 0 || y < 0) throw new IllegalArgumentException("Value out of range! " + x + ", " + y + ", " + z);
        carvedBlocks.add(new Vector(x, y, z));
    }

    public List<Vector> getCarvedBlocks() {
        return carvedBlocks;
    }

    public ChunkData merge(ChunkData data, boolean doLava) {
        for(Vector v : carvedBlocks) {
            Material m = data.getType(v.getBlockX(), v.getBlockY(), v.getBlockZ());
            if(!m.equals(Material.BEDROCK) && m.isSolid()) data.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), (v.getBlockY() < 8 && doLava) ? Material.LAVA : Material.AIR);
        }
        return data;
    }
}
