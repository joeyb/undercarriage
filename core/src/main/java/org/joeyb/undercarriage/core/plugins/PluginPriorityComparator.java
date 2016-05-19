package org.joeyb.undercarriage.core.plugins;

import java.io.Serializable;
import java.util.Comparator;

/**
 * {@code PluginPriorityComparator} is an implementation of {@link Comparator} for ordering {@link Plugin}s based on
 * the priority given via {@link Plugin#priority()}.
 */
public class PluginPriorityComparator implements Comparator<Plugin<?>>, Serializable {

    /**
     * Compares two plugins based on priority. A plugin with a higher priority will be defined as "less than" so that it
     * is sorted earlier in a sorted collection. If two plugins have the same priority, then class name is used as the
     * fall-back sort criteria in order to maintain predictability.
     */
    @Override
    public int compare(Plugin<?> o1, Plugin<?> o2) {
        final int priorityDiff = o2.priority().priority - o1.priority().priority;

        return priorityDiff != 0 ? priorityDiff : o1.getClass().getName().compareTo(o2.getClass().getName());
    }
}
