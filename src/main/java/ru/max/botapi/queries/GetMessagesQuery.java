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
import ru.max.botapi.model.MessageList;

import java.util.Collection;
import java.util.Set;

import static ru.max.botapi.client.MaxTransportClient.Method;

public class GetMessagesQuery extends MaxQuery<MessageList> {
    public static final String PATH_TEMPLATE = "/messages";
    public final QueryParam<Long> chatId = new QueryParam<>("chat_id", this);
    public final QueryParam<Collection<String>> messageIds = new CollectionQueryParam<>("message_ids", this);
    public final QueryParam<Long> from = new QueryParam<>("from", this);
    public final QueryParam<Long> to = new QueryParam<>("to", this);
    public final QueryParam<Integer> count = new QueryParam<>("count", this);

    public GetMessagesQuery(MaxClient client) {
        super(client, PATH_TEMPLATE, null, MessageList.class, Method.GET);
    }

    public GetMessagesQuery chatId(Long value) {
        this.chatId.setValue(value);
        return this;
    }

    public GetMessagesQuery messageIds(Set<String> value) {
        this.messageIds.setValue(value);
        return this;
    }

    public GetMessagesQuery from(Long value) {
        this.from.setValue(value);
        return this;
    }

    public GetMessagesQuery to(Long value) {
        this.to.setValue(value);
        return this;
    }

    public GetMessagesQuery count(Integer value) {
        this.count.setValue(value);
        return this;
    }
}
