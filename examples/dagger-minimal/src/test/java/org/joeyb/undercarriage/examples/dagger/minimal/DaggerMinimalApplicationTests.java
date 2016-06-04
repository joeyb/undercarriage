package org.joeyb.undercarriage.examples.dagger.minimal;

import org.joeyb.undercarriage.examples.dagger.minimal.dagger.DaggerApplicationComponent;
import org.junit.Test;

public class DaggerMinimalApplicationTests {

    @Test
    public void appStartsAndStopsSuccessfully() {
        DaggerMinimalApplication application = DaggerApplicationComponent.create().application();

        application.start();

        application.stop();
    }
}
