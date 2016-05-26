package org.joeyb.undercarriage.example;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.plugins.PluginBase;

import javax.inject.Inject;

public class ExamplePlugin extends PluginBase<ExampleConfigSection> {

    @Inject
    public ExamplePlugin(ConfigContext<? extends ExampleConfigSection> configContext) {
        super(configContext);
    }
}
