package org.polydev.gaea.population;

import org.bukkit.block.data.BlockData;

import java.util.UUID;

public class BlockCoordinate {
    private final int x;
    private final int y;
    private final int z;

    private final BlockData blockData;

    public BlockCoordinate(int x, int y, int z, BlockData blockData) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.blockData = blockData;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockData getBlockData() {
        return blockData;
    }
}
