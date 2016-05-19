package org.joeyb.undercarriage.example;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.joeyb.undercarriage.core.ApplicationBase;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.plugins.Plugin;
import org.joeyb.undercarriage.example.dagger.DaggerExampleApplicationComponent;

import javax.inject.Inject;

public class ExampleApplication extends ApplicationBase<ExampleConfig> {

    private final Iterable<Plugin<? super ExampleConfig>> enabledPlugins;

    @Inject
    public ExampleApplication(ConfigContext<ExampleConfig> configContext, ExamplePlugin examplePlugin) {
        super(configContext);

        this.enabledPlugins = ImmutableList.of(examplePlugin);
    }

    @Override
    protected Iterable<Plugin<? super ExampleConfig>> enabledPlugins() {
        return Iterables.concat(super.enabledPlugins(), enabledPlugins);
    }

    public static void main(String[] args) {
        ExampleApplication application = DaggerExampleApplicationComponent.create().application();

        application.start();
        application.stop();
    }
}
