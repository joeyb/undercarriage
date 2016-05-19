package org.joeyb.undercarriage.core.plugins;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PluginPriorityComparatorTests {

    @Test
    public void compareReturnsNegativeWhenLeftOperandIsHigherPriority() {
        PluginPriorityComparator comparator = new PluginPriorityComparator();

        int result = comparator.compare(new HighPriorityPlugin(), new LowPriorityPlugin());

        assertThat(result).isLessThan(0);
    }

    @Test
    public void compareReturnsPositiveWhenLeftOperandIsLowerPriority() {
        PluginPriorityComparator comparator = new PluginPriorityComparator();

        int result = comparator.compare(new LowPriorityPlugin(), new HighPriorityPlugin());

        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void compareReturnsNonZeroForDifferentClassesWithSamePriority() {
        PluginPriorityComparator comparator = new PluginPriorityComparator();

        int result = comparator.compare(new HighPriorityPlugin(), new HighPriorityPlugin2());

        assertThat(result).isNotEqualTo(0);
    }

    private static class HighPriorityPlugin extends MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.HIGH;
        }
    }

    private static class HighPriorityPlugin2 extends HighPriorityPlugin {

    }

    private static class LowPriorityPlugin extends MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.LOW;
        }
    }
}
