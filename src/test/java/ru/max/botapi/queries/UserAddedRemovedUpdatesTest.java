package ru.max.botapi.queries;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.NoopUpdateVisitor;
import ru.max.botapi.VisitedUpdatesTracer;
import ru.max.botapi.model.BotAddedToChatUpdate;
import ru.max.botapi.model.BotRemovedFromChatUpdate;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.Update;
import ru.max.botapi.model.User;
import ru.max.botapi.model.UserAddedToChatUpdate;
import ru.max.botapi.model.UserRemovedFromChatUpdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class UserAddedRemovedUpdatesTest extends GetUpdatesIntegrationTest {
    @Test
    public void shouldGetUpdatesInChat() throws Exception {
        test("UserAddedRemovedUpdatesTest#shouldGetUpdatesInChat", false);
    }

    @Test
    public void shouldGetUpdatesInChannel() throws Exception {
        test("UserAddedRemovedUpdatesTest#shouldGetUpdatesInChannel", true);
    }

    private void test(String chatTitle, boolean isChannel) throws Exception {
        Chat commonChat = getByTitle(getChats(), chatTitle);
        Long commonChatId = commonChat.getChatId();
        User bot3user = new User(MaxIntegrationTest.bot3.getUserId(), MaxIntegrationTest.bot3.getFirstName(), null, MaxIntegrationTest.bot3.getUsername(), true,
                System.currentTimeMillis());

        CountDownLatch bot3added = new CountDownLatch(1);
        CountDownLatch bot3removed = new CountDownLatch(1);
        VisitedUpdatesTracer bot2updates = new VisitedUpdatesTracer(new NoopUpdateVisitor() {
            @Override
            public void visit(UserAddedToChatUpdate model) {
                assertThat(model.getChatId(), is(commonChatId));
                assertThat(model.getInviterId(), is(MaxIntegrationTest.bot1.getUserId()));
                MaxIntegrationTest.assertUser(model.getUser(), bot3user);
                assertThat(model.isChannel(), is(isChannel));
                bot3added.countDown();
            }

            @Override
            public void visit(UserRemovedFromChatUpdate model) {
                assertThat(model.getChatId(), is(commonChatId));
                assertThat(model.getAdminId(), is(MaxIntegrationTest.bot1.getUserId()));
                MaxIntegrationTest.assertUser(model.getUser(), bot3user);
                assertThat(model.isChannel(), is(isChannel));
                bot3removed.countDown();
            }
        });

        CountDownLatch bot3updates = new CountDownLatch(2);
        Update.Visitor bot3updatesConsumer = new NoopUpdateVisitor() {
            @Override
            public void visit(BotAddedToChatUpdate model) {
                bot3updates.countDown();
            }

            @Override
            public void visit(BotRemovedFromChatUpdate model) {
                bot3updates.countDown();
            }
        };

        try (AutoCloseable ignored = MaxIntegrationTest.bot2.addConsumer(commonChatId, bot2updates);
             AutoCloseable ignored2 = addBot3Consumer(bot3updatesConsumer)) {
            try {
                addUser(MaxIntegrationTest.client, commonChatId, MaxIntegrationTest.bot3.getUserId());
                MaxIntegrationTest.await(bot3added);
            } finally {
                removeUser(MaxIntegrationTest.client, commonChatId, MaxIntegrationTest.bot3.getUserId());
                MaxIntegrationTest.await(bot3removed);
            }

            MaxIntegrationTest.await(bot3updates);
        }
    }

}