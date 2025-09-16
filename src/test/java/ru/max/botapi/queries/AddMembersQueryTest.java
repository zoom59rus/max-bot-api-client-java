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
import ru.max.botapi.model.UserIdsList;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AddMembersQueryTest extends UnitTestBase {
    
    @Test
    public void addMembersTest() throws Exception {
        UserIdsList userIdsList = new UserIdsList(Arrays.asList(1L, 2L, 3L));
        Long chatId = 3L;
        SimpleQueryResult response = api.addMembers(userIdsList, chatId).execute();
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void shouldThrowException() throws Exception {
        api.addMembers(null, 1L).execute();
    }

    @Test
    public void shouldThrowException2() throws Exception {
        api.addMembers(new UserIdsList(Arrays.asList(1L, 2L)), null).execute();
    }
}
