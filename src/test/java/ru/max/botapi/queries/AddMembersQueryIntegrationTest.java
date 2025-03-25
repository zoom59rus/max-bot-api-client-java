package ru.max.botapi.queries;

import java.util.Collections;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.SimpleQueryResult;
import ru.max.botapi.model.UserIdsList;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;


public class AddMembersQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void testAddEmptyMembersList() throws Exception {
        Chat chat = getByTitle(getChats(client), "AddMembersQueryIntegrationTest#testAddMemberAlreadyAdded");
        SimpleQueryResult result = new AddMembersQuery(client, new UserIdsList(Collections.emptyList()),
                chat.getChatId()).execute();

        assertThat(result.isSuccess(), is(false));
        assertThat(result.getMessage(), not(isEmptyString()));
    }

    @Test
    public void testAddMembersAlreadyAdded() throws Exception {
        Chat chat = getByTitle(getChats(client), "AddMembersQueryIntegrationTest#testAddMemberAlreadyAdded");
        SimpleQueryResult result = new AddMembersQuery(client, new UserIdsList(
                Collections.singletonList(bot2.getUserId())), chat.getChatId()).execute();

        assertThat(result.isSuccess(), is(false));
        assertThat(result.getMessage(), not(isEmptyString()));
    }
}