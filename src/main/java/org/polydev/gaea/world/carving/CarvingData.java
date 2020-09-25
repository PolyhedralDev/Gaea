package org.polydev.gaea.world.carving;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarvingData {
    private final int chunkX;
    private final int chunkZ;
    Map<Vector, CarvingType> carvedBlocks = new HashMap<>();

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

    public void carve(int x, int y, int z, CarvingType type) {
        isInRange(x, y, z);
        carvedBlocks.put(new Vector(x, y, z), type);
    }

    private void isInRange(int x, int y, int z) {
        if(x > 15 || z > 15 || y > 255 || x < 0 || z < 0 || y < 0) throw new IllegalArgumentException("Value out of range! " + x + ", " + y + ", " + z);
    }

    public Map<Vector, CarvingType> getCarvedBlocks() {
        return carvedBlocks;
    }

    public boolean isCarved(int x, int y, int z) {
        /*for(CarvedBlock b : carvedBlocks) {
            Vector v = b.getBlock();
            if(v.getBlockX() == x && v.getBlockY() == y && v.getBlockZ() == z) return true;
        }*/
        return carvedBlocks.containsKey(new Vector(x, y, z));
    }

    public ChunkData merge(ChunkData data, boolean doLava) {
        for(Vector  v : carvedBlocks.keySet()) {
            Material m = data.getType(v.getBlockX(), v.getBlockY(), v.getBlockZ());
            if(!m.equals(Material.BEDROCK) && m.isSolid()) data.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), (v.getBlockY() < 8 && doLava) ? Material.LAVA : Material.AIR);
        }
        return data;
    }
    public void merge(Chunk data, boolean doLava) {
        for(Vector  v : carvedBlocks.keySet()) {
            Material m = data.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getType();
            if(!m.equals(Material.BEDROCK) && m.isSolid()) data.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ()).setType((v.getBlockY() < 8 && doLava) ? Material.LAVA : Material.AIR);
        }
    }

    public enum CarvingType {
        CENTER, WALL, TOP, BOTTOM;
    }

}
