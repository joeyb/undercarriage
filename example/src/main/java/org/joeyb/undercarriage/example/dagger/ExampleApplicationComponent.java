package org.joeyb.undercarriage.example.dagger;

import dagger.Component;
import org.joeyb.undercarriage.example.ExampleApplication;

import javax.inject.Singleton;

@Component(modules = ConfigModule.class)
@Singleton
public interface ExampleApplicationComponent {

    ExampleApplication application();
}
