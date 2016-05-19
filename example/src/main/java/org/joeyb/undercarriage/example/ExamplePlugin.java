package org.joeyb.undercarriage.example;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.plugins.Plugin;

import javax.inject.Inject;

public class ExamplePlugin implements Plugin<ExampleConfigSection> {

    private final ConfigContext<? extends ExampleConfigSection> configContext;

    @Inject
    public ExamplePlugin(ConfigContext<? extends ExampleConfigSection> configContext) {
        this.configContext = configContext;
    }

    @Override
    public ConfigContext<? extends ExampleConfigSection> configContext() {
        return configContext;
    }
}
