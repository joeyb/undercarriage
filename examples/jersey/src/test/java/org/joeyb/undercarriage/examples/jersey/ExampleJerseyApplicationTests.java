package org.joeyb.undercarriage.examples.jersey;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.joeyb.undercarriage.examples.jersey.resources.PingResource;
import org.joeyb.undercarriage.jersey.testing.JerseyApplicationTestRule;
import org.junit.Rule;
import org.junit.Test;

public class ExampleJerseyApplicationTests {

    @Rule
    public final JerseyApplicationTestRule<ExampleJerseyConfig, ExampleJerseyApplication> applicationTestRule =
            new JerseyApplicationTestRule<>(
                    () -> new ExampleJerseyApplication(new TestExampleJerseyApplicationBinder()));

    @Test
    public void pingEndpointShouldRespondWithPong() {
        given().port(applicationTestRule.application().port()).get("/ping")
                .then().statusCode(200)
                .and().body(is(PingResource.PONG));
    }
}
