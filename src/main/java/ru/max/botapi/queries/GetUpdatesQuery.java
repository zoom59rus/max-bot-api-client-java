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

import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.UpdateList;

import java.util.Collection;
import java.util.Set;

import static ru.max.botapi.client.MaxTransportClient.Method;

public class GetUpdatesQuery extends MaxQuery<UpdateList> {
    public static final String PATH_TEMPLATE = "/updates";
    public final QueryParam<Integer> limit = new QueryParam<>("limit", this);
    public final QueryParam<Integer> timeout = new QueryParam<>("timeout", this);
    public final QueryParam<Long> marker = new QueryParam<>("marker", this);
    public final QueryParam<Collection<String>> types = new CollectionQueryParam<>("types", this);

    public GetUpdatesQuery(MaxClient client) {
        super(client, PATH_TEMPLATE, null, UpdateList.class, Method.GET);
    }

    public GetUpdatesQuery limit(Integer value) {
        this.limit.setValue(value);
        return this;
    }

    public GetUpdatesQuery timeout(Integer value) {
        this.timeout.setValue(value);
        return this;
    }

    public GetUpdatesQuery marker(Long value) {
        this.marker.setValue(value);
        return this;
    }

    public GetUpdatesQuery types(Set<String> value) {
        this.types.setValue(value);
        return this;
    }
}
