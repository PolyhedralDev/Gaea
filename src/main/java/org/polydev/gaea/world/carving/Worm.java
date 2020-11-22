package org.polydev.gaea.world.carving;

import net.jafama.FastMath;
import org.bukkit.util.Vector;

import java.util.Random;

public abstract class Worm {
    private final Random r;
    private final Vector origin;
    private final Vector running;
    private final int length;
    private int topCut = 0;
    private int bottomCut = 0;
    private int[] radius = new int[] {0, 0, 0};

    public Worm(int length, Random r, Vector origin) {
        this.r = r;
        this.length = length;
        this.origin = origin;
        this.running = origin;
    }

    public void setBottomCut(int bottomCut) {
        this.bottomCut = bottomCut;
    }

    public void setTopCut(int topCut) {
        this.topCut = topCut;
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
        return new WormPoint(running, radius, topCut, bottomCut);
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
        private final int topCut;
        private final int bottomCut;
        private final int[] rad;

        public WormPoint(Vector origin, int[] rad, int topCut, int bottomCut) {
            this.origin = origin;
            this.rad = rad;
            this.topCut = topCut;
            this.bottomCut = bottomCut;
        }

        private static int getChunkCoordinate(int n) {
            if(n >= 0) return n % 16;
            else return 15 - (FastMath.abs(n % 16));
        }

        private static double ellipseEquation(int x, int y, int z, double xr, double yr, double zr) {
            return (FastMath.pow(x, 2) / FastMath.pow(xr + 0.5D, 2)) + (FastMath.pow(y, 2) / FastMath.pow(yr + 0.5D, 2)) + (FastMath.pow(z, 2) / FastMath.pow(zr + 0.5D, 2));
        }

        public Vector getOrigin() {
            return origin;
        }

        public int getRadius(int index) {
            return rad[index];
        }

        public void carve(CarvingData data, int chunkX, int chunkZ) {
            for(int x = - getRadius(0) - 1; x <= getRadius(0) + 1; x++) {
                for(int y = - getRadius(1) - 1; y <= getRadius(1) + 1; y++) {
                    for(int z = - getRadius(2) - 1; z <= getRadius(2) + 1; z++) {
                        Vector position = origin.clone().add(new Vector(x, y, z));
                        if(FastMath.floor((double) (position.getBlockX()) / 16) == chunkX && FastMath.floor((double) (position.getBlockZ()) / 16) == chunkZ && position.getY() >= 0) {
                            double eq = ellipseEquation(x, y, z, getRadius(0), getRadius(1), getRadius(2));
                            if(eq <= 1 &&
                                    y >= -getRadius(1) - 1 + bottomCut && y <= getRadius(1) + 1 - topCut) {
                                data.carve(position.getBlockX() - (chunkX << 4), position.getBlockY(), position.getBlockZ() - (chunkZ << 4), CarvingData.CarvingType.CENTER);
                            } else if(eq <= 1.5) {
                                CarvingData.CarvingType type = CarvingData.CarvingType.WALL;
                                if(y <= -getRadius(1) - 1 + bottomCut) {
                                    type = CarvingData.CarvingType.BOTTOM;
                                } else if(y >= getRadius(1) + 1 - topCut) {
                                    type = CarvingData.CarvingType.TOP;
                                }
                                if(data.isCarved(position.getBlockX() - (chunkX << 4), position.getBlockY(), position.getBlockZ() - (chunkZ << 4)))
                                    continue;
                                data.carve(position.getBlockX() - (chunkX << 4), position.getBlockY(), position.getBlockZ() - (chunkZ << 4), type);
                            }
                        }
                    }
                }
            }
        }
    }
}
