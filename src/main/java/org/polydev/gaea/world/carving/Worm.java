package org.polydev.gaea.world.carving;

import org.bukkit.util.Vector;

import java.util.Random;

public abstract class Worm {
    private final Random r;
    private final Vector origin;
    private final Vector running;
    private final int length;
    private int[] radius = new int[] {0, 0, 0};
    public Worm(int length, Random r, Vector origin) {
        this.r = r;
        this.length = length;
        this.origin = origin;
        this.running = origin;
    }

    public Vector getOrigin() {
        return origin;
    }

    public int getLength() {
        return length;
    }

    public Vector getRunning() {
        return running;
    }

    public WormPoint getPoint() {
        return new WormPoint(running, radius);
    }

    public int[] getRadius() {
        return radius;
    }

    public void setRadius(int[] radius) {
        this.radius = radius;
    }

    public Random getRandom() {
        return r;
    }

    public abstract void step();

    public static class WormPoint {
        private final Vector origin;
        private final int[] rad;

        public WormPoint(Vector origin, int[] rad) {
            this.origin = origin;
            this.rad = rad;
        }

        private static int getChunkCoordinate(int n) {
            if(n >= 0) return n % 16;
            else return 15-(Math.abs(n % 16));
        }

        private static double ellipseEquation(int x, int y, int z, int xr, int yr, int zr) {
            return (Math.pow(x, 2)/Math.pow(xr + 0.5D, 2)) + (Math.pow(y, 2)/Math.pow(yr + 0.5D, 2)) + (Math.pow(z, 2)/Math.pow(zr + 0.5D, 2));
        }

        public Vector getOrigin() {
            return origin;
        }

        public int getRadius(int index) {
            return rad[index];
        }

        public void carve(CarvingData data, int chunkX, int chunkZ) {
            if(Math.abs(origin.getBlockX()/16 - chunkX) > 1 && Math.abs(origin.getBlockZ()/16 - chunkZ) > 1) return;
            for(int x = - getRadius(0); x <= getRadius(0); x++) {
                for(int y = - getRadius(1); y <= getRadius(1); y++) {
                    for(int z = - getRadius(2); z <= getRadius(2); z++) {
                        Vector position = origin.clone().add(new Vector(x, y, z));
                        if(ellipseEquation(x, y, z, getRadius(0), getRadius(1), getRadius(2)) <= 1) {
                            if(Math.floor((double)(position.getBlockX())/16) == chunkX && Math.floor((double)(position.getBlockZ())/16) == chunkZ && position.getY() >= 0) data.carve(position.getBlockX() - (chunkX*16), position.getBlockY(), position.getBlockZ() - (chunkZ*16));
                        }
                    }
                }
            }
        }
    }
}
