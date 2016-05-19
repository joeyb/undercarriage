package org.joeyb.undercarriage.core.plugins;

/**
 * {@code PluginPriority} is used to specify the plugin execution order.
 */
public enum PluginPriority {

    LOW(0),
    NORMAL(50),
    HIGH(100);

    public final int priority;

    PluginPriority(int priority) {
        this.priority = priority;
    }
}
