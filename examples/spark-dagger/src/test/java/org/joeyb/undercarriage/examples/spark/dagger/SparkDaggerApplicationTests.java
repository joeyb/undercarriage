package org.joeyb.undercarriage.examples.spark.dagger;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.joeyb.undercarriage.examples.spark.dagger.dagger.DaggerApplicationComponent;
import org.junit.Test;

public class SparkDaggerApplicationTests {

    @Test
    public void pingEndpointShouldRespondWithPong() {
        SparkDaggerApplication application = DaggerApplicationComponent.create().application();

        application.start();

        given().port(application.port()).get("/ping")
                .then().statusCode(200)
                .and().body(is("pong"));

        application.stop();
    }

    @Test
    public void pluginPingEndpointShouldRespondWithPong() {
        SparkDaggerApplication application = DaggerApplicationComponent.create().application();

        application.start();

        given().port(application.port()).get("/plugin-ping")
                .then().statusCode(200)
                .and().body(is("plugin-pong"));

        application.stop();
    }
}
