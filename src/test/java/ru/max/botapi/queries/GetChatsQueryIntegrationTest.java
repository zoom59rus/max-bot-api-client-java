package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ChatList;
import ru.max.botapi.model.ChatStatus;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.isEmptyString;


public class GetChatsQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldGetAllChats() throws Exception {
        List<Chat> chats = new ArrayList<>();
        Long marker = null;
        do {
            ChatList chatList = botAPI.getChats().marker(marker).execute();
            chats.addAll(chatList.getChats());
            marker = chatList.getMarker();
        } while (marker != null);

        assertThat(chats.size(), is(greaterThan(0)));

        for (Chat chat : chats) {
            if (chat.getStatus() != ChatStatus.ACTIVE) {
                continue;
            }

            if (chat.getTitle() != null && chat.getTitle().toLowerCase().contains("public")) {
                assertThat(chat.getTitle(), chat.isPublic(), is(true));
                assertThat(chat.getTitle(), chat.getLink(), is(not(isEmptyString())));
            }

            assertThat(chat.getChatId(), is(notNullValue()));
            assertThat(chat.getType(), is(notNullValue()));
            assertThat(chat.getStatus(), is(notNullValue()));
            assertThat(chat.getTitle(), chat.getParticipantsCount(), is(greaterThan(0)));

//            if (chat.getType() != ChatType.DIALOG && chat.getTitle().contains("bot is admin")) {
//                assertThat("Chat: " + chat, chat.getOwnerId(), is(notNullValue()));
//            }
        }
    }
}