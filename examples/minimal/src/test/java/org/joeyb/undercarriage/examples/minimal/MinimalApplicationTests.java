package org.joeyb.undercarriage.examples.minimal;

import org.junit.Test;

public class MinimalApplicationTests {

    @Test
    public void appStartsAndStopsSuccessfully() {
        MinimalApplication application = new MinimalApplication();

        application.start();

        application.stop();
    }
}
