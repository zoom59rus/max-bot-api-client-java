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

import org.junit.Test;

import ru.max.botapi.model.PinMessageBody;
import ru.max.botapi.model.SimpleQueryResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PinMessageQueryTest extends UnitTestBase {
    
    @Test
    public void pinMessageTest() throws Exception {
        Long chatId = ID_COUNTER.incrementAndGet();
        String messageId = "mid123";
        PinMessageBody body = new PinMessageBody(messageId).notify(true);
        PinMessageQuery query = new PinMessageQuery(client, body, chatId);
        SimpleQueryResult response = query.execute();

        assertThat(response.isSuccess(), is(true));
    }
    
}
