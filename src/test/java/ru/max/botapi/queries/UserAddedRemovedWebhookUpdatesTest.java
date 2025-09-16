package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.NoopUpdateVisitor;
import ru.max.botapi.VisitedUpdatesTracer;
import ru.max.botapi.model.*;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class UserAddedRemovedWebhookUpdatesTest extends GetUpdatesIntegrationTest {
    @Test
    public void shouldGetUpdatesInChat() throws Exception {
        test("UserAddedRemovedWebhookUpdatesTest#shouldGetUpdatesInChat", false);
    }

    @Test
    public void shouldGetUpdatesInChannel() throws Exception {
        test("UserAddedRemovedWebhookUpdatesTest#shouldGetUpdatesInChannel", true);
    }

    private void test(String chatTitle, boolean isChannel) throws Exception {
        Chat commonChat = getByTitle(getChats(), chatTitle);
        Long commonChatId = commonChat.getChatId();
        User bot2user = new User(bot2.getUserId(), bot2.getFirstName(), null, bot2.getUsername(), true,
                System.currentTimeMillis());

        CountDownLatch bot2added = new CountDownLatch(1);
        CountDownLatch bot2removed = new CountDownLatch(1);
        CountDownLatch bot3expectedUpdates = new CountDownLatch(2);
        VisitedUpdatesTracer bot3updates = new VisitedUpdatesTracer(new NoopUpdateVisitor() {
            @Override
            public void visit(UserAddedToChatUpdate model) {
                assertThat(model.getChatId(), is(commonChatId));
                assertThat(model.getInviterId(), is(bot1.getUserId()));
                assertUser(model.getUser(), bot2user);
                assertThat(model.isChannel(), is(isChannel));
                bot3expectedUpdates.countDown();
            }

            @Override
            public void visit(UserRemovedFromChatUpdate model) {
                assertThat(model.getChatId(), is(commonChatId));
                assertThat(model.getAdminId(), is(bot1.getUserId()));
                assertUser(model.getUser(), bot2user);
                assertThat(model.isChannel(), is(isChannel));
                bot3expectedUpdates.countDown();
            }
        });

        FailByDefaultUpdateVisitor visitor = new FailByDefaultUpdateVisitor(bot1) {
            @Override
            public void visit(BotAddedToChatUpdate model) {
                bot2added.countDown();
            }

            @Override
            public void visit(BotRemovedFromChatUpdate model) {
                bot2removed.countDown();
            }
        };

        try (AutoCloseable ignored = bot2.addConsumer(commonChatId, visitor);
             AutoCloseable ignored2 = addBot3Consumer(bot3updates)) {
            try {
                addUser(client, commonChatId, bot2.getUserId());
                await(bot2added);
            } finally {
                removeUser(client, commonChatId, bot2.getUserId());
                await(bot2removed);
            }

            await(bot3expectedUpdates);
        }
    }
}