package org.polydev.gaea.population;

import java.util.HashSet;
import java.util.UUID;

public class AsyncPopulationReturn {
    private final HashSet<BlockCoordinate> changeList;
    private final int populatorId;
    private final UUID worldID;
    private final int chunkX;
    private final int chunkZ;

    public AsyncPopulationReturn(HashSet<BlockCoordinate> changeList, int populatorId, UUID worldID, int chunkX, int chunkZ) {
        this.changeList = changeList;
        this.populatorId = populatorId;
        this.worldID = worldID;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public HashSet<BlockCoordinate> getChangeList() {
        return changeList;
    }

    public int getPopulatorId() {
        return populatorId;
    }

    public UUID getWorldID() {
        return worldID;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }
}
