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

import ru.max.botapi.model.ChatAdmin;
import ru.max.botapi.model.ChatAdminPermission;
import ru.max.botapi.model.ChatAdminsList;
import ru.max.botapi.model.SimpleQueryResult;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collections;
import java.util.EnumSet;

public class PostAdminsQueryTest extends MaxQueryTest {
    
    @Test
    public void postAdminsTest() throws Exception {
        ChatAdmin chatAdmin = new ChatAdmin(123L, EnumSet.allOf(ChatAdminPermission.class));
        ChatAdminsList chatAdminsList = new ChatAdminsList(Collections.singletonList(chatAdmin));
        Long chatId = 999L;
        PostAdminsQuery query = new PostAdminsQuery(client, chatAdminsList, chatId);
        SimpleQueryResult response = query.execute();

        assertThat(response.isSuccess(), is(true));
    }
    
}
