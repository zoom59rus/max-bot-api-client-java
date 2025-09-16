package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;


public class CollectionQueryParamTest {
    @Test
    public void shouldFormatCollection() throws Exception {
        MaxQuery<?> holder = mock(MaxQuery.class);
        CollectionQueryParam<String> param = new CollectionQueryParam<>("testparam", holder);
        param.setValue(Arrays.asList("one", "two"));
        assertThat(param.format(), is("one,two"));
    }

    @Test
    public void shouldFormatToEmptyString() {
        MaxQuery<?> holder = mock(MaxQuery.class);
        CollectionQueryParam<String> param = new CollectionQueryParam<>("testparam", holder);
        assertThat(param.format(), is(""));
    }
}