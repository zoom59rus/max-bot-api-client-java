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

package ru.max.botapi.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.jetbrains.annotations.NotNull;

import ru.max.botapi.client.MaxClient;
import ru.max.botapi.client.MaxTransportClient;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.exceptions.TransportClientException;

public class MaxQuery<T> {
    private final MaxClient maxClient;
    private final String url;
    private final Class<T> responseType;
    private final Object body;
    private final MaxTransportClient.Method method;
    private List<QueryParam<?>> params;

    public MaxQuery(MaxClient maxClient, String url, Class<T> responseType) {
        this(maxClient, url, null, responseType, MaxTransportClient.Method.POST);
    }

    public MaxQuery(MaxClient maxClient, String url, Class<T> responseType,
                    MaxTransportClient.Method method) {
        this(maxClient, url, null, responseType, method);
    }

    public MaxQuery(MaxClient maxClient, String url, Object body, Class<T> responseType,
                    MaxTransportClient.Method method) {
        this.maxClient = maxClient;
        this.url = url;
        this.responseType = responseType;
        this.body = body;
        this.method = method;
    }

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

    void addParam(@NotNull QueryParam param) {
        if (params == null) {
            params = new ArrayList<>();
        }

        params.add(param);
    }

    public static String substitute(String pathTemplate, Object... substitutions) {
        StringBuilder sb = new StringBuilder();
        int nextSubst = 0;
        char[] chars = pathTemplate.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '{') {
                i = pathTemplate.indexOf('}', i);
                sb.append(substitutions[nextSubst++]);
                continue;
            }

            sb.append(c);
        }

        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public List<QueryParam<?>> getParams() {
        return params;
    }

    public MaxTransportClient.Method getMethod() {
        return method;
    }

    public Object getBody() {
        return body;
    }

    public Class<T> getResponseType() {
        return responseType;
    }

    protected T unwrap(ExecutionException e) throws APIException, ClientException {
        Throwable cause = e.getCause();
        if (cause == null) {
            throw new ClientException(e);
        }

        if (cause instanceof TransportClientException) {
            throw new ClientException(cause);
        }

        if (cause instanceof APIException) {
            throw (APIException) cause;
        }

        if (cause instanceof ClientException) {
            throw (ClientException) cause;
        }

        throw new ClientException(cause);
    }
}
