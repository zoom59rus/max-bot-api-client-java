package ru.max.botapi.queries;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ChatAdminPermission;
import ru.max.botapi.model.ChatMember;
import ru.max.botapi.model.ChatMembersList;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


public class GetAdminsQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldReturnAllAdmins() throws Exception {
        Chat chat = getByTitle(getChats(), "GetAdminsQueryIntegrationTest#shouldReturnAllAdmins");
        ChatMembersList membersList = new GetAdminsQuery(client, chat.getChatId()).execute();
        List<ChatMember> admins = membersList.getMembers();

        assertThat(membersList.getMarker(), is(nullValue()));
        assertThat(admins.size(), is(3));

        Map<Long, ChatMember> adminsById = admins.stream().collect(Collectors.toMap(ChatMember::getUserId, Function.identity()));
        assertThat(adminsById.get(bot1.getUserId()), is(notNullValue()));
        assertThat(adminsById.get(bot2.getUserId()), is(notNullValue()));
        assertThat(adminsById.get(bot2.getUserId()).getPermissions(), hasItem(ChatAdminPermission.READ_ALL_MESSAGES));
    }

    @Test
    public void shouldThrowExceptionIfCurrentBotIsNotAdmin() throws Exception {
        Chat chat = getByTitle(getChats(), "GetAdminsQueryIntegrationTest#shouldThrowExceptionIfCurrentBotIsNotAdmin");
        try {
            new GetAdminsQuery(client, chat.getChatId()).execute();
        } catch (APIException e) {
            assertThat(e.getStatusCode(), is(403));
        }
    }

    @Test
    public void shouldThrowExceptionIfCurrentBotIsNotMember() throws Exception {
        Chat chat = getByTitle(getChats(client2),
                "GetAdminsQueryIntegrationTest#shouldThrowExceptionIfCurrentBotIsNotMember");
        try {
            new GetAdminsQuery(client, chat.getChatId()).execute();
        } catch (APIException e) {
            assertThat(e.getStatusCode(), is(403));
        }
    }

    @Test
    public void shouldThrowExceptionIfInvalidChatIsPassed() throws Exception {
        try {
            new GetAdminsQuery(client, ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE)).execute();
        } catch (APIException e) {
            assertThat(e.getStatusCode(), is(400));
        }
    }
}