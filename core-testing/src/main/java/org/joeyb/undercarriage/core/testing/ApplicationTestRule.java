package org.joeyb.undercarriage.core.testing;

import org.joeyb.undercarriage.core.Application;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;

import java.util.function.Supplier;

/**
 * {@code ApplicationTestRule} is a JUnit {@link TestRule} that manages running an application during testing. If it is
 * setup as a {@link Rule} then it will create a new application instance for each test. If it is declared as a
 * {@link ClassRule} then it will create a single application instance for all tests within the test's class.
 *
 * @param <ConfigT> the app's config type
 * @param <AppT> the app type
 */
public class ApplicationTestRule<ConfigT extends ConfigSection, AppT extends Application<ConfigT>>
        extends ExternalResource {

    private final Supplier<AppT> applicationSupplier;

    private AppT application;

    /**
     * Constructs an instance of {@code ApplicationTestRule}. The given {@link Supplier} is used whenever a new instance
     * of the app needs to be created.
     *
     * @param applicationSupplier supplier for creating a new instance of the app
     */
    public ApplicationTestRule(Supplier<AppT> applicationSupplier) {
        this.applicationSupplier = applicationSupplier;
    }

    /**
     * Returns the current test's application instance. Returns {@code null} if a test is not running.
     */
    public final AppT application() {
        return application;
    }

    /**
     * Creates a new instance of the app and starts it before the test run.
     */
    @Override
    protected void before() throws Throwable {
        application = applicationSupplier.get();
        application.start();
    }

    /**
     * Stops the app and nulls out the reference to it after the test run.
     */
    @Override
    protected void after() {
        application.stop();
        application = null;
    }
}
