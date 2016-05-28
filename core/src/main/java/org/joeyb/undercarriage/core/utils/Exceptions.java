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
     * @param function the function that potentially throws a checked exception
     * @param <T> the return type of the given function
     * @return the return value of the given function
     */
    public static <T> T wrapChecked(Callable<T> function) {
        return wrapChecked(function, null);
    }

    /**
     * Wraps a function that potentially throws a checked exception, catches the exception if it occurs, and re-throws
     * it wrapped in a {@link RuntimeException}. If {@code errorMessage} is non-null then it is included as the message
     * on the exception.
     *
     * @param function the function that potentially throws a checked exception
     * @param errorMessage the message to associate with the new {@link RuntimeException}
     * @param <T> the return type of the given function
     * @return the return value of the given function
     */
    public static <T> T wrapChecked(Callable<T> function, String errorMessage) {
        try {
            return function.call();
        } catch (Exception e) {
            throw errorMessage == null
                  ? new RuntimeException(e)
                  : new RuntimeException(errorMessage, e);
        }
    }

    private Exceptions() { }
}
