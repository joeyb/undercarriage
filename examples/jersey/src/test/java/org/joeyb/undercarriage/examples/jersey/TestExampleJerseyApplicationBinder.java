package org.joeyb.undercarriage.examples.jersey;

import org.glassfish.hk2.api.Factory;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.testing.config.MockConfigContext;

public class TestExampleJerseyApplicationBinder extends ExampleJerseyApplicationBinder {

    @Override
    protected Class<? extends Factory<ConfigContext<ExampleJerseyConfig>>> configContextFactory() {
        return MockConfigContextDecoratorFactory.class;
    }

    public static class MockConfigContextDecoratorFactory extends DefaultConfigContextFactory {

        @Override
        public ConfigContext<ExampleJerseyConfig> provide() {
            return new MockConfigContext<ExampleJerseyConfig>(super.provide()) { };
        }
    }
}
