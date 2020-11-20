package org.polydev.gaea.world.carving;

import org.polydev.gaea.util.FastRandom;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.MathUtil;

import java.util.Random;

public abstract class Carver {
    private final int minY;
    private final int maxY;

    public Carver(int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
    }

    public CarvingData carve(int chunkX, int chunkZ, World w) {
        CarvingData data = new CarvingData(chunkX, chunkZ);
        for(int x = chunkX - 4; x <= chunkX + 4; x++) {
            for(int z = chunkZ - 4; z <= chunkZ + 4; z++) {
                if(isChunkCarved(w, x, z, new FastRandom(MathUtil.hashToLong(this.getClass().getName() + "_" + x + "&" + z)))) {
                    long seed = MathUtil.getCarverChunkSeed(x, z, w.getSeed());
                    Random r = new FastRandom(seed);
                    Worm carving = getWorm(seed, new Vector((x << 4) + r.nextInt(16), r.nextInt(maxY - minY + 1) + minY, (z << 4) + r.nextInt(16)));
                    for(int i = 0; i < carving.getLength(); i++) {
                        carving.step();
                        if(carving.getRunning().clone().setY(0).distance(carving.getOrigin().clone().setY(0)) > 64)
                            break;
                        carving.getPoint().carve(data, chunkX, chunkZ);
                    }
                }
            }
        }
        return data;
    }

    public abstract Worm getWorm(long seed, Vector l);

    public abstract boolean isChunkCarved(World w, int chunkX, int chunkZ, Random r);
}
