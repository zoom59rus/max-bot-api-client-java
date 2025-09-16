/*
 * ------------------------------------------------------------------------
 * Max chat Bot API
 * ------------------------------------------------------------------------
 * Copyright (C) 2025 COMMUNICATION PLATFORM LLC
 * ------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */

package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.TestBot;
import ru.max.botapi.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static spark.Spark.get;

public class GetUpdatesQueryTest extends UnitTestBase {

    @Test
    public void getUpdatesTest() throws Exception {
        Chat randomChat = random(chats.values());
        long now = System.currentTimeMillis();
        User user = new User(ID_COUNTER.incrementAndGet(), "user", "name", "username", false, System.currentTimeMillis());
        MessageCreatedUpdate messageCreatedUpdate = new MessageCreatedUpdate(message(randomChat.getChatId(), null),
                now);
        MessageEditedUpdate messageEditedUpdate = new MessageEditedUpdate(message(randomChat.getChatId(), null), now);
        MessageRemovedUpdate messageRemovedUpdate = new MessageRemovedUpdate("mid." + ID_COUNTER.incrementAndGet(),
                ID_COUNTER.incrementAndGet(), ID_COUNTER.incrementAndGet(), now);
        Callback callback = new Callback(now, "calbackId", random(users.values())).payload("payload");
        MessageCallbackUpdate messageCallbackUpdate = new MessageCallbackUpdate(
                callback, message(randomChat.getChatId(), null), now).userLocale("ru-RU");
        UserAddedToChatUpdate userAddedToChatUpdate = new UserAddedToChatUpdate(ID_COUNTER.incrementAndGet(),
                user, false, System.currentTimeMillis()).inviterId(ID_COUNTER.incrementAndGet());
        UserRemovedFromChatUpdate userRemovedFromChatUpdate = new UserRemovedFromChatUpdate(
                ID_COUNTER.incrementAndGet(), user, false, System.currentTimeMillis()).adminId(ID_COUNTER.incrementAndGet());
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

        List<Update> updates = Arrays.asList(
                messageCreatedUpdate,
                messageEditedUpdate,
                messageRemovedUpdate,
                messageCallbackUpdate,
                userAddedToChatUpdate,
                userRemovedFromChatUpdate,
                botAddedToChatUpdate,
                botRemovedFromChatUpdate,
                botStartedUpdate,
                chatTitleChangedUpdate,
                messageChatCreatedUpdate
        );

        get("/updates", (request, response) -> new UpdateList(updates, null), this::serialize);

        Integer limit = 100;
        Integer timeout = 30;
        Long marker = null;
        UpdateList response = api.getUpdates()
                .marker(marker)
                .limit(limit)
                .timeout(timeout)
                .types(new HashSet<>(Arrays.asList(Update.MESSAGE_CREATED, Update.BOT_ADDED)))
                .execute();
        assertThat(response.getUpdates(), is(updates));

        for (Update update : updates) {
            update.visit(new FailByDefaultUpdateVisitor(mock(TestBot.class)) {
                @Override
                public void visit(MessageCreatedUpdate model) {
                    assertThat(model, is(messageCreatedUpdate));
                }

                @Override
                public void visit(MessageCallbackUpdate model) {
                    assertThat(model, is(messageCallbackUpdate));
                }

                @Override
                public void visit(MessageEditedUpdate model) {
                    assertThat(model, is(messageEditedUpdate));
                }

                @Override
                public void visit(MessageRemovedUpdate model) {
                    assertThat(model, is(messageRemovedUpdate));
                }

                @Override
                public void visit(BotAddedToChatUpdate model) {
                    assertThat(model, is(botAddedToChatUpdate));
                }

                @Override
                public void visit(BotRemovedFromChatUpdate model) {
                    assertThat(model, is(botRemovedFromChatUpdate));
                }

                @Override
                public void visit(UserAddedToChatUpdate model) {
                    assertThat(model, is(userAddedToChatUpdate));
                }

                @Override
                public void visit(UserRemovedFromChatUpdate model) {
                    assertThat(model, is(userRemovedFromChatUpdate));
                }

                @Override
                public void visit(BotStartedUpdate model) {
                    assertThat(model, is(botStartedUpdate));
                }

                @Override
                public void visit(ChatTitleChangedUpdate model) {
                    assertThat(model, is(chatTitleChangedUpdate));
                }

                @Override
                public void visit(MessageChatCreatedUpdate model) {
                    assertThat(model, is(messageChatCreatedUpdate));
                }
            });
        }
    }
}
