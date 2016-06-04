package org.joeyb.undercarriage.examples.jersey;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import org.joeyb.undercarriage.examples.jersey.resources.PingResource;
import org.joeyb.undercarriage.jersey.config.ImmutableJerseyConfig;
import org.junit.Test;

public class ExampleJerseyApplicationTests {

    @Test
    public void pingEndpointShouldRespondWithPong() {
        ExampleJerseyApplication application = new ExampleJerseyApplication(new TestExampleJerseyApplicationBinder());

        ExampleJerseyConfig config = application.configContext().config();

        when(application.configContext().config())
                .thenReturn(
                        ImmutableExampleJerseyConfig.builder()
                                .from(config)
                                .jersey(
                                        ImmutableJerseyConfig.builder()
                                                .from(config.jersey())
                                                .joinServerThread(false)
                                                .build())
                                .build());

        application.start();

        given().port(application.port()).get("/ping")
                .then().statusCode(200)
                .and().body(is(PingResource.PONG));

        application.stop();
    }
}
