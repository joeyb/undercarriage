package org.joeyb.undercarriage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.joeyb.undercarriage.core.utils.Exceptions.wrapChecked;

import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class ExceptionsTests {

    @Test
    public void wrapCheckedThrowsRuntimeExceptionForCheckedException() {
        final String message = UUID.randomUUID().toString();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> wrapChecked(() -> throwCheckedException(message)))
                .isNotNull()
                .withCauseInstanceOf(IOException.class)
                .matches(e -> e.getCause().getMessage().equals(message));
    }

    @Test
    public void wrapCheckedReturnsResultWhenNoException() {
        final String result = UUID.randomUUID().toString();

        assertThat(wrapChecked(() -> returnResult(result)))
                .isEqualTo(result);
    }

    private static String returnResult(String result) throws IOException {
        return result;
    }

    private static String throwCheckedException(String message) throws IOException {
        throw new IOException(message);
    }
}
