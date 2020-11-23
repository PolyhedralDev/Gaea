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
            int xRad = getRadius(0);
            int yRad = getRadius(1);
            int zRad = getRadius(2);
            int originX = (chunkX << 4);
            int originZ = (chunkZ << 4);
            for(int x = -xRad - 1; x <= xRad + 1; x++) {
                for(int y = -yRad - 1; y <= yRad + 1; y++) {
                    for(int z = -zRad - 1; z <= zRad + 1; z++) {
                        Vector position = origin.clone().add(new Vector(x, y, z));
                        if(FastMath.floor((double) (position.getBlockX()) / 16) == chunkX && FastMath.floor((double) (position.getBlockZ()) / 16) == chunkZ && position.getY() >= 0) {
                            double eq = ellipseEquation(x, y, z, xRad, yRad, zRad);
                            if(eq <= 1 &&
                                    y >= -yRad - 1 + bottomCut && y <= yRad + 1 - topCut) {
                                data.carve(position.getBlockX() - originX, position.getBlockY(), position.getBlockZ() - originZ, CarvingData.CarvingType.CENTER);
                            } else if(eq <= 1.5) {
                                CarvingData.CarvingType type = CarvingData.CarvingType.WALL;
                                if(y <= -yRad - 1 + bottomCut) {
                                    type = CarvingData.CarvingType.BOTTOM;
                                } else if(y >= yRad + 1 - topCut) {
                                    type = CarvingData.CarvingType.TOP;
                                }
                                if(data.isCarved(position.getBlockX() - originX, position.getBlockY(), position.getBlockZ() - originZ))
                                    continue;
                                data.carve(position.getBlockX() - originX, position.getBlockY(), position.getBlockZ() - originZ, type);
                            }
                        }
                    }
                }
            }
        }
    }
}
