package org.joeyb.undercarriage.examples.jersey;

import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;

import org.glassfish.hk2.api.Factory;
import org.joeyb.undercarriage.core.config.ConfigContext;

public class TestExampleJerseyApplicationBinder extends ExampleJerseyApplicationBinder {

    @Override
    protected Class<? extends Factory<ConfigContext<ExampleJerseyConfig>>> configContextFactory() {
        return MockConfigContextDecoratorFactory.class;
    }

    public static class MockConfigContextDecoratorFactory extends DefaultConfigContextFactory {

        @Override
        public ConfigContext<ExampleJerseyConfig> provide() {
            ConfigContext<ExampleJerseyConfig> configContext = super.provide();

            return mock(configContext.getClass(), delegatesTo(configContext));
        }
    }
}
