package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.model.*;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;


public class UserLeftJoinedUpdatesTest extends GetUpdatesIntegrationTest {
    @Test
    public void shouldGetUpdatesInChat() throws Exception {
        test("UserLeftJoinedUpdatesTest#shouldGetUpdatesInChat", false);
    }

    @Test
    public void shouldGetUpdatesInPublicChat() throws Exception {
        test("UserLeftJoinedUpdatesTest#shouldGetUpdatesInPublicChat", false);
    }

    @Test
    public void shouldGetUpdatesInPrivateChannel() throws Exception {
        test("UserLeftJoinedUpdatesTest#shouldGetUpdatesInPrivateChannel", true);
    }

    @Test
    public void shouldGetUpdatesInPublicChannel() throws Exception {
        test("UserLeftJoinedUpdatesTest#shouldGetUpdatesInPublicChannel", true);
    }

    private void test(String chatTitle, boolean isChanel) throws Exception {
        Chat commonChat = getByTitle(getChats(), chatTitle);
        Long commonChatId = commonChat.getChatId();
        User bot3user = new User(bot3.getUserId(), bot3.getFirstName(), null, bot3.getUsername(), true,
                System.currentTimeMillis());

        CountDownLatch bot3added = new CountDownLatch(1);
        CountDownLatch bot3removed = new CountDownLatch(1);
        FailByDefaultUpdateVisitor bot1updates = new FailByDefaultUpdateVisitor(bot1) {
            @Override
            public void visit(UserAddedToChatUpdate model) {
                assertThat(model.getChatId(), is(commonChatId));
                assertThat(model.getInviterId(), is(nullValue()));
                assertUser(model.getUser(), bot3user);
                assertThat(model.isChannel(), is(isChanel));
                bot3added.countDown();
            }

            @Override
            public void visit(UserRemovedFromChatUpdate model) {
                assertThat(model.getChatId(), is(commonChatId));
                assertThat(model.getAdminId(), is(nullValue()));
                assertUser(model.getUser(), bot3user);
                assertThat(model.isChannel(), is(isChanel));
                bot3removed.countDown();
            }
        };

        try (AutoCloseable ignored = bot1.addConsumer(commonChatId, bot1updates)) {
            try {
                bot3.joinChat(commonChat.getLink());
                await(bot3added);
            } finally {
                bot3.leaveChat(commonChatId);
                await(bot3removed);
            }
        }
    }
}