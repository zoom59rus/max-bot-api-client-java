package ru.max.botapi.client;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.max.botapi.server.MaxService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;


@Tag("UnitTest")
public class MaxClientTest {
    @Test
    public void shouldObtainEndpointFromEnvironment() {
        String endpoint = "https://testapi.max.ru";
        MaxClient client = new MaxClient(MaxService.ACCESS_TOKEN, mock(MaxTransportClient.class),
                mock(MaxSerializer.class)) {
            @Override
            String getEnvironment(String name) {
                if (name.equals(MaxClient.ENDPOINT_ENV_VAR_NAME)) {
                    return endpoint;
                }

                return super.getEnvironment(name);
            }
        };

        assertThat(client.getEndpoint(), is(endpoint));
    }
}