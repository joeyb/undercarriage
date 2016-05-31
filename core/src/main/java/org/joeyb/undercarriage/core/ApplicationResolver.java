package org.joeyb.undercarriage.core;

import org.joeyb.undercarriage.core.config.ConfigSection;

import java.util.Optional;

/**
 * {@code ApplicationResolver} is used to get the app instance and break potential circular dependencies.
 */
public interface ApplicationResolver {

    /**
     * Returns the application instance.
     *
     * @throws IllegalStateException if the application is null
     */
    Application<?> application();

    /**
     * Returns the application as the requested type if it implements that type, otherwise throws.
     *
     * <p>It's important to note that, due to type erasure, this method makes no guarantees about the generic portions
     * of the requested types. For example, it's possible to pass in an app class like
     * {@code Class&lt;Application&lt;WrongConfigType&gt;&gt;} and this method will not fail, but you will run into
     * issues once you try to access any fields/methods that actually use that generic type.
     *
     * @param applicationClass the requested app type
     * @param <AppT> the requested app type
     * @throws IllegalArgumentException if the application is not of the requested type
     * @throws IllegalStateException if the application is null
     */
    <AppT extends Application<?>> AppT application(Class<AppT> applicationClass);

    /**
     * Returns the application as the requested type if it implements that type, otherwise returns an empty optional.
     *
     * <p>It's important to note that, due to type erasure, this method makes no guarantees about the generic portions
     * of the requested types. For example, it's possible to pass in an app class like
     * {@code Class&lt;Application&lt;WrongConfigType&gt;&gt;} and this method will not fail, but you will run into
     * issues once you try to access any fields/methods that actually use that generic type.
     *
     * @param applicationClass the requested app type
     * @param <AppT> the requested app type
     * @throws IllegalStateException if the application is null
     */
    <AppT extends Application<?>> Optional<AppT> optionalApplication(Class<AppT> applicationClass);
}
