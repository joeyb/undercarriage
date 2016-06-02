package org.joeyb.undercarriage.example.dagger;

import dagger.Component;

import org.joeyb.undercarriage.example.ExampleApplication;

import javax.inject.Singleton;

/**
 * {@code ExampleApplicationComponent} is the main dagger {@link Component} for the application.
 */
@Component(modules = ExampleApplicationModule.class)
@Singleton
public interface ExampleApplicationComponent {

    ExampleApplication application();
}
