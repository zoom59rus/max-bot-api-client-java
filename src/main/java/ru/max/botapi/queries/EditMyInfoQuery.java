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
import ru.max.botapi.model.BotInfo;import ru.max.botapi.model.BotPatch;
import static ru.max.botapi.client.MaxTransportClient.Method;

public class EditMyInfoQuery extends MaxQuery<BotInfo> {
    public static final String PATH_TEMPLATE = "/me";

    public EditMyInfoQuery(MaxClient client, BotPatch botPatch) {
        super(client, PATH_TEMPLATE, botPatch, BotInfo.class, Method.PATCH);
    }
}
