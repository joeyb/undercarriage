package org.joeyb.undercarriage.core.plugins;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;

public class MockPlugin implements Plugin<ConfigSection> {

    @Override
    public ConfigContext<ConfigSection> configContext() {
        return null;
    }
}
