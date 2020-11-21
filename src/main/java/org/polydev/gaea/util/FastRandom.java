package org.polydev.gaea.util;

import java.util.Random;
import java.util.SplittableRandom;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.apache.commons.rng.core.source64.XoRoShiRo128PlusPlus;

public class FastRandom extends Random {

    private XoRoShiRo128PlusPlus random;

    public FastRandom() {
        super();
        SplittableRandom randomseed = new SplittableRandom();
        this.random = new XoRoShiRo128PlusPlus(randomseed.nextLong(), randomseed.nextLong());
    }

    public FastRandom(long seed) {
        super(seed);
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

    @Override
    public synchronized void setSeed(long seed) {
        SplittableRandom randomseed = new SplittableRandom(seed);
        this.random = new XoRoShiRo128PlusPlus(randomseed.nextLong(), randomseed.nextLong());
    }

    @Override
    protected int next(int bits) {
        return super.next(bits);
    }

    @Override
    public void nextBytes(byte[] bytes) {
        random.nextBytes(bytes);
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public long nextLong() {
        return random.nextLong();
    }

    @Override
    public synchronized double nextGaussian() {
        return super.nextGaussian();
    }
}