package org.joeyb.undercarriage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import java.util.List;

public class GenericClassTests {

    @Test
    public void getGenericClassWorksWithRealType() {
        Class<List<String>> c = new GenericClass<List<String>>() { }.getGenericClass();

        assertThat(c.getCanonicalName()).isEqualTo(List.class.getCanonicalName());
    }
}
