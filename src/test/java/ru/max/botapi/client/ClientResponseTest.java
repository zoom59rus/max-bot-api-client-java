package ru.max.botapi.client;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;


@Tag("UnitTest")
public class ClientResponseTest {
    @Test
    public void shouldReturnHeaders() {
        Map<String, String> headers = Collections.singletonMap("Header", "Value");
        ClientResponse response = new ClientResponse(200, null, headers);
        assertThat(response.getHeaders(), is(sameInstance(headers)));
    }

    @Test
    public void shouldReturnBody() {
        byte[] body = new byte[]{1, 2, 3};
        ClientResponse response = new ClientResponse(200, body, Collections.emptyMap());
        assertThat(response.getBody(), is(sameInstance(body)));
    }
}