package org.joeyb.undercarriage.core.utils;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * {@code Iterables} provides start helper methods for working with {@link Iterable}s.
 */
public class Iterables {

    /**
     * Returns a parallel {@link Stream} for the given {@link Iterable}.
     *
     * @param iterable the iterable for which we are generating a stream
     * @param <T> the type of objects in the iterable
     */
    public static <T> Stream<T> parallelStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true);
    }

    /**
     * Returns a sequential (i.e. non-parallel) {@link Stream} for the given {@link Iterable}.
     *
     * @param iterable the iterable for which we are generating a stream
     * @param <T> the type of objects in the iterable
     */
    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private Iterables() { }
}
