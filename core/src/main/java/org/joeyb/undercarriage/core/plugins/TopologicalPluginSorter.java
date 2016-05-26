package org.joeyb.undercarriage.core.plugins;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.joeyb.undercarriage.core.config.ConfigSection;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code TopologicalPluginSorter} is an implementation of {@link PluginSorter} that uses toposort in order to resolve
 * the plugin dependency graph. The plugins are sorted so that all dependencies are executed first, and the priority
 * returned from {@link Plugin#priority()} is used to sort plugins that would otherwise have effectively the same
 * location in the order.
 */
public class TopologicalPluginSorter implements PluginSorter {

    private static final PluginPriorityComparator PLUGIN_PRIORITY_COMPARATOR = new PluginPriorityComparator();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ConfigSection> Iterable<Plugin<? super T>> sort(Iterable<Plugin<? super T>> plugins) {
        final Collection<Plugin<? super T>> pluginsList = ImmutableList.copyOf(plugins).stream()
                .sorted(PLUGIN_PRIORITY_COMPARATOR)
                .collect(Collectors.toList());

        DirectedGraph<Plugin<? super T>> graph = createAndPopulateGraph(pluginsList);

        // Use Kahn's topological sort algorithm to resolve the dependency graph.
        // See https://en.wikipedia.org/wiki/Topological_sorting#Kahn.27s_algorithm for details on the algorithm.

        // Prepare storage for the final sorted list.
        Collection<Plugin<? super T>> sortedPlugins = new LinkedHashSet<>(pluginsList.size());

        // The top level of our sorted dependency list should be each of the plugins that have no dependencies.
        // As dependencies are resolved, this queue will be populated with plugins that have no remaining dependencies.
        Queue<Plugin<? super T>> dependencyFreePlugins = new LinkedList<>(pluginsList.stream()
                .filter(p -> !p.dependencies().iterator().hasNext())
                .collect(Collectors.toList()));

        while (dependencyFreePlugins.size() > 0) {
            final Plugin<? super T> p = dependencyFreePlugins.remove();

            sortedPlugins.add(p);

            for (final Plugin<? super T> dependentPlugin : graph.getEdges(p)) {
                graph.removeEdge(p, dependentPlugin);

                if (!graph.hasIncomingEdges(dependentPlugin)) {
                    dependencyFreePlugins.add(dependentPlugin);
                }
            }
        }

        // If the graph still has edges then there is at least one cycle.
        if (graph.hasEdges()) {
            throw new IllegalStateException(String.format(
                    "Dependency graph must not contain any cycles. Check the following plugins: %s",
                    String.join(
                            ", ",
                            graph.getNodesWithEdges().stream()
                                    .map(p -> p.getClass().getSimpleName())
                                    .collect(Collectors.toSet()))));
        }

        return sortedPlugins;
    }

    private static <T extends ConfigSection> DirectedGraph<Plugin<? super T>> createAndPopulateGraph(
            Collection<Plugin<? super T>> plugins) {

        final Map<Class<?>, Plugin<? super T>> classMap = plugins.stream()
                .collect(Collectors.toMap(Plugin::getClass, p -> p));

        final DirectedGraph<Plugin<? super T>> graph = new DirectedGraph<>(plugins);

        // For each plugin, create an edge for each of its dependencies (from the dependency to the plugin).
        for (final Plugin<? super T> p : plugins) {
            for (final Class<?> c : p.dependencies()) {
                Optional<Class<?>> dependencyKey = classMap.keySet().stream()
                        .filter(c::isAssignableFrom)
                        .findFirst();

                // Make sure the dependency is actually enabled.
                if (!dependencyKey.isPresent()) {
                    throw new IllegalStateException(
                            String.format("Plugin %s depends on plugin %s, but %s is not enabled.",
                                          p.getClass().getSimpleName(),
                                          c.getSimpleName(),
                                          c.getSimpleName()));
                }

                graph.addEdge(classMap.get(dependencyKey.get()), p);
            }
        }

        return graph;
    }

    private static class DirectedGraph<T> {

        private final Map<T, Set<T>> graph;

        DirectedGraph(Collection<T> nodes) {
            graph = new LinkedHashMap<>(nodes.size());

            for (final T node : nodes) {
                graph.put(node, new LinkedHashSet<>());
            }
        }

        void addEdge(T from, T to) {
            checkFromAndToExist(from, to);

            graph.get(from).add(to);
        }

        void checkFromAndToExist(T from, T to) {
            if (!graph.containsKey(from) || !graph.containsKey(to)) {
                throw new NoSuchElementException("Graph must contain both the from and to nodes of an edge.");
            }
        }

        Set<T> getEdges(T from) {
            return ImmutableSet.copyOf(graph.get(from));
        }

        Set<T> getNodesWithEdges() {
            return graph.keySet().stream().filter(node -> graph.get(node).size() > 0).collect(Collectors.toSet());
        }

        boolean hasEdges() {
            return graph.values().stream().anyMatch(edges -> edges.size() > 0);
        }

        boolean hasIncomingEdges(T node) {
            return graph.values().stream().anyMatch(edges -> edges.contains(node));
        }

        void removeEdge(T from, T to) {
            checkFromAndToExist(from, to);

            graph.get(from).remove(to);
        }
    }
}
