package org.joeyb.undercarriage.core.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ManualConfigContextTests {

    @Test
    public void configGivenAtConstructionIsReturnedFromConfigMethod() {
        ConfigSection config = mock(ConfigSection.class);

        ManualConfigContext<ConfigSection> configContext = new ManualConfigContext<>(config);

        assertThat(configContext.config()).isEqualTo(config);
    }
}
