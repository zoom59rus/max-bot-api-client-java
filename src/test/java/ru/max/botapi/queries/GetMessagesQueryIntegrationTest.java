package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;


public class GetMessagesQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldGetMessagesByIds() throws Exception {
        String text = "GetMessagesQueryIntegrationTest " + randomText();
        String text2 = "GetMessagesQueryIntegrationTest " + randomText();
        List<Chat> chats = getChats();
        Chat dialog = getByType(chats, ChatType.DIALOG);
        Chat chat = getByTitle(chats, "test chat #1");
        SendMessageResult sendMessageResult = botAPI.sendMessage(new NewMessageBody(text, null, null)).chatId(
                dialog.getChatId()).execute();
        SendMessageResult sendMessageResult2 = botAPI.sendMessage(new NewMessageBody(text2, null, null)).chatId(
                chat.getChatId()).execute();
        Set<String> ids = new HashSet<>();
        ids.add(sendMessageResult.getMessage().getBody().getMid());
        ids.add(sendMessageResult2.getMessage().getBody().getMid());

        MessageList messageList = new GetMessagesQuery(client).messageIds(ids).execute();
        assertThat(messageList.getMessages().stream().map(m -> m.getBody().getText()).collect(Collectors.toSet()),
                hasItems(text, text2));
    }

    @Test
    public void shouldGetMessagesByChatId() throws Exception {
        int count = 5;
        List<Chat> chats = getChats();
        Chat chat = getByTitle(chats, "test chat #5");
        Set<NewMessageBody> newMessages = Stream.generate(() -> new NewMessageBody(randomText(), null, null))
                .limit(count)
                .collect(Collectors.toSet());

        for (NewMessageBody newMessage : newMessages) {
            botAPI.sendMessage(newMessage).chatId(chat.getChatId()).execute();
        }

        List<Message> messages = new GetMessagesQuery(client)
                .chatId(chat.getChatId())
                .count(count).execute().getMessages();

        assertThat(messages.stream().map(m -> m.getBody().getText()).collect(Collectors.toSet()),
                is(newMessages.stream().map(NewMessageBody::getText).collect(Collectors.toSet())));
    }

    @Test
    public void shouldReturnMessageStat() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = getByTitle(chats, "test channel #4");
        new SendMessageQuery(client2, new NewMessageBody(randomText(), null, null)).chatId(chat.getChatId()).execute();

        new GetMessagesQuery(client).chatId(chat.getChatId()).execute();
        MessageList messageList = new GetMessagesQuery(client2).chatId(chat.getChatId()).execute();
        assertThat(messageList.getMessages().get(0).getStat().getViews(), is(greaterThan(0)));
    }

    @Test
    public void shouldReturnNoStatInDialogs() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = getByType(chats, ChatType.DIALOG);
        new SendMessageQuery(client, new NewMessageBody(randomText(), null, null)).chatId(chat.getChatId()).execute();
        MessageList messageList = new GetMessagesQuery(client).chatId(chat.getChatId()).execute();
        assertThat(messageList.getMessages().get(0).getStat(), is(nullValue()));
    }

    @Test
    public void shouldGetAllMessagesInDialog() throws Exception {
        List<Chat> chats = getChats();
        Chat dialog = getBy(chats, c -> c.getType() == ChatType.DIALOG && c.getChatId() != (bot1.getUserId() ^ bot3.getUserId()));

        List<String> posted = new ArrayList<>();
        Thread.sleep(1000);
        long start = now();
        Long dialogChatId = dialog.getChatId();
        for (int i = 0; i < 30; i++) {
            String text = randomText();
            new SendMessageQuery(client, new NewMessageBody(text, null, null)).chatId(dialogChatId).execute();
            posted.add(text);
        }

        long from = now();
        List<String> fetched = new ArrayList<>();
        do {
            MessageList messageList = new GetMessagesQuery(client).chatId(dialogChatId).count(10).from(from).to(start).execute();
            List<Message> messages = messageList.getMessages();
            if (messages.isEmpty()) {
                break;
            }

            from = messages.get(messages.size() - 1).getTimestamp() - 1;
            for (Message message : messages) {
                fetched.add(message.getBody().getText());
            }
        } while (from > start);

        Collections.reverse(posted);
        assertThat(fetched, is(posted));
    }

    @Test
    public void shouldHasURLInPublicChats() throws Exception {
        List<Chat> allChats = getChats();
        Chat publicChat = getByTitle(allChats, "GetMessagesQueryIntegrationTest#publicChat");
        Chat publicChannel = getByTitle(allChats, "GetMessagesQueryIntegrationTest#publicChannel");

        NewMessageBody nmb = new NewMessageBody(randomText(), null, null);
        List<Message> messages = send(nmb, Arrays.asList(publicChannel, publicChat));
        for (Message message : messages) {
            assertThat(message.getUrl().length(), is(greaterThan(0)));
        }
    }

    @Test
    public void shouldThrowExceptionOnInvalidMessageId() throws Exception {
        MessageList messageList = new GetMessagesQuery(client).messageIds(
                Collections.singleton("mid.nonexistingmessageid123456789012")).execute();

        assertThat(messageList.getMessages(), is(empty()));
    }
}