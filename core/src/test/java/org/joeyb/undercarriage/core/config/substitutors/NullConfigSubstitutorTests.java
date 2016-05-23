package org.joeyb.undercarriage.core.config.substitutors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import java.util.UUID;

public class NullConfigSubstitutorTests {

    @Test
    public void substituteReturnsTheConfigUnchanged() {
        final String config = UUID.randomUUID().toString();

        NullConfigSubstitutor substitutor = new NullConfigSubstitutor();

        assertThat(substitutor.substitute(config)).isEqualTo(config);
    }
}
