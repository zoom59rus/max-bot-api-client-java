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

package ru.max.botapi;

import ru.max.botapi.client.MaxClient;
import ru.max.botapi.queries.upload.MaxUploadAVQuery;
import ru.max.botapi.queries.upload.MaxUploadFileQuery;
import ru.max.botapi.queries.upload.MaxUploadImageQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MaxUploadAPI {
    private final MaxClient client;

    public MaxUploadAPI(MaxClient client) {
        this.client = client;
    }

    public MaxUploadFileQuery uploadFile(String url, File file) throws FileNotFoundException {
        return new MaxUploadFileQuery(client, url, file);
    }

    public MaxUploadFileQuery uploadFile(String url, String fileName, InputStream inputStream) {
        return new MaxUploadFileQuery(client, url, fileName, inputStream);
    }

    public MaxUploadImageQuery uploadImage(String url, File file) throws FileNotFoundException {
        return new MaxUploadImageQuery(client, url, file.getName(), new FileInputStream(file));
    }

    public MaxUploadImageQuery uploadImage(String url, String fileName, InputStream inputStream) {
        return new MaxUploadImageQuery(client, url, fileName, inputStream);
    }

    public MaxUploadAVQuery uploadAV(String url, File file) {
        return new MaxUploadAVQuery(client, url, file);
    }

    public MaxUploadAVQuery uploadAV(String url, String fileName, InputStream inputStream) {
        return new MaxUploadAVQuery(client, url, fileName, inputStream);
    }
}
