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
import ru.max.botapi.model.ChatMembersList;

import java.util.Collection;
import java.util.Set;

import static ru.max.botapi.client.MaxTransportClient.Method;

public class GetMembersQuery extends MaxQuery<ChatMembersList> {
    public static final String PATH_TEMPLATE = "/chats/{chatId}/members";
    public final QueryParam<Collection<Long>> userIds = new CollectionQueryParam<>("user_ids", this);
    public final QueryParam<Long> marker = new QueryParam<>("marker", this);
    public final QueryParam<Integer> count = new QueryParam<>("count", this);

    public GetMembersQuery(MaxClient client, Long chatId) {
        super(client, substitute(PATH_TEMPLATE, chatId), null, ChatMembersList.class, Method.GET);
    }

    public GetMembersQuery userIds(Set<Long> value) {
        this.userIds.setValue(value);
        return this;
    }

    public GetMembersQuery marker(Long value) {
        this.marker.setValue(value);
        return this;
    }

    public GetMembersQuery count(Integer value) {
        this.count.setValue(value);
        return this;
    }
}
