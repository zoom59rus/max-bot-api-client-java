package ru.max.botapi.client.impl;

import org.apache.log4j.Level;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.max.botapi.client.ClientResponse;
import ru.max.botapi.server.MaxServer;
import ru.max.botapi.server.MaxService;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static spark.Spark.get;

@Tag("UnitTest")
public class OkHttpTransportClientTest {
    @Test
    public void shouldThrowException() throws Exception {
        OkHttpTransportClient client = new OkHttpTransportClient();
        InputStream inputStream = mock(InputStream.class);
        IOException fakeException = new IOException("fake exception");
        when(inputStream.read(any(), anyInt(), anyInt())).thenThrow(fakeException);
        doThrow(fakeException).when(inputStream).close();

        client.post("http://invalidurl", "test.txt", inputStream);
    }

    @Test
    public void shouldThrowExceptionWhenResponseBodyIsInvalid() throws Throwable {
        String path = "/shouldThrowExceptionWhenResponseBodyIsInvalid";
        get(path, (req, resp) -> {
            // too big content-length
            resp.header("Content-Length", String.valueOf(Long.MAX_VALUE));
            return "response";
        });

        OkHttpTransportClient client = new OkHttpTransportClient();
        Future<ClientResponse> futureResponse = client.get(MaxServer.ENDPOINT + path + "?access_token=" + MaxService.ACCESS_TOKEN);
        org.apache.log4j.Logger logger4j = org.apache.log4j.Logger.getRootLogger();
        Level currentLevel = logger4j.getLevel();
        logger4j.setLevel(Level.DEBUG);

        try {
            futureResponse.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        } finally {
            logger4j.setLevel(currentLevel);
        }
    }
}