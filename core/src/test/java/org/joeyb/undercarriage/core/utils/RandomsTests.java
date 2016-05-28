package org.joeyb.undercarriage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.joeyb.undercarriage.core.utils.UtilsTestHelpers.assertClassOnlyHasPrivateConstructor;

import org.junit.Test;

public class RandomsTests {

    @Test
    public void randomsOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Randoms.class);
    }

    @Test
    public void randIntReturnsValueInRangeForValidInputs() {
        final int min = -100;
        final int max = 100;

        int rand = Randoms.randInt(min, max);

        assertThat(rand)
                .isGreaterThanOrEqualTo(min)
                .isLessThanOrEqualTo(max);
    }

    @Test
    public void randIntThrowsIfMinGreaterThanMax() {
        final int min = 100;
        final int max = 0;

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Randoms.randInt(min, max));
    }
}
