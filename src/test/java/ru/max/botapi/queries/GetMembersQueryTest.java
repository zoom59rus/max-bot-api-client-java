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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Test;

import ru.max.botapi.exceptions.RequiredParameterMissingException;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ChatList;
import ru.max.botapi.model.ChatMember;
import ru.max.botapi.model.ChatMembersList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetMembersQueryTest extends UnitTestBase {
    
    @Test
    public void getMembersTest() throws Exception {
        ChatList chatList = api.getChats().count(1).execute();
        Chat chat = chatList.getChats().get(0);
        Long chatId = chat.getChatId();
        Long marker = null;
        Integer count = 5;
        List<ChatMember> members = new ArrayList<>();
        do {
            ChatMembersList response = api.getMembers(chatId)
                    .count(count)
                    .marker(marker)
                    .userIds(new LinkedHashSet<>(Arrays.asList(1L, 2L)))
                    .execute();
            marker = response.getMarker();
            members.addAll(response.getMembers());
        } while (marker != null);

        assertThat(members.size(), is(chat.getParticipantsCount()));
        for (ChatMember member : members) {
            assertThat(member.isOwner(), is(notNullValue()));
            assertThat(member.getLastAccessTime(), is(notNullValue()));
        }
    }

    @Test(expected = RequiredParameterMissingException.class)
    public void shouldThrowException() throws Exception {
        api.getMembers(null).execute();
    }
}
