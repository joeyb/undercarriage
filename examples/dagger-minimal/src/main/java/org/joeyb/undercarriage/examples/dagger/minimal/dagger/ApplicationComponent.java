package org.joeyb.undercarriage.examples.dagger.minimal.dagger;

import dagger.Component;

import org.joeyb.undercarriage.examples.dagger.minimal.DaggerMinimalApplication;

import javax.inject.Singleton;

@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {

    DaggerMinimalApplication application();
}
