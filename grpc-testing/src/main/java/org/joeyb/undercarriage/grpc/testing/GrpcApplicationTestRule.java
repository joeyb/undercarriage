package org.joeyb.undercarriage.grpc.testing;

import static org.mockito.Mockito.when;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.testing.ApplicationTestRule;
import org.joeyb.undercarriage.grpc.GrpcApplication;
import org.joeyb.undercarriage.grpc.config.GrpcConfig;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;
import org.joeyb.undercarriage.grpc.config.ImmutableGrpcConfig;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import java.util.function.Supplier;

/**
 * {@code GrpcApplicationTestRule} is a JUnit {@link TestRule} that manages running a gRPC application during testing.
 * If it is setup as a {@link Rule} then it will create a new application instance for each test. If it is declared as a
 * {@link ClassRule} then it will create a single application instance for all tests within the test's class.
 *
 * <p>If the config provided by the app's {@link ConfigContext#config()} is a mock, then the rule will automatically
 * configure the app to run on a random available port.
 *
 * @param <ConfigT> the app's config type
 * @param <AppT> the app type
 */
public class GrpcApplicationTestRule<ConfigT extends GrpcConfigSection, AppT extends GrpcApplication<ConfigT>>
        extends ApplicationTestRule<ConfigT, AppT> {

    /**
     * Constructs an instance of {@code GrpcApplicationTestRule}. The given {@link Supplier} is used whenever a new
     * instance of the app needs to be created.
     *
     * @param applicationSupplier supplier for creating a new instance of the app
     */
    public GrpcApplicationTestRule(Supplier<AppT> applicationSupplier) {
        super(() -> {
            AppT application = applicationSupplier.get();

            ConfigT config = application.configContext().config();

            if (Mockito.mockingDetails(config.grpc()).isMock()) {
                // If config.grpc() is a mock then the config has deep stubs, so we should mock the settings directly
                // in order to preserve the deep stubs.
                when(config.grpc().port()).thenReturn(0);
            } else if (Mockito.mockingDetails(config).isMock()) {
                // If config is a mock without deep stubs then we should replace the entire config.grpc() value.
                GrpcConfig grpcConfig = config.grpc();

                when(config.grpc()).thenReturn(
                        ImmutableGrpcConfig.builder()
                                .from(grpcConfig)
                                .port(0)
                                .build());
            }

            return application;
        });
    }
}
