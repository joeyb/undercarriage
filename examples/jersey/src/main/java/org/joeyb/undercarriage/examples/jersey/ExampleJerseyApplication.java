package org.joeyb.undercarriage.examples.jersey;

import org.glassfish.hk2.utilities.Binder;
import org.joeyb.undercarriage.examples.jersey.resources.PingResource;
import org.joeyb.undercarriage.jersey.JerseyApplicationBase;

public class ExampleJerseyApplication extends JerseyApplicationBase<ExampleJerseyConfig> {

    public ExampleJerseyApplication(Binder binder) {
        super(binder);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        resourceConfig().registerClasses(PingResource.class);
    }

    /**
     * The application's entry point.
     *
     * @param args command-line parameters
     */
    public static void main(String[] args) {
        ExampleJerseyApplication application = new ExampleJerseyApplication(new ExampleJerseyApplicationBinder());

        application.start();

        application.stop();
    }
}
