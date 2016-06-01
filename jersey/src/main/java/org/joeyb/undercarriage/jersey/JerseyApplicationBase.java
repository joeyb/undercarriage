package org.joeyb.undercarriage.jersey;

import static org.joeyb.undercarriage.core.utils.Exceptions.wrapChecked;

import com.google.common.annotations.VisibleForTesting;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.joeyb.undercarriage.core.ApplicationBase;
import org.joeyb.undercarriage.core.LateBoundApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.utils.GenericClass;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

/**
 * {@code JerseyApplicationBase} provides a base default implementation for the {@link JerseyApplication} interface.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class JerseyApplicationBase<ConfigT extends JerseyConfigSection>
        extends ApplicationBase<ConfigT>
        implements JerseyApplication<ConfigT> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyApplicationBase.class);

    private final ResourceConfig resourceConfig;
    private final ServiceLocator serviceLocator;

    private Server server;

    /**
     * Constructs an instance of {@link JerseyApplicationBase} using the given {@link Binder}. This constructor
     * automatically creates a {@link ServiceLocator} for the {@link Binder} and calls
     * {@link JerseyApplicationBase#JerseyApplicationBase(ServiceLocator)}.
     *
     * @param binder the app's HK2-based DI configuration
     */
    protected JerseyApplicationBase(Binder binder) {
        this(createServiceLocator(binder));
    }

    /**
     * Constructs an instance of {@link JerseyApplicationBase} using the given {@link ServiceLocator}. This constructor
     * uses the {@link ServiceLocator} to get the instances for the app's {@link ConfigContext} and its
     * {@link ResourceConfig}. Both should be configured as singletons in the DI configuration. It also uses the
     * {@link ServiceLocator} to get the instance for the app's {@link LateBoundApplicationResolver} and sets this
     * instance as the application instance. It should also be configured as a singleton in the DI configuration.
     *
     * @param serviceLocator the app's HK2-based service locator
     */
    protected JerseyApplicationBase(ServiceLocator serviceLocator) {
        super(serviceLocator.getService(new GenericClass<ConfigContext<ConfigT>>() { }.getGenericClass()));

        this.resourceConfig = serviceLocator.getService(ResourceConfig.class);
        this.serviceLocator = serviceLocator;

        LateBoundApplicationResolver applicationResolver =
                serviceLocator.getService(LateBoundApplicationResolver.class);

        applicationResolver.setApplication(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int port() {
        if (!isStarted()) {
            throw new IllegalStateException("The application must be started before we know its port.");
        }

        return server.getURI().getPort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResourceConfig resourceConfig() {
        return resourceConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ServiceLocator serviceLocator() {
        return serviceLocator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStart() {
        super.onStart();

        final URI baseUri = UriBuilder
                .fromUri(configContext().config().jersey().baseUri())
                .build();

        server = createServer(baseUri);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.getServletContext().setAttribute(ServletProperties.SERVICE_LOCATOR, serviceLocator());
        servletContextHandler.setContextPath(baseUri.getPath());

        ServletHolder jerseyServletHolder = new ServletHolder(new ServletContainer(resourceConfig()));
        jerseyServletHolder.setInitOrder(0);

        servletContextHandler.addServlet(jerseyServletHolder, "/*");

        server.setHandler(servletContextHandler);

        wrapChecked(
                () -> {
                    server.start();
                    return null;
                },
                "Jersey/Jetty server failed to start");

        if (configContext().config().jersey().joinServerThread()) {
            try {
                server.join();
            } catch (InterruptedException e) {
                LOGGER.info("Jersey/Jetty server interrupted", e);
            }
        }
    }

    private static ServiceLocator createServiceLocator(Binder binder) {
        final ServiceLocator serviceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator();

        ServiceLocatorUtilities.bind(serviceLocator, binder);

        return serviceLocator;
    }

    @VisibleForTesting
    Server createServer(URI baseUri) {
        return JettyHttpContainerFactory.createServer(baseUri, false);
    }
}
