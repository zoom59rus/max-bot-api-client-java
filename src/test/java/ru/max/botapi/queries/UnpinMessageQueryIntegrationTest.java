package ru.max.botapi.queries;


import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.PinMessageBody;
import ru.max.botapi.model.SimpleQueryResult;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class UnpinMessageQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldThrowForDialog() throws Exception {
        botAPI.unpinMessage(123L).execute();
    }

    @Test
    public void shouldThrowForNonExistingChat() throws Exception {
        botAPI.unpinMessage(-123L).execute();
    }

    @Test
    public void shouldThrowIfBotIsNotAdmin() throws Exception {
        long chatId = getByTitle(getChats(client2), "PinMessageQueryIntegrationTest").getChatId();
        String mid = doSend(new NewMessageBody(randomText(), null, null), chatId).getMessage().getBody().getMid();
        botAPI.pinMessage(new PinMessageBody(mid), chatId).execute();
        new UnpinMessageQuery(client2, chatId).execute();
    }

    @Test
    public void shouldUnpin() throws Exception {
        long chatId = getByTitle(getChats(client2), "PinMessageQueryIntegrationTest").getChatId();
        String mid = doSend(new NewMessageBody(randomText(), null, null), chatId).getMessage().getBody().getMid();
        botAPI.pinMessage(new PinMessageBody(mid), chatId).execute();
        botAPI.unpinMessage(chatId).execute();
        assertThat(botAPI.getPinnedMessage(chatId).execute().getMessage(), is(nullValue()));

        SimpleQueryResult secondUnpin = botAPI.unpinMessage(chatId).execute();
        assertThat(secondUnpin.isSuccess(), is(false));
    }
}