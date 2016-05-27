package org.joeyb.undercarriage.core.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import com.google.common.collect.ImmutableList;

import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Test;

public class TopologicalPluginSorterTests {

    @Test
    public void returnsEmptyGivenEmpty() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(ImmutableList.of());

        assertThat(sortedPlugins)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    public void returnedOrderIsCorrectGivenParentChild() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        ParentPlugin parentPlugin = mock(ParentPlugin.class, CALLS_REAL_METHODS);
        ChildPlugin childPlugin = mock(ChildPlugin.class, CALLS_REAL_METHODS);

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(parentPlugin, childPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(parentPlugin, childPlugin);
    }

    @Test
    public void returnedOrderIsCorrectGivenChildParent() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        ParentPlugin parentPlugin = mock(ParentPlugin.class, CALLS_REAL_METHODS);
        ChildPlugin childPlugin = mock(ChildPlugin.class, CALLS_REAL_METHODS);

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(childPlugin, parentPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(parentPlugin, childPlugin);
    }

    @Test
    public void returnedOrderIsCorrectGivenHighParentNormalParent() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        HighPriorityParentPlugin highPriorityParentPlugin = mock(HighPriorityParentPlugin.class, CALLS_REAL_METHODS);
        ParentPlugin parentPlugin = mock(ParentPlugin.class, CALLS_REAL_METHODS);

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(highPriorityParentPlugin, parentPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(highPriorityParentPlugin, parentPlugin);
    }

    @Test
    public void returnedOrderIsCorrectGivenNormalParentHighParent() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        HighPriorityParentPlugin highPriorityParentPlugin = mock(HighPriorityParentPlugin.class, CALLS_REAL_METHODS);
        ParentPlugin parentPlugin = mock(ParentPlugin.class, CALLS_REAL_METHODS);

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(parentPlugin, highPriorityParentPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(highPriorityParentPlugin, parentPlugin);
    }

    @Test
    public void returnedOrderIsCorrectGivenComplexDependencyGraph() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        ParentPlugin parentPlugin = mock(ParentPlugin.class, CALLS_REAL_METHODS);
        ChildPlugin childPlugin = mock(ChildPlugin.class, CALLS_REAL_METHODS);
        GrandChildPlugin grandChildPlugin = mock(GrandChildPlugin.class, CALLS_REAL_METHODS);
        HighPriorityGrandChildPlugin highPriorityGrandChildPlugin =
                mock(HighPriorityGrandChildPlugin.class, CALLS_REAL_METHODS);

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(grandChildPlugin, highPriorityGrandChildPlugin, childPlugin, parentPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(parentPlugin, childPlugin, highPriorityGrandChildPlugin, grandChildPlugin);
    }


    @Test
    public void sortThrowsForMissingDependency() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        ChildPlugin childPlugin = mock(ChildPlugin.class, CALLS_REAL_METHODS);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> pluginSorter.sort(ImmutableList.of(childPlugin)));
    }

    @Test
    public void sortThrowsForCycle() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        ParentPlugin parentPlugin = mock(ParentPlugin.class, CALLS_REAL_METHODS);
        CyclePlugin1 cyclePlugin1 = mock(CyclePlugin1.class, CALLS_REAL_METHODS);
        CyclePlugin2 cyclePlugin2 = mock(CyclePlugin2.class, CALLS_REAL_METHODS);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> pluginSorter.sort(ImmutableList.of(parentPlugin, cyclePlugin1, cyclePlugin2)));
    }

    private abstract static class ParentPlugin implements MockPlugin {

    }

    private abstract static class ChildPlugin implements MockPlugin {

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ParentPlugin.class);
        }
    }

    private abstract static class GrandChildPlugin implements MockPlugin {

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ChildPlugin.class);
        }
    }

    private abstract static class HighPriorityGrandChildPlugin implements MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.HIGH;
        }

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ChildPlugin.class);
        }
    }

    private abstract static class CyclePlugin1 implements MockPlugin {

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ParentPlugin.class, CyclePlugin2.class);
        }
    }

    private abstract static class CyclePlugin2 implements MockPlugin {

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ParentPlugin.class, CyclePlugin1.class);
        }
    }

    private abstract static class HighPriorityParentPlugin implements MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.HIGH;
        }
    }
}
