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
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.SendMessageResult;

import static ru.max.botapi.client.MaxTransportClient.Method;

public class SendMessageQuery extends MaxQuery<SendMessageResult> {
    public static final String PATH_TEMPLATE = "/messages";
    public final QueryParam<Long> userId = new QueryParam<>("user_id", this);
    public final QueryParam<Long> chatId = new QueryParam<>("chat_id", this);
    public final QueryParam<Boolean> disableLinkPreview = new QueryParam<>("disable_link_preview", this);

    public SendMessageQuery(MaxClient client, NewMessageBody newMessageBody) {
        super(client, PATH_TEMPLATE, newMessageBody, SendMessageResult.class, Method.POST);
    }

    public SendMessageQuery userId(Long value) {
        this.userId.setValue(value);
        return this;
    }

    public SendMessageQuery chatId(Long value) {
        this.chatId.setValue(value);
        return this;
    }

    public SendMessageQuery disableLinkPreview(Boolean value) {
        this.disableLinkPreview.setValue(value);
        return this;
    }
}
