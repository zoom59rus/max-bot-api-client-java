package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.*;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class MessageRemovedUpdateTest extends GetUpdatesIntegrationTest {
    @Test
    public void shouldGetUpdateInChat() throws Exception {
        test("MessageRemovedUpdateTest#shouldGetUpdatesInChat");
    }

    @Test
    public void shouldGetUpdateInPublicChat() throws Exception {
        test("MessageRemovedUpdateTest#shouldGetUpdatesInPublicChat");
    }

    @Test
    public void shouldGetUpdateInPrivateChannel() throws Exception {
        test("MessageRemovedUpdateTest#shouldGetUpdatesInPrivateChannel");
    }

    @Test
    public void shouldGetUpdateInPublicChannel() throws Exception {
        test("MessageRemovedUpdateTest#shouldGetUpdatesInPublicChannel");
    }

    private void test(String chatTitle) throws Exception {
        Chat commonChat = getByTitle(getChats(), chatTitle);
        Long chatId = commonChat.getChatId();

        ArrayBlockingQueue<MessageRemovedUpdate> bot1receivedUpdates = new ArrayBlockingQueue<>(2);
        ArrayBlockingQueue<MessageRemovedUpdate> bot3receivedUpdates = new ArrayBlockingQueue<>(2);
        Update.Visitor bot3updatesConsumer = new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
            @Override
            public void visit(MessageCreatedUpdate model) {
                // ignore
            }

            @Override
            public void visit(MessageRemovedUpdate model) {
                bot3receivedUpdates.offer(model);
            }
        };

        FailByDefaultUpdateVisitor bot1updates = new Bot1ToBot3RedirectingUpdateVisitor(bot3updatesConsumer) {
            @Override
            protected void onMessageCreated(MessageCreatedUpdate model) {
                // ignore
            }

            @Override
            public void visit(MessageRemovedUpdate model) {
                bot1receivedUpdates.offer(model);
            }
        };

        try (AutoCloseable ignored = MaxIntegrationTest.bot1.addConsumer(chatId, bot1updates)) {
            SendMessageResult result = doSend(MaxIntegrationTest.client2, new NewMessageBody(randomText(), null, null), chatId);
            String mid1 = result.getMessage().getBody().getMid();
            // bot2 deletes its own message
            new DeleteMessageQuery(MaxIntegrationTest.client2, mid1).execute();
            // bot1 received update
            MessageRemovedUpdate update1 = Objects.requireNonNull(bot1receivedUpdates.poll(30, TimeUnit.SECONDS),
                    "update1");
            assertThat(update1.getMessageId(), is(mid1));
            assertThat(update1.getChatId(), is(chatId));
            assertThat(update1.getUserId(), is(MaxIntegrationTest.bot2.getUserId()));
            // bot3 received webhook-update
            MessageRemovedUpdate bot3update1 = Objects.requireNonNull(bot3receivedUpdates.poll(30, TimeUnit.SECONDS),
                    "bot3update1");
            assertThat(bot3update1.getMessageId(), is(mid1));
            assertThat(bot3update1.getChatId(), is(chatId));
            assertThat(bot3update1.getUserId(), is(MaxIntegrationTest.bot2.getUserId()));

            SendMessageResult result2 = doSend(MaxIntegrationTest.client2, new NewMessageBody(randomText(), null, null), chatId);
            String mid2 = result2.getMessage().getBody().getMid();
            // bot3 deletes bot2 message
            new DeleteMessageQuery(MaxIntegrationTest.client3, mid2).execute();
            MessageRemovedUpdate update2 = Objects.requireNonNull(bot1receivedUpdates.poll(30, TimeUnit.SECONDS),
                    "update2");
            assertThat(update2.getMessageId(), is(mid2));
            assertThat(update2.getChatId(), is(chatId));
            assertThat(update2.getUserId(), is(MaxIntegrationTest.bot3.getUserId()));

            SendMessageResult result3 = doSend(MaxIntegrationTest.client, new NewMessageBody(randomText(), null, null), chatId);
            String mid3 = result3.getMessage().getBody().getMid();
            // bot2 deletes bot1 message. bot3 should receive webhook-update
            new DeleteMessageQuery(MaxIntegrationTest.client2, mid3).execute();
            MessageRemovedUpdate bot3update2 = Objects.requireNonNull(bot3receivedUpdates.poll(30, TimeUnit.SECONDS),
                    "bot3update1");
            assertThat(bot3update2.getMessageId(), is(mid3));
            assertThat(bot3update2.getChatId(), is(chatId));
            assertThat(bot3update2.getUserId(), is(MaxIntegrationTest.bot2.getUserId()));
        }
    }
}