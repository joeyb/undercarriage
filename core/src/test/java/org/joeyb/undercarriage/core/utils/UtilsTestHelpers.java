package org.joeyb.undercarriage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.joeyb.undercarriage.core.utils.Exceptions.wrapChecked;

import java.lang.reflect.Constructor;

class UtilsTestHelpers {

    static void assertClassOnlyHasPrivateConstructor(Class<?> klass) {
        Constructor<?>[] constructors = klass.getDeclaredConstructors();

        assertThat(constructors)
                .isNotNull()
                .hasSize(1);

        Constructor<?> constructor = constructors[0];

        assertThat(constructor)
                .isNotNull()
                .matches(c -> c.getParameterCount() == 0)
                .matches(c -> !c.isAccessible());

        // Execute the constructor for code coverage.
        constructor.setAccessible(true);

        wrapChecked(() -> constructor.newInstance((Object[]) null));
    }

    private UtilsTestHelpers() { }
}
