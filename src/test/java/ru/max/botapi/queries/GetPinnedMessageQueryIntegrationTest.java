package ru.max.botapi.queries;


import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;


public class GetPinnedMessageQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldThrowForDialog() throws Exception {
        botAPI.getPinnedMessage(123L).execute();
    }

    @Test
    public void shouldThrowForNonExistingChat() throws Exception {
        botAPI.getPinnedMessage(-123L).execute();
    }

    @Test
    public void shouldThrowIfBotIsNotAdmin() throws Exception {
        long chatId = getByTitle(getChats(client2), "PinMessageQueryIntegrationTest").getChatId();
        new GetPinnedMessageQuery(client2, chatId).execute();
    }
}