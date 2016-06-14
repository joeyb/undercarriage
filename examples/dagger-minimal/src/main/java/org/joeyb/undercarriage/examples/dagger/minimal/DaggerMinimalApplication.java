package org.joeyb.undercarriage.examples.dagger.minimal;

import org.joeyb.undercarriage.core.ApplicationBase;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class DaggerMinimalApplication extends ApplicationBase<DaggerMinimalConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaggerMinimalApplication.class);

    @Inject
    public DaggerMinimalApplication(ConfigContext<DaggerMinimalConfig> configContext) {
        super(configContext);
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
}
