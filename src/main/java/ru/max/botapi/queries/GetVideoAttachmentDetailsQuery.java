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
import ru.max.botapi.model.VideoAttachmentDetails;
import static ru.max.botapi.client.MaxTransportClient.Method;

public class GetVideoAttachmentDetailsQuery extends MaxQuery<VideoAttachmentDetails> {
    public static final String PATH_TEMPLATE = "/videos/{videoToken}";

    public GetVideoAttachmentDetailsQuery(MaxClient client, String videoToken) {
        super(client, substitute(PATH_TEMPLATE, videoToken), null, VideoAttachmentDetails.class, Method.GET);
    }
}
