package org.joeyb.undercarriage.core.plugins;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

public class PluginPriorityComparatorTests {

    @Test
    public void compareReturnsNegativeWhenLeftOperandIsHigherPriority() {
        PluginPriorityComparator comparator = new PluginPriorityComparator();

        int result = comparator.compare(
                mock(HighPriorityPlugin.class, CALLS_REAL_METHODS),
                mock(LowPriorityPlugin.class, CALLS_REAL_METHODS));

        assertThat(result).isLessThan(0);
    }

    @Test
    public void compareReturnsPositiveWhenLeftOperandIsLowerPriority() {
        PluginPriorityComparator comparator = new PluginPriorityComparator();

        int result = comparator.compare(
                mock(LowPriorityPlugin.class, CALLS_REAL_METHODS),
                mock(HighPriorityPlugin.class, CALLS_REAL_METHODS));

        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void compareReturnsNonZeroForDifferentClassesWithSamePriority() {
        PluginPriorityComparator comparator = new PluginPriorityComparator();

        int result = comparator.compare(
                mock(HighPriorityPlugin.class, CALLS_REAL_METHODS),
                mock(HighPriorityPlugin2.class, CALLS_REAL_METHODS));

        assertThat(result).isNotEqualTo(0);
    }

    private static abstract class HighPriorityPlugin implements MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.HIGH;
        }
    }

    private static abstract class HighPriorityPlugin2 extends HighPriorityPlugin {

    }

    private static abstract class LowPriorityPlugin implements MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.LOW;
        }
    }
}
