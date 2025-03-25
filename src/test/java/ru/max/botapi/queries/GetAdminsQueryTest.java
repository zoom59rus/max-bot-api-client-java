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

import ru.max.botapi.exceptions.RequiredParameterMissingException;
import ru.max.botapi.model.ChatMembersList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetAdminsQueryTest extends UnitTestBase {

    @Test
    public void getAdminsTest() throws Exception {
        Long chatId = ID_COUNTER.incrementAndGet();
        GetAdminsQuery query = api.getAdmins(chatId);
        ChatMembersList response = query.execute();
        assertThat(response.getMembers(), is(notNullValue()));
        assertThat(response.getMembers().size(), is(greaterThan(0)));
    }

    @Test(expected = RequiredParameterMissingException.class)
    public void shouldThrow() throws Exception {
        api.getAdmins(null).execute();
    }
}
