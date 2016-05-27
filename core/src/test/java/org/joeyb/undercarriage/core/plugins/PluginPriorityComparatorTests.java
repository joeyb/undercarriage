package org.joeyb.undercarriage.core.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import org.junit.Test;

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

    private abstract static class HighPriorityPlugin implements MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.HIGH;
        }
    }

    private abstract static class HighPriorityPlugin2 extends HighPriorityPlugin {

    }

    private abstract static class LowPriorityPlugin implements MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.LOW;
        }
    }
}
