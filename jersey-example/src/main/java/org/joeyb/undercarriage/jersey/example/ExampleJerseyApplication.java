package org.joeyb.undercarriage.jersey.example;

import org.joeyb.undercarriage.jersey.JerseyApplicationBase;
import org.joeyb.undercarriage.jersey.example.resources.PingResource;

public class ExampleJerseyApplication extends JerseyApplicationBase<ExampleJerseyApplicationConfig> {

    public ExampleJerseyApplication() {
        super(new ExampleJerseyApplicationBinder());
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        resourceConfig().registerClasses(PingResource.class);
    }
}
