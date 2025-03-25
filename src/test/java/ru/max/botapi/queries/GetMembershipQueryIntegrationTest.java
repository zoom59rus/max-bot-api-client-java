package ru.max.botapi.queries;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ChatAdminPermission;
import ru.max.botapi.model.ChatMember;
import ru.max.botapi.model.ChatType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;


public class GetMembershipQueryIntegrationTest extends MaxIntegrationTest {
    @Test(expected = APIException.class)
    public void shouldNotReturnPermissionsForDialog() throws Exception {
        Chat dialog = getByType(getChats(), ChatType.DIALOG);
        new GetMembershipQuery(client, dialog.getChatId()).execute();
    }

    @Test
    public void shouldReturnPermissionsForChatWhereBotIsAdmin() throws Exception {
        Chat chat = getByTitle(getChats(), "test chat #1");
        GetMembershipQuery query = new GetMembershipQuery(client, chat.getChatId());
        ChatMember chatMember = query.execute();
        assertThat(chatMember.getPermissions().size(), is(greaterThan(0)));
        assertThat(chatMember.getPermissions(), hasItem(ChatAdminPermission.WRITE));
        assertThat(chatMember.isBot(), is(true));
    }

    @Test
    public void shouldReturnNullForChatWhereBotIsNOTAdmin() throws Exception {
        Chat chat = getByTitle(getChats(), "test chat #3");
        GetMembershipQuery query = new GetMembershipQuery(client, chat.getChatId());
        ChatMember chatMember = query.execute();
        assertThat(chatMember.getPermissions(), is(nullValue()));
    }
}