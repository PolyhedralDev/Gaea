package org.polydev.gaea.world.carving;


import net.jafama.FastMath;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.util.FastRandom;

import java.util.Random;

public class CaveCarver extends Carver {
    private final int chance;
    private final int maxRad;

    public CaveCarver(int chance, int minY, int maxY, int maxRad) {
        super(minY, maxY);
        this.chance = chance;
        this.maxRad = maxRad;
    }

    @Override
    public Worm getWorm(long seed, Vector l) {
        return new CaveWorm(new FastRandom(seed).nextInt(90) + 30, new FastRandom(seed), l, maxRad);
    }


    @Override
    public boolean isChunkCarved(World w, int chunkX, int chunkZ, Random r) {
        return r.nextInt(100) < chance;
    }

    public static class CaveWorm extends Worm {
        private final Vector direction;
        private final int maxRad;
        private double runningRadius;

        public CaveWorm(int length, Random r, Vector origin, int maxRad) {
            super(length, r, origin);
            runningRadius = (r.nextDouble() / 2 + 0.5) * 4;
            this.maxRad = maxRad;
            direction = new Vector(r.nextDouble() - 0.5D, (r.nextDouble() - 0.5D) / 4, r.nextDouble() - 0.5D).normalize();
        }

        @Override
        public void step() {
            setRadius(new int[] {(int) runningRadius, (int) runningRadius, (int) runningRadius});
            runningRadius += (getRandom().nextDouble() - 0.5) / 8;
            runningRadius = FastMath.min(runningRadius, maxRad);
            direction.rotateAroundX(FastMath.toRadians(getRandom().nextDouble() * 2));
            direction.rotateAroundY(FastMath.toRadians(getRandom().nextDouble() * 6));
            direction.rotateAroundZ(FastMath.toRadians(getRandom().nextDouble() * 2));
            getRunning().add(direction);
        }
    }
}
