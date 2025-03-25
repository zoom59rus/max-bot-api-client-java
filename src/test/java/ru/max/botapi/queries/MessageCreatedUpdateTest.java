package ru.max.botapi.queries;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.FailByDefaultUpdateVisitor;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.SendMessageResult;
import ru.max.botapi.model.Update;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;


public class MessageCreatedUpdateTest extends GetUpdatesIntegrationTest {
    @Test
    public void shouldGetUpdateInPublicChat() throws Exception {
        test("MessageCreatedUpdateTest#shouldGetUpdatesInPublicChat");
    }

    @Test
    public void shouldGetUpdateInPublicChannel() throws Exception {
        test("MessageCreatedUpdateTest#shouldGetUpdatesInPublicChannel");
    }

    private void test(String chatTitle) throws Exception {
        Chat commonChat = getByTitle(getChats(), chatTitle);
        Long chatId = commonChat.getChatId();

        ArrayBlockingQueue<MessageCreatedUpdate> bot1receivedUpdates = new ArrayBlockingQueue<>(2);
        ArrayBlockingQueue<MessageCreatedUpdate> bot3receivedUpdates = new ArrayBlockingQueue<>(2);
        Update.Visitor bot3updatesConsumer = new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
            @Override
            public void visit(MessageCreatedUpdate model) {
                bot3receivedUpdates.offer(model);
            }
        };

        FailByDefaultUpdateVisitor bot1updates = new Bot1ToBot3RedirectingUpdateVisitor(bot3updatesConsumer) {
            @Override
            protected void onMessageCreated(MessageCreatedUpdate model) {
                bot1receivedUpdates.offer(model);
            }
        };

        try (AutoCloseable ignored = MaxIntegrationTest.bot1.addConsumer(chatId, bot1updates)) {
            SendMessageResult result = doSend(MaxIntegrationTest.client2, new NewMessageBody(randomText(), null, null), chatId);
            assertThat(result.getMessage().getUrl(), startsWith("https://"));
            
            // bot1 received update
            MessageCreatedUpdate update1 = bot1receivedUpdates.poll(30, TimeUnit.SECONDS);
            assertThat(update1.getMessage().getUrl(), is(result.getMessage().getUrl()));

            // bot3 received
            MessageCreatedUpdate bot3update = bot3receivedUpdates.poll(30, TimeUnit.SECONDS);
            assertThat(bot3update.getMessage().getUrl(), is(result.getMessage().getUrl()));

        }
    }
}