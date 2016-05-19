package org.joeyb.undercarriage.core.utils;

import java.util.concurrent.Callable;

/**
 * {@code Exceptions} contains static helper methods for working with exceptions.
 */
public class Exceptions {

    /**
     * Wraps a function that potentially throws a checked exception, catches the exception if it occurs, and re-throws
     * it wrapped in a {@link RuntimeException}.
     *
     * @param f the function that potentially throws a checked exception
     * @param <T> the return type of the given function
     * @return the return value of the given function
     */
    public static <T> T wrapChecked(Callable<T> f) {
        try {
            return f.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Exceptions() { }
}
