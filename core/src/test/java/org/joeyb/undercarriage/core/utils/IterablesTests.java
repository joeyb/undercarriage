package org.joeyb.undercarriage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.joeyb.undercarriage.core.utils.UtilsTestHelpers.assertClassOnlyHasPrivateConstructor;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import java.util.UUID;
import java.util.stream.Stream;

public class IterablesTests {

    @Test
    public void iterablesOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Iterables.class);
    }

    @Test
    public void streamReturnsValidStream() {
        final Iterable<String> strings = ImmutableList.of(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        Stream<String> stringsStream = Iterables.stream(strings);

        assertThat(stringsStream.isParallel()).isEqualTo(false);
        assertThat(stringsStream).containsExactlyElementsOf(strings);
    }

    @Test
    public void parallelStreamReturnsValidStream() {
        final Iterable<String> strings = ImmutableList.of(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        Stream<String> stringsStream = Iterables.parallelStream(strings);

        assertThat(stringsStream.isParallel()).isEqualTo(true);
        assertThat(stringsStream).containsOnlyElementsOf(strings);
    }
}
