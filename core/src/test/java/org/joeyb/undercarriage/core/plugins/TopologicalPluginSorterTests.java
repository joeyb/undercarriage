package org.joeyb.undercarriage.core.plugins;

import com.google.common.collect.ImmutableList;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

        ParentPlugin parentPlugin = new ParentPlugin();
        ChildPlugin childPlugin = new ChildPlugin();

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(parentPlugin, childPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(parentPlugin, childPlugin);
    }

    @Test
    public void returnedOrderIsCorrectGivenChildParent() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        ParentPlugin parentPlugin = new ParentPlugin();
        ChildPlugin childPlugin = new ChildPlugin();

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(childPlugin, parentPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(parentPlugin, childPlugin);
    }

    @Test
    public void returnedOrderIsCorrectGivenHighParentNormalParent() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        HighPriorityParentPlugin highPriorityParentPlugin = new HighPriorityParentPlugin();
        ParentPlugin parentPlugin = new ParentPlugin();

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(highPriorityParentPlugin, parentPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(highPriorityParentPlugin, parentPlugin);
    }

    @Test
    public void returnedOrderIsCorrectGivenNormalParentHighParent() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        HighPriorityParentPlugin highPriorityParentPlugin = new HighPriorityParentPlugin();
        ParentPlugin parentPlugin = new ParentPlugin();

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(parentPlugin, highPriorityParentPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(highPriorityParentPlugin, parentPlugin);
    }

    @Test
    public void returnedOrderIsCorrectGivenComplexDependencyGraph() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        ParentPlugin parentPlugin = new ParentPlugin();
        ChildPlugin childPlugin = new ChildPlugin();
        GrandChildPlugin grandChildPlugin = new GrandChildPlugin();
        HighPriorityGrandChildPlugin highPriorityGrandChildPlugin = new HighPriorityGrandChildPlugin();

        Iterable<Plugin<? super ConfigSection>> sortedPlugins = pluginSorter.sort(
                ImmutableList.of(grandChildPlugin, highPriorityGrandChildPlugin, childPlugin, parentPlugin));

        assertThat(sortedPlugins)
                .isNotNull()
                .containsExactly(parentPlugin, childPlugin, highPriorityGrandChildPlugin, grandChildPlugin);
    }


    @Test
    public void sortThrowsForMissingDependency() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        ChildPlugin childPlugin = new ChildPlugin();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> pluginSorter.sort(ImmutableList.of(childPlugin)));
    }

    @Test
    public void sortThrowsForCycle() {
        TopologicalPluginSorter pluginSorter = new TopologicalPluginSorter();

        ParentPlugin parentPlugin = new ParentPlugin();
        CyclePlugin1 cyclePlugin1 = new CyclePlugin1();
        CyclePlugin2 cyclePlugin2 = new CyclePlugin2();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> pluginSorter.sort(ImmutableList.of(parentPlugin, cyclePlugin1, cyclePlugin2)));
    }

    private static class ParentPlugin extends MockPlugin {

    }

    private static class ChildPlugin extends MockPlugin {

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ParentPlugin.class);
        }
    }

    private static class GrandChildPlugin extends MockPlugin {

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ChildPlugin.class);
        }
    }

    private static class HighPriorityGrandChildPlugin extends MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.HIGH;
        }

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ChildPlugin.class);
        }
    }

    private static class CyclePlugin1 extends MockPlugin {

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ParentPlugin.class, CyclePlugin2.class);
        }
    }

    private static class CyclePlugin2 extends MockPlugin {

        @Override
        public Iterable<Class<? extends Plugin<?>>> dependencies() {
            return ImmutableList.of(ParentPlugin.class, CyclePlugin1.class);
        }
    }

    private static class HighPriorityParentPlugin extends MockPlugin {

        @Override
        public PluginPriority priority() {
            return PluginPriority.HIGH;
        }
    }
}
