package ru.max.botapi.queries;



import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ChatType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class GetChatByLinkQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldReturn404() throws Exception {
        try {
            botAPI.getChatByLink("nonExistingLink" + System.currentTimeMillis()).execute();
        } catch (APIException e) {
            assertThat(e.getStatusCode(), is(404));
        }
    }

    @Test
    public void shouldReturnDialogWithUser() throws Exception {
        Chat chat = botAPI.getChatByLink("MasterBot").execute();
        assertThat(chat.getType(), is(ChatType.DIALOG));
        assertThat(chat.getDialogWithUser().getUsername().toLowerCase(), is("MasterBot"));
    }

    @Test
    public void shouldReturnChat() throws Exception {
        Chat chat = botAPI.getChatByLink("helpchat").execute();
        assertThat(chat.getType(), is(ChatType.CHAT));
        assertThat(chat.isPublic(), is(true));
        assertThat(chat.getLink(), is("https://max.ru/helpchat"));
    }

    @Test
    public void shouldReturnChat2() throws Exception {
        Chat chat = botAPI.getChatByLink("@helpchat").execute();
        assertThat(chat.getType(), is(ChatType.CHAT));
        assertThat(chat.isPublic(), is(true));
        assertThat(chat.getLink(), is("https://max.ru/helpchat"));
    }

    @Test
    public void shouldReturnChannel() throws Exception {
        Chat chat = botAPI.getChatByLink("botapichannel").execute();
        assertThat(chat.getType(), is(ChatType.CHANNEL));
        assertThat(chat.isPublic(), is(true));
        assertThat(chat.getLink(), is("https://max.ru/botapichannel"));
    }
}