package org.joeyb.undercarriage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.joeyb.undercarriage.core.utils.UtilsTestHelpers.assertClassOnlyHasPrivateConstructor;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public class SuppliersTests {

    @Test
    public void suppliersOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Suppliers.class);
    }

    @Test
    public void memoizedSupplierExecutesDelegateGetOnceAndAlwaysReturnsSameValue() {
        CountDownLatch supplierLatch = new CountDownLatch(1);

        Supplier<Integer> explodingSupplier =
                () -> {
                    if (supplierLatch.getCount() == 0) {
                        throw new IllegalStateException("Value can only be supplied once.");
                    }

                    supplierLatch.countDown();

                    return Randoms.randInt(1, 10000);
                };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(explodingSupplier);

        int value1 = memoizedSupplier.get();
        int value2 = memoizedSupplier.get();
        int value3 = memoizedSupplier.get();

        assertThat(value1)
                .isEqualTo(value2)
                .isEqualTo(value3);
    }

    @Test
    public void memoizePassesThroughExistingMemoizedSupplierUnchanged() {
        CountDownLatch supplierLatch = new CountDownLatch(1);

        Supplier<Integer> explodingSupplier =
                () -> {
                    if (supplierLatch.getCount() == 0) {
                        throw new IllegalStateException("Value can only be supplied once.");
                    }

                    supplierLatch.countDown();

                    return Randoms.randInt(1, 10000);
                };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(explodingSupplier);

        int value1 = memoizedSupplier.get();

        Supplier<Integer> rememoizedSupplier = Suppliers.memoize(memoizedSupplier);

        assertThat(memoizedSupplier).isEqualTo(rememoizedSupplier);

        int value2 = rememoizedSupplier.get();

        assertThat(value1).isEqualTo(value2);
    }
}
