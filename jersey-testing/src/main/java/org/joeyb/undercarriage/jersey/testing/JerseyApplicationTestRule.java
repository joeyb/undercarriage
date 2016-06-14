package org.joeyb.undercarriage.jersey.testing;

import static org.mockito.Mockito.when;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.testing.ApplicationTestRule;
import org.joeyb.undercarriage.jersey.JerseyApplication;
import org.joeyb.undercarriage.jersey.config.ImmutableJerseyConfig;
import org.joeyb.undercarriage.jersey.config.JerseyConfig;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import java.util.function.Supplier;

/**
 * {@code JerseyApplicationTestRule} is a JUnit {@link TestRule} that manages running a Jersey application during
 * testing. If it is setup as a {@link Rule} then it will create a new application instance for each test. If it is
 * declared as a {@link ClassRule} then it will create a single application instance for all tests within the test's
 * class.
 *
 * <p>If the config provided by the app's {@link ConfigContext#config()} is a mock, then the rule will automatically
 * configure the app to run on a random available port with a base URI of {@code http://localhost:0/}. It will also be
 * configured to not join the server's thread.
 *
 * @param <ConfigT> the app's config type
 * @param <AppT> the app type
 */
public class JerseyApplicationTestRule<ConfigT extends JerseyConfigSection, AppT extends JerseyApplication<ConfigT>>
        extends ApplicationTestRule<ConfigT, AppT> {

    private static final String TEST_BASE_URI = "http://localhost:0";

    /**
     * Constructs an instance of {@code JerseyApplicationTestRule}. The given {@link Supplier} is used whenever a new
     * instance of the app needs to be created.
     *
     * @param applicationSupplier supplier for creating a new instance of the app
     */
    public JerseyApplicationTestRule(Supplier<AppT> applicationSupplier) {
        super(() -> {
            AppT application = applicationSupplier.get();

            ConfigT config = application.configContext().config();

            if (Mockito.mockingDetails(config.jersey()).isMock()) {
                // If config.jersey() is a mock then the config has deep stubs, so we should mock the settings directly
                // in order to preserve the deep stubs.
                when(config.jersey().baseUri()).thenReturn(TEST_BASE_URI);
                when(config.jersey().joinServerThread()).thenReturn(false);
            } else if (Mockito.mockingDetails(config).isMock()) {
                // If config is a mock without deep stubs then we should replace the entire config.jersey() value.
                JerseyConfig jerseyConfig = config.jersey();

                when(config.jersey()).thenReturn(
                        ImmutableJerseyConfig.builder()
                                .from(jerseyConfig)
                                .baseUri(TEST_BASE_URI)
                                .joinServerThread(false)
                                .build());
            }

            return application;
        });
    }
}
