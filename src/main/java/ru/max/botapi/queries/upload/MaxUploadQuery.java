/*
 * ------------------------------------------------------------------------
 * Max chat Bot API
 * ------------------------------------------------------------------------
 * Copyright (C) 2025 COMMUNICATION PLATFORM LLC
 * ------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */

package ru.max.botapi.queries.upload;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.queries.MaxQuery;

public abstract class MaxUploadQuery<T> extends MaxQuery<T> {
    private final UploadExec uploadExec;
    private final MaxClient maxClient;

    public MaxUploadQuery(MaxClient maxClient, Class<T> responseType, String url, File file) {
        super(maxClient, url, responseType);
        this.maxClient = maxClient;
        this.uploadExec = new FileUploadExec(url, file);
    }

    public MaxUploadQuery(MaxClient maxClient, Class<T> responseType, String url, String fileName,
                          InputStream input) {
        super(maxClient, url, responseType);
        this.maxClient = maxClient;
        this.uploadExec = new StreamUploadExec(url, fileName, input);
    }

    public UploadExec getUploadExec() {
        return uploadExec;
    }

    @Override
    public T execute() throws APIException, ClientException {
        try {
            return maxClient.newCall(this).get();
        } catch (InterruptedException e) {
            throw new ClientException("Current request was interrupted", e);
        } catch (ExecutionException e) {
            return unwrap(e);
        }
    }

    public Future<T> enqueue() throws ClientException {
        return maxClient.newCall(this);
    }
}
