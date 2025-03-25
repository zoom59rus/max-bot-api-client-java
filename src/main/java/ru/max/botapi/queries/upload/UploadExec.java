package ru.max.botapi.queries.upload;

import java.util.concurrent.Future;

import ru.max.botapi.client.ClientResponse;
import ru.max.botapi.client.MaxTransportClient;
import ru.max.botapi.exceptions.ClientException;

public interface UploadExec {
    Future<ClientResponse> newCall(MaxTransportClient transportClient) throws ClientException, InterruptedException;
}
