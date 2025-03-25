package ru.max.botapi.queries;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.exceptions.BadRequestException;
import ru.max.botapi.exceptions.ChatAccessForbiddenException;


public class GetPinnedMessageQueryIntegrationTest extends MaxIntegrationTest {
    @Test(expected = BadRequestException.class)
    public void shouldThrowForDialog() throws Exception {
        botAPI.getPinnedMessage(123L).execute();
    }

    @Test(expected = ChatAccessForbiddenException.class)
    public void shouldThrowForNonExistingChat() throws Exception {
        botAPI.getPinnedMessage(-123L).execute();
    }

    @Test(expected = ChatAccessForbiddenException.class)
    public void shouldThrowIfBotIsNotAdmin() throws Exception {
        long chatId = getByTitle(getChats(client2), "PinMessageQueryIntegrationTest").getChatId();
        new GetPinnedMessageQuery(client2, chatId).execute();
    }
}