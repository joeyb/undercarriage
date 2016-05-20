package org.joeyb.undercarriage.spark.example;

import org.joeyb.undercarriage.spark.example.dagger.DaggerTestSparkApplicationComponent;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class TestSparkApplicationTests {

    @Test
    public void pingEndpointShouldRespondWithPong() {
        TestSparkApplication application = DaggerTestSparkApplicationComponent.create().application();

        application.start();

        given().port(application.port()).get("/ping")
                .then().statusCode(200)
                .and().body(is("pong"));

        application.stop();
    }

    @Test
    public void pluginPingEndpointShouldRespondWithPong() {
        TestSparkApplication application = DaggerTestSparkApplicationComponent.create().application();

        application.start();

        given().port(application.port()).get("/plugin-ping")
                .then().statusCode(200)
                .and().body(is("plugin-pong"));

        application.stop();
    }
}
