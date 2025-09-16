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

import org.junit.jupiter.api.Test;
import ru.max.botapi.model.SimpleQueryResult;
import spark.Spark;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DeleteChatQueryTest extends MaxQueryTest {
    @Test
    public void shouldExecuteQuery() throws Exception {
        Spark.delete("/chats/123", (req, resp) -> new SimpleQueryResult(true), this::serialize);
        Long chatId = 123L;
        DeleteChatQuery query = api.deleteChat(chatId);
        SimpleQueryResult result = query.execute();
        assertThat(result.isSuccess(), is(true));
    }

    @Test
    public void shouldThrow() throws Exception {
        api.deleteChat(null).execute();
    }
    
}
