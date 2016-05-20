package org.joeyb.undercarriage.core.utils;

import com.google.common.base.Preconditions;

import java.util.concurrent.ThreadLocalRandom;

/**
 * {@code Randoms} provides static helper methods for generating random numbers. It uses {@link ThreadLocalRandom} as
 * its underlying random number generator.
 */
public class Randoms {

    /**
     * Returns a random int between {@code min} and {@code max}, inclusive.
     *
     * @param min the minimum value in the random number range
     * @param max the maximum value in the random number range
     * @throws IllegalArgumentException if min is greater than max
     */
    public static int randInt(int min, int max) {
        Preconditions.checkArgument(min <= max, "min must be less than or equal to max");

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
