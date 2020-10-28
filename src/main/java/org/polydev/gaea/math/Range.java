package org.polydev.gaea.math;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Random;

/**
 * A range of numbers that can be iterated on
 */
public class Range implements Iterable<Integer> {
    private int min;
    private int max;

    /**
     * Instantiates a Range with a maximum and minimum value.
     * @param min Minimum value
     * @param max Maximum value
     */
    public Range(int min, int max) {
        if(min > max) throw new IllegalArgumentException("Minimum must not be grater than maximum!");
        this.max = max;
        this.min = min;
    }

    /**
     * Checks if a number is within range
     * @param test Number to test
     * @return Whether number is within this range.
     */
    public boolean isInRange(double test) {
        return test >= min && test < max;
    }

    /**
     * Get the maximum value
     * @return Max value
     */
    public int getMax() {
        return max;
    }

    /**
     * Set the maximum value
     * @param max New maximum value
     * @return This Range object
     */
    public Range setMax(int max) {
        this.max = max;
        return this;
    }

    /**
     * Get the minimum value
     * @return Min value
     */
    public int getMin() {
        return min;
    }

    /**
     * Set the minimum value
     * @param min New minimum value
     * @return This Range object
     */
    public Range setMin(int min) {
        this.min = min;
        return this;
    }

    /**
     * Calculate the range (max-min)
     * @return Range
     */
    public int getRange() {
        return max - min;
    }

    /**
     * Multiply both components by a value
     * @param mult Value to multiply by
     * @return This Range object
     */
    public Range multiply(int mult) {
        min *= mult;
        max *= mult;
        return this;
    }

    /**
     * Get a new Range, with max and min reflected over a point.
     * @param pt Point to reflect over
     * @return New reflected Range
     */
    public Range reflect(int pt) {
        return new Range(2 * pt - this.getMax(), 2 * pt - this.getMin());
    }

    /**
     * Get a random value from this Range with the provided Random instance.
     * @param r Random instance to use
     * @return Random value within this Range. (min = inclusive, max = exclusive)
     */
    public int get(Random r) {
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Get a new Range containing the intersection of this Range and another.
     * @param other Other Range
     * @return Range containing the intersection, null if none exists.
     */
    public Range intersects(Range other) {
        try {
            return new Range(Math.max(this.getMin(), other.getMin()), Math.min(this.getMax(), other.getMax()));
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Add a value to the min and max components.
     * @param add Value to add
     * @return This Range object
     */
    public Range add(int add) {
        this.min += add;
        this.max += add;
        return this;
    }

    /**
     * Subtract a value from the min and max components.
     * @param sub Value to subtract
     * @return This Range object
     */
    public Range sub(int sub) {
        this.min -= sub;
        this.max -= sub;
        return this;
    }

    @Override
    public String toString() {
        return "Min: " + getMin() + ", Max:" + getMax();
    }

    @Override
    public int hashCode() {
        return min * 31 + max;
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Range)) return false;
        Range other = (Range) obj;
        return other.getMin() == this.getMin() && other.getMax() == this.getMax();
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new RangeIterator(this);
    }

    private static class RangeIterator implements Iterator<Integer> {
        private final Range m;
        private Integer current;

        public RangeIterator(Range m) {
            this.m = m;
            current = m.getMin();
        }

        @Override
        public boolean hasNext() {
            return current < m.getMax();
        }

        @Override
        public Integer next() {
            current++;
            return current - 1;
        }
    }
}

