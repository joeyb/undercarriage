package org.joeyb.undercarriage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.joeyb.undercarriage.core.utils.Exceptions.wrapChecked;
import static org.joeyb.undercarriage.core.utils.UtilsTestHelpers.assertClassOnlyHasPrivateConstructor;

import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class ExceptionsTests {

    @Test
    public void exceptionsOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Exceptions.class);
    }

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
    public void wrapCheckedWithErrorMessageThrowsRuntimeExceptionWithMessageForCheckedException() {
        final String checkedMessage = UUID.randomUUID().toString();
        final String uncheckedMessage = UUID.randomUUID().toString();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> wrapChecked(() -> throwCheckedException(checkedMessage), uncheckedMessage))
                .withMessage(uncheckedMessage)
                .isNotNull()
                .withCauseInstanceOf(IOException.class)
                .matches(e -> e.getCause().getMessage().equals(checkedMessage));
    }

    @Test
    public void wrapCheckedReturnsResultWhenNoException() {
        final String result = UUID.randomUUID().toString();

        assertThat(wrapChecked(() -> returnResult(result)))
                .isEqualTo(result);
    }

    @Test
    public void wrapCheckedWithErrorMessageReturnsResultWhenNoException() {
        final String result = UUID.randomUUID().toString();
        final String uncheckedMessage = UUID.randomUUID().toString();

        assertThat(wrapChecked(() -> returnResult(result), uncheckedMessage))
                .isEqualTo(result);
    }

    private static String returnResult(String result) throws IOException {
        return result;
    }

    private static String throwCheckedException(String message) throws IOException {
        throw new IOException(message);
    }
}
