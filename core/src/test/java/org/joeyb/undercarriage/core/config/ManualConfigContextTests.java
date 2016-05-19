package org.joeyb.undercarriage.core.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ManualConfigContextTests {

    @Test
    public void configGivenAtConstructionIsReturnedFromConfigMethod() {
        ConfigSection config = mock(ConfigSection.class);

        ManualConfigContext<ConfigSection> configContext = new ManualConfigContext<>(config);

        assertThat(configContext.config()).isEqualTo(config);
    }
}
