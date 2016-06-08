package org.joeyb.undercarriage.examples.minimal;

import org.joeyb.undercarriage.core.ApplicationBase;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinimalApplication extends ApplicationBase<MinimalConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinimalApplication.class);

    public MinimalApplication() {
        super(createConfigContext());
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        LOGGER.info("Configuring...");
    }

    @Override
    protected void onStart() {
        super.onStart();

        LOGGER.info("Starting...");
    }

    @Override
    protected void onStop() {
        super.onStop();

        LOGGER.info("Stopping...");
    }

    private static ConfigContext<MinimalConfig> createConfigContext() {
        return new ManualConfigContext<>(ImmutableMinimalConfig.builder().build());
    }
}
