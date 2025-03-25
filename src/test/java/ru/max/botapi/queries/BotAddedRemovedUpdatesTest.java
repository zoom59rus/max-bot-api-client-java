package ru.max.botapi.queries;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.VisitedUpdatesTracer;
import ru.max.botapi.Visitors;
import ru.max.botapi.model.BotAddedToChatUpdate;
import ru.max.botapi.model.BotRemovedFromChatUpdate;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.FailByDefaultUpdateVisitor;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.User;
import ru.max.botapi.model.UserRemovedFromChatUpdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class BotAddedRemovedUpdatesTest extends GetUpdatesIntegrationTest {
    @Test
    public void shouldGetUpdatesInChat() throws Exception {
        test("test chat #8", false);
    }

    @Test
    public void shouldGetUpdatesInPrivateChatWithLink() throws Exception {
        test("BotAddedRemovedUpdatesTest#shouldGetUpdatesInPrivateChatWithLink", false);
    }

    @Test
    public void shouldGetUpdatesInChannel() throws Exception {
        test("BotAddedRemovedUpdatesTest#shouldGetUpdatesInChannel", true);
    }

    private void test(String chatTitle, boolean isChannel) throws Exception {
        Chat commonChat = getByTitle(getChats(), chatTitle);
        Long commonChatId = commonChat.getChatId();
        User bot1user = new User(MaxIntegrationTest.bot1.getUserId(), MaxIntegrationTest.bot1.getFirstName(), null, MaxIntegrationTest.bot1.getUsername(), true,
                System.currentTimeMillis());

        CountDownLatch bot2removed = new CountDownLatch(1);
        CountDownLatch bot3removed = new CountDownLatch(1);

        VisitedUpdatesTracer bot2updates = Visitors.tracing(Visitors.noDuplicates(new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
            @Override
            public void visit(MessageCreatedUpdate model) {
                Long senderId = model.getMessage().getSender().getUserId();
                if (senderId.equals(MaxIntegrationTest.bot3.getUserId())) {
                    return;
                }

                super.visit(model);
            }

            @Override
            public void visit(BotAddedToChatUpdate model) {
                assertThat(model.getChatId(), is(commonChatId));
                assertThat(model.getUser().getUserId(), is(MaxIntegrationTest.bot1.getUserId()));
                assertThat(model.isChannel(), is(isChannel));
                removeUser(MaxIntegrationTest.client, commonChatId, MaxIntegrationTest.bot2.getUserId());
            }

            @Override
            public void visit(BotRemovedFromChatUpdate model) {
                assertThat(model.getChatId(), is(commonChatId));
                MaxIntegrationTest.assertUser(model.getUser(), bot1user);
                assertThat(model.isChannel(), is(isChannel));
                bot2removed.countDown();
            }

            @Override
            public void visit(UserRemovedFromChatUpdate model) {
                // ignoring
            }
        }));

        VisitedUpdatesTracer bot3updates = Visitors.tracing(Visitors.noDuplicates(new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
            @Override
            public void visit(BotAddedToChatUpdate model) {
                MaxIntegrationTest.LOG.info("Bot {} added to chat", model.getUser().getName());
                assertThat(model.getChatId(), is(commonChatId));
                assertThat(model.getUser().getUserId(), is(MaxIntegrationTest.bot1.getUserId()));
                assertThat(model.isChannel(), is(isChannel));
                removeUser(MaxIntegrationTest.client, commonChatId, MaxIntegrationTest.bot3.getUserId());
            }

            @Override
            public void visit(BotRemovedFromChatUpdate model) {
                assertThat(model.getChatId(), is(commonChatId));
                MaxIntegrationTest.assertUser(model.getUser(), bot1user);
                assertThat(model.isChannel(), is(isChannel));
                bot3removed.countDown();
            }
        }));


        try (AutoCloseable ignored = addBot3Consumer(bot3updates);
             AutoCloseable ignore2 = MaxIntegrationTest.bot2.addConsumer(commonChatId, bot2updates)) {
            addUser(MaxIntegrationTest.client, commonChatId, MaxIntegrationTest.bot2.getUserId());
            MaxIntegrationTest.await(bot2removed);

            addUser(MaxIntegrationTest.client, commonChatId, MaxIntegrationTest.bot3.getUserId());
            MaxIntegrationTest.await(bot3removed);
        } finally {
            removeUser(MaxIntegrationTest.client, commonChatId, MaxIntegrationTest.bot2.getUserId());
            removeUser(MaxIntegrationTest.client, commonChatId, MaxIntegrationTest.bot3.getUserId());
        }
    }
}