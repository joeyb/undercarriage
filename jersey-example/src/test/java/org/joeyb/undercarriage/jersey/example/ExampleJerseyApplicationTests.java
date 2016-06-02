package org.joeyb.undercarriage.jersey.example;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.joeyb.undercarriage.jersey.example.resources.PingResource;
import org.junit.Test;

public class ExampleJerseyApplicationTests {

    @Test
    public void pingEndpointShouldRespondWithPong() {
        ExampleJerseyApplication application = new ExampleJerseyApplication();

        application.start();

        given().port(application.port()).get("/ping")
                .then().statusCode(200)
                .and().body(is(PingResource.PONG));

        application.stop();
    }
}
