package ru.max.botapi.queries.upload;

import java.io.File;
import java.util.concurrent.Future;

import ru.max.botapi.client.ClientResponse;
import ru.max.botapi.client.MaxTransportClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.exceptions.TransportClientException;

class FileUploadExec implements UploadExec {
    private final File file;
    private final String url;

    FileUploadExec(String url, File file) {
        this.url = url;
        this.file = file;
    }

    @Override
    public Future<ClientResponse> newCall(MaxTransportClient transportClient) throws ClientException,
            InterruptedException {

        try {
            return transportClient.post(url, file);
        } catch (TransportClientException e) {
            throw new ClientException(e);
        }
    }
}
