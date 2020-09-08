package org.polydev.gaea.math;

public interface ChunkInterpolator {
    double getNoise(byte x, byte z);
    double getNoise(byte x, int y, byte z);
}
