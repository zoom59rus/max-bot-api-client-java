package ru.max.botapi.queries;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.exceptions.BadRequestException;
import ru.max.botapi.exceptions.ChatAccessForbiddenException;
import ru.max.botapi.model.Message;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.PinMessageBody;
import ru.max.botapi.model.SimpleQueryResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class PinMessageQueryIntegrationTest extends MaxIntegrationTest {
    @Test(expected = BadRequestException.class)
    public void shouldThrowForDialog() throws Exception {
        botAPI.pinMessage(new PinMessageBody("mid123"), 123L).execute();
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowForNonExistingChat() throws Exception {
        botAPI.pinMessage(new PinMessageBody("mid123"), -123L).execute();
    }

    @Test(expected = ChatAccessForbiddenException.class)
    public void shouldThrowIfBotIsNotAdmin() throws Exception {
        long chatId = getByTitle(getChats(client2), "PinMessageQueryIntegrationTest").getChatId();
        String mid = doSend(new NewMessageBody(randomText(), null, null), chatId).getMessage().getBody().getMid();
        new PinMessageQuery(client2, new PinMessageBody(mid), chatId).execute();
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowIfMessageIdIsInvalid() throws Exception {
        long chatId = getByTitle(getChats(), "PinMessageQueryIntegrationTest").getChatId();
        botAPI.pinMessage(new PinMessageBody("mid123"), chatId).execute();
    }

    @Test
    public void shouldPin() throws Exception {
        long chatId = getByTitle(getChats(), "PinMessageQueryIntegrationTest").getChatId();
        Message message = doSend(new NewMessageBody(randomText(), null, null), chatId).getMessage();
        SimpleQueryResult result = botAPI.pinMessage(new PinMessageBody(message.getBody().getMid()), chatId).execute();
        assertThat(result.isSuccess(), is(true));

        Message pinned = botAPI.getPinnedMessage(chatId).execute().getMessage();
        assertThat(pinned, is(message));
    }
}