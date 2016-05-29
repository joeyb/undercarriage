package org.joeyb.undercarriage.core.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import com.google.common.collect.ImmutableList;

import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Test;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.UUID;

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

    @Test
    public void directedGraphConstructorAddsAllNodesWithoutEdges() {
        final String node1 = UUID.randomUUID().toString();
        final String node2 = UUID.randomUUID().toString();

        final Collection<String> nodes = ImmutableList.of(node1, node2);

        TopologicalPluginSorter.DirectedGraph<String> graph = new TopologicalPluginSorter.DirectedGraph<>(nodes);

        graph.checkFromAndToExist(node1, node2);

        assertThat(graph.hasEdges()).isEqualTo(false);

        assertThat(graph.getEdges(node1))
                .isNotNull()
                .isEmpty();

        assertThat(graph.getEdges(node2))
                .isNotNull()
                .isEmpty();

        assertThat(graph.getNodesWithEdges())
                .isNotNull()
                .isEmpty();

        assertThat(graph.hasIncomingEdges(node1)).isEqualTo(false);
        assertThat(graph.hasIncomingEdges(node2)).isEqualTo(false);
    }

    @Test
    public void directedGraphCheckFromAndToExistThrowsIfEitherDoesNotExist() {
        final String node1 = UUID.randomUUID().toString();

        final Collection<String> nodes = ImmutableList.of(node1);

        TopologicalPluginSorter.DirectedGraph<String> graph = new TopologicalPluginSorter.DirectedGraph<>(nodes);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> graph.checkFromAndToExist(node1, UUID.randomUUID().toString()));

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> graph.checkFromAndToExist(UUID.randomUUID().toString(), node1));
    }

    @Test
    public void directedGraphAddEdgeIsSuccessful() {
        final String node1 = UUID.randomUUID().toString();
        final String node2 = UUID.randomUUID().toString();

        final Collection<String> nodes = ImmutableList.of(node1, node2);

        TopologicalPluginSorter.DirectedGraph<String> graph = new TopologicalPluginSorter.DirectedGraph<>(nodes);

        graph.addEdge(node1, node2);

        assertThat(graph.hasEdges()).isEqualTo(true);

        assertThat(graph.getEdges(node1)).containsExactly(node2);

        assertThat(graph.getEdges(node2))
                .isNotNull()
                .isEmpty();

        assertThat(graph.getNodesWithEdges()).containsExactly(node1);

        assertThat(graph.hasIncomingEdges(node1)).isEqualTo(false);
        assertThat(graph.hasIncomingEdges(node2)).isEqualTo(true);
    }

    @Test
    public void directedGraphRemoveEdgeIsSuccessful() {
        final String node1 = UUID.randomUUID().toString();
        final String node2 = UUID.randomUUID().toString();

        final Collection<String> nodes = ImmutableList.of(node1, node2);

        TopologicalPluginSorter.DirectedGraph<String> graph = new TopologicalPluginSorter.DirectedGraph<>(nodes);

        graph.addEdge(node1, node2);
        graph.removeEdge(node1, node2);

        assertThat(graph.hasEdges()).isEqualTo(false);

        assertThat(graph.getEdges(node1))
                .isNotNull()
                .isEmpty();

        assertThat(graph.getEdges(node2))
                .isNotNull()
                .isEmpty();

        assertThat(graph.getNodesWithEdges())
                .isNotNull()
                .isEmpty();

        assertThat(graph.hasIncomingEdges(node1)).isEqualTo(false);
        assertThat(graph.hasIncomingEdges(node2)).isEqualTo(false);
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
