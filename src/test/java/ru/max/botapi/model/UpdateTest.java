package ru.max.botapi.model;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.max.botapi.TestBot;
import ru.max.botapi.queries.UnitTestBase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;


@Tag("UnitTest")
public class UpdateTest extends UnitTestBase {
    long now = System.currentTimeMillis();
    User user = new User(ID_COUNTER.incrementAndGet(), "user", "name", "username", false, System.currentTimeMillis());
    MessageCreatedUpdate messageCreatedUpdate = new MessageCreatedUpdate(message(ID_COUNTER.incrementAndGet(), null),
            now);
    MessageEditedUpdate messageEditedUpdate = new MessageEditedUpdate(message(ID_COUNTER.incrementAndGet(), null), now);
    MessageRemovedUpdate messageRemovedUpdate = new MessageRemovedUpdate("mid." + ID_COUNTER.incrementAndGet(),
            ID_COUNTER.incrementAndGet(), ID_COUNTER.incrementAndGet(), now);
    Callback callback = new Callback(now, "calbackId", random(users.values())).payload("payload");
    MessageCallbackUpdate messageCallbackUpdate = new MessageCallbackUpdate(
            callback, message(ID_COUNTER.incrementAndGet(), null), now).userLocale("ru-RU");
    UserAddedToChatUpdate userAddedToChatUpdate = new UserAddedToChatUpdate(ID_COUNTER.incrementAndGet(),
            user, false, System.currentTimeMillis()).inviterId(ID_COUNTER.incrementAndGet());
    UserRemovedFromChatUpdate userRemovedFromChatUpdate = new UserRemovedFromChatUpdate(
            ID_COUNTER.incrementAndGet(), user, false, System.currentTimeMillis()).adminId(
            ID_COUNTER.incrementAndGet());
    BotAddedToChatUpdate botAddedToChatUpdate = new BotAddedToChatUpdate(ID_COUNTER.incrementAndGet(),
            user, true,
            System.currentTimeMillis());
    BotRemovedFromChatUpdate botRemovedFromChatUpdate = new BotRemovedFromChatUpdate(ID_COUNTER.incrementAndGet(),
            user, false, System.currentTimeMillis());
    BotStartedUpdate botStartedUpdate = new BotStartedUpdate(ID_COUNTER.incrementAndGet(),
            user, System.currentTimeMillis());
    ChatTitleChangedUpdate chatTitleChangedUpdate = new ChatTitleChangedUpdate(ID_COUNTER.incrementAndGet(),
            user, "title", System.currentTimeMillis());
    MessageChatCreatedUpdate messageChatCreatedUpdate = new MessageChatCreatedUpdate(randomChat(), "mId", now);

    public UpdateTest() throws Exception {
    }


    @Test
    public void shouldVisitDefault() {
        Update update = new Update(System.currentTimeMillis());
        update.visit(new FailByDefaultUpdateVisitor(mock(TestBot.class)) {
            @Override
            public void visitDefault(Update model) {
                assertThat(model, is(update));
            }
        });
    }

    @Test
    public void shouldMapDefault() {
        Update update = new Update(System.currentTimeMillis());
        MappedUpdate map = update.map(new FailByDefaultUpdateMapper<MappedUpdate>() {
            @Override
            public MappedUpdate mapDefault(Update model) {
                return new MappedUpdate(model);
            }
        });

        assertThat(map.update, is(update));
    }

    @Test
    public void shouldMapEveryUpdate() {
        Update.Mapper<MappedUpdate> mapper = new DefaultUpdateMapper<>(MappedUpdate::new);
        assertThat(messageCreatedUpdate.map(mapper).update, is(messageCreatedUpdate));
        assertThat(messageEditedUpdate.map(mapper).update, is(messageEditedUpdate));
        assertThat(messageRemovedUpdate.map(mapper).update, is(messageRemovedUpdate));
        assertThat(userAddedToChatUpdate.map(mapper).update, is(userAddedToChatUpdate));
        assertThat(messageCallbackUpdate.map(mapper).update, is(messageCallbackUpdate));
        assertThat(userRemovedFromChatUpdate.map(mapper).update, is(userRemovedFromChatUpdate));
        assertThat(chatTitleChangedUpdate.map(mapper).update, is(chatTitleChangedUpdate));
        assertThat(botRemovedFromChatUpdate.map(mapper).update, is(botRemovedFromChatUpdate));
        assertThat(botAddedToChatUpdate.map(mapper).update, is(botAddedToChatUpdate));
        assertThat(botStartedUpdate.map(mapper).update, is(botStartedUpdate));
        assertThat(messageChatCreatedUpdate.map(mapper).update, is(messageChatCreatedUpdate));
    }

    private static class MappedUpdate {
        private final Update update;

        private MappedUpdate(Update update) {
            this.update = update;
        }
    }
}