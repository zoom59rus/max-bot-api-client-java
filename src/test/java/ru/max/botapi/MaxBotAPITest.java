package ru.max.botapi;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.client.MaxSerializer;
import ru.max.botapi.client.MaxTransportClient;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@Tag("UnitTest")
public class MaxBotAPITest {
    @Test
    public void shouldConstructEqualObjects() {
        MaxTransportClient transport = mock(MaxTransportClient.class);
        MaxSerializer serializer = mock(MaxSerializer.class);
        String accessToken = "access_token";
        MaxClient client = new MaxClient(accessToken, transport, serializer);
        MaxBotAPI api = new MaxBotAPI(accessToken, transport, serializer);
        assertThat(api.client.getTransport(), sameInstance(client.getTransport()));
        assertThat(api.client.getSerializer(), sameInstance(client.getSerializer()));
    }
}