package org.joeyb.undercarriage.examples.spark.dagger;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.joeyb.undercarriage.examples.spark.dagger.dagger.DaggerApplicationComponent;
import org.joeyb.undercarriage.examples.spark.dagger.dagger.MockConfigContextModule;
import org.joeyb.undercarriage.spark.testing.SparkApplicationTestRule;
import org.junit.Rule;
import org.junit.Test;

public class SparkDaggerApplicationTests {

    @Rule
    public final SparkApplicationTestRule<SparkDaggerConfig, SparkDaggerApplication> applicationTestRule =
            new SparkApplicationTestRule<>(
                    () -> DaggerApplicationComponent.builder()
                            .configContextModule(new MockConfigContextModule())
                            .build()
                            .application());

    @Test
    public void pingEndpointShouldRespondWithPong() {
        given().port(applicationTestRule.application().port()).get("/ping")
                .then().statusCode(200)
                .and().body(is("pong"));
    }

    @Test
    public void pluginPingEndpointShouldRespondWithPong() {
        given().port(applicationTestRule.application().port()).get("/plugin-ping")
                .then().statusCode(200)
                .and().body(is("plugin-pong"));
    }
}
