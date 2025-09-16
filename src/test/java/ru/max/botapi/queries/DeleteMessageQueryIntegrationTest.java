package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;


public class DeleteMessageQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldDeleteItsOwnMessageInDialog() throws Exception {
        List<Chat> chats = getChats();
        Chat dialog = getByType(chats, ChatType.DIALOG);
        SendMessageResult result = botAPI.sendMessage(new NewMessageBody("to be deleted", null, null))
                .chatId(dialog.getChatId())
                .execute();

        String messageIdToDelete = result.getMessage().getBody().getMid();
        SimpleQueryResult deleteResult = new DeleteMessageQuery(client, messageIdToDelete).execute();
        assertThat(deleteResult.isSuccess(), is(true));

        assertDeleted(dialog.getChatId(), messageIdToDelete);
    }

    @Test
    public void shouldDeleteMessagesInChat() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = chats.stream()
                .filter(c -> "test chat #1".equals(c.getTitle()))
                .findFirst()
                .orElseThrow(() -> new Exception("Couldn't find chat"));

        Long chatId = chat.getChatId();
        SendMessageResult result1 = new SendMessageQuery(client, new NewMessageBody("bot 1 message", null, null))
                .chatId(chatId).execute();

        SendMessageResult result2 = new SendMessageQuery(client2, new NewMessageBody("bot 2 message", null, null))
                .chatId(chatId).execute();

        new DeleteMessageQuery(client, result1.getMessage().getBody().getMid()).execute();
        new DeleteMessageQuery(client, result2.getMessage().getBody().getMid()).execute();

        assertDeleted(chatId, result1.getMessage().getBody().getMid(), result2.getMessage().getBody().getMid());
    }

    @Test
    public void shouldThrowExceptionWhenBotHasNoPermission() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = chats.stream()
                .filter(c -> "test chat #2".equals(c.getTitle()))
                .findFirst()
                .orElseThrow(() -> new Exception("Couldn't find chat"));

        Long chatId = chat.getChatId();
        SendMessageResult bot2Message = new SendMessageQuery(client2, new NewMessageBody("bot 2 message", null, null))
                .chatId(chatId).execute();

        new DeleteMessageQuery(client, bot2Message.getMessage().getBody().getMid()).execute();
    }

    @Test
    public void shouldThrowExceptionWhenBotIsNotAdmin() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = chats.stream()
                .filter(c -> "test chat #3".equals(c.getTitle()))
                .findFirst()
                .orElseThrow(() -> new Exception("Couldn't find chat"));

        Long chatId = chat.getChatId();

        SendMessageResult bot2Message = new SendMessageQuery(client2, new NewMessageBody("bot 2 message", null, null))
                .chatId(chatId).execute();

        new DeleteMessageQuery(client, bot2Message.getMessage().getBody().getMid()).execute();
    }

    @Test
    public void shouldThrowExceptionWhenMIDIsInvalid() throws Exception {
        new DeleteMessageQuery(client, "mid.asdash9237498kjsdhfkjsehe2763478").execute();
    }

    private void assertDeleted(Long chatId, String... messageIdToDelete) throws Exception {
        MessageList messagesInDialog = botAPI.getMessages().chatId(chatId).count(5).execute();
        Set<String> messageIds = messagesInDialog.getMessages()
                .stream().map(m -> m.getBody().getMid())
                .collect(Collectors.toSet());

        assertThat(messageIds, not(contains(messageIdToDelete)));
    }
}