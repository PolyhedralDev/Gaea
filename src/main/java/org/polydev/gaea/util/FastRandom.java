package org.polydev.gaea.util;

import java.util.Random;
import java.util.SplittableRandom;
import org.apache.commons.rng.core.source64.XoRoShiRo128PlusPlus;

public class FastRandom extends Random {

    private final XoRoShiRo128PlusPlus random;

    public FastRandom() {
        SplittableRandom randomseed = new SplittableRandom();
        this.random = new XoRoShiRo128PlusPlus(randomseed.nextLong(), randomseed.nextLong());
    }

    public FastRandom(long seed) {
        SplittableRandom randomseed = new SplittableRandom(seed);
        this.random = new XoRoShiRo128PlusPlus(randomseed.nextLong(), randomseed.nextLong());
    }
    
    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public float nextFloat() {
        return (float) random.nextDouble();
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }
}