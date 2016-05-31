package org.joeyb.undercarriage.core.utils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import java.util.function.Supplier;

/**
 * {@code Suppliers} provides static helper methods for working with {@link Supplier} instances.
 */
public class Suppliers {

    /**
     * Returns a supplier which caches the instance retrieved during the first call to {@link Supplier#get()} and
     * returns that value on subsequent calls to {@link Supplier#get()}. See:
     * <a href="http://en.wikipedia.org/wiki/Memoization">memoization</a>
     *
     * <p>The returned supplier is thread-safe. The delegate's {@link Supplier#get()} method will be invoked at
     * most once.
     *
     * <p>If {@code delegate} is an instance created by an earlier call to {@code memoize}, it is returned directly.
     *
     * <p>This was taken from Guava and adapted to work with Java 8 {@link Supplier}s.
     *
     * @param delegate the underlying supplier that provides the value to be memoized
     * @param <T> the return type of the supplier
     */
    public static <T> Supplier<T> memoize(Supplier<T> delegate) {
        return delegate instanceof MemoizingSupplier
               ? delegate
               : new MemoizingSupplier<>(Preconditions.checkNotNull(delegate));
    }

    @VisibleForTesting
    static class MemoizingSupplier<T> implements Supplier<T> {

        final Supplier<T> delegate;

        transient volatile boolean initialized;

        // "value" does not need to be volatile; visibility piggy-backs on volatile read of "initialized".
        transient T value;

        MemoizingSupplier(Supplier<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public T get() {
            // A 2-field variant of Double Checked Locking.
            if (!initialized) {
                synchronized (this) {
                    if (!initialized) {
                        T t = delegate.get();
                        value = t;
                        initialized = true;
                        return t;
                    }
                }
            }
            return value;
        }
    }

    private Suppliers() { }
}
