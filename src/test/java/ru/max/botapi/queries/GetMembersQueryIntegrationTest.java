package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ChatMember;
import ru.max.botapi.model.ChatMembersList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.fail;


public class GetMembersQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldGetMembersOfChat() throws Exception {
        List<Chat> chats = getChats();
        Chat chat2 = getByTitle(chats, "test chat #2");
        ChatMembersList membersList = new GetMembersQuery(client, chat2.getChatId()).execute();
        Map<Long, ChatMember> members = membersList.getMembers().stream()
                .collect(Collectors.toMap(ChatMember::getUserId, Function.identity()));

        assertThat(members.size(), is(3));
        assertThat(members.get(bot1.getUserId()).isAdmin(), is(true));
        assertThat(members.get(bot1.getUserId()).isBot(), is(true));
        assertThat(members.get(bot2.getUserId()).isAdmin(), is(false));

        members.remove(bot1.getUserId());
        members.remove(bot2.getUserId());

        for (ChatMember member : members.values()) {
            assertThat(member.isBot(), is(false));
        }
    }

    @Test
    public void shouldGetMembersOfChatWhereBotIsNotAdmin() throws Exception {
        List<Chat> chats = getChats();
        Chat chat2 = getByTitle(chats, "test chat #3");
        ChatMembersList membersList = new GetMembersQuery(client, chat2.getChatId()).execute();
        Map<Long, ChatMember> members = membersList.getMembers().stream()
                .collect(Collectors.toMap(ChatMember::getUserId, Function.identity()));

        assertThat(members.size(), is(3));
        assertThat(members.get(bot1.getUserId()).isAdmin(), is(false));
        assertThat(members.get(bot2.getUserId()).isAdmin(), is(false));
    }

    @Test
    public void shouldNotGetMembersOfChannelWhereBotIsNotAdmin() throws Exception {
        List<Chat> chats = getChats();
        Chat chat2 = getByTitle(chats, "test channel #2");
        ChatMembersList membersList = new GetMembersQuery(client, chat2.getChatId()).execute();
        membersList.getMembers().stream().collect(Collectors.toMap(ChatMember::getUserId, Function.identity()));
    }

    @Test
    public void shouldGetMembersByIds() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = getByTitle(chats, "test chat #1");
        HashSet<Long> ids = new HashSet<>(Arrays.asList(bot1.getUserId(), bot2.getUserId()));
        ChatMembersList members = new GetMembersQuery(client, chat.getChatId()).userIds(ids).execute();
        assertThat(members.getMarker(), is(nullValue()));
        assertThat(members.getMembers().size(), is(ids.size()));

        Map<Long, ChatMember> byId = members.getMembers().stream().collect(
                Collectors.toMap(ChatMember::getUserId, Function.identity()));

        ChatMember myMembership = byId.get(bot1.getUserId());
        ChatMember bot2Membership = byId.get(bot2.getUserId());
        assertThat(myMembership.getPermissions(), is(not(empty())));
        assertThat(myMembership.isAdmin(), is(true));
        assertThat(myMembership.getUsername(), is(bot1.getUsername()));
        assertThat(bot2Membership.isAdmin(), is(false));
        assertThat(bot2Membership.getPermissions(), is(nullValue()));
    }

    @Test
    public void shouldGetAllMembers() throws Exception {
        Long chatId = getByTitle(getChats(), "GetMembershipQueryIntegrationTest#shouldReturnAllMembers").getChatId();
        Long marker = null;
        List<ChatMember> result = new ArrayList<>();
        do {
            ChatMembersList membersList = new GetMembersQuery(client, chatId).count(1).marker(marker).execute();

            result.addAll(membersList.getMembers().stream().filter(cm -> cm.getName().toLowerCase().contains("bot"))
                    .collect(Collectors.toList()));

            marker = membersList.getMarker();
        } while (marker != null);

        assertThat(result.stream().map(ChatMember::getUserId).collect(Collectors.toList()),
                is(Stream.of(bot1.getUserId(), bot2.getUserId(), bot3.getUserId())
                        .sorted(Comparator.reverseOrder()).collect(Collectors.toList())));
    }


    @Test
    public void shouldThrowExceptionNotChannelAdmin() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = getByTitle(chats, "test channel #2");
        HashSet<Long> ids = new HashSet<>(Arrays.asList(bot1.getUserId(), bot2.getUserId()));
        new GetMembersQuery(client, chat.getChatId()).userIds(ids).execute();
    }

    @Test
    public void shouldThrowExceptionIfNotChatMember() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = getByTitle(chats, "test chat #5");
        HashSet<Long> ids = new HashSet<>(Arrays.asList(bot1.getUserId(), bot2.getUserId()));
        new GetMembersQuery(client2, chat.getChatId()).userIds(ids).execute();
    }

    @Test
    public void shouldThrowExceptionIfNotChannelMember() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = getByTitle(chats, "test channel #3");
        HashSet<Long> ids = new HashSet<>(Arrays.asList(bot1.getUserId(), bot2.getUserId()));
        new GetMembersQuery(client2, chat.getChatId()).userIds(ids).execute();
    }

    @Test
    public void shouldReturnDeletedMember() throws Exception {
        List<Chat> chats = getChats();
        Chat chat = getByTitle(chats, "GetMembersQueryIntegrationTest#shouldReturnBlockedMember");
        ChatMembersList membersList = botAPI.getMembers(chat.getChatId()).execute();
        ChatMember deletedMember = null;
        for (ChatMember member : membersList.getMembers()) {
            if (member.getName().equals("DELETED USER")) {
                deletedMember = member;
                break;
            }
        }

        if (deletedMember == null) {
            fail("Deleted user not found");
        }

        assertThat(deletedMember.getLastActivityTime(), is(0L));
        assertThat(deletedMember.getUsername(), is(nullValue()));

        ChatMembersList membersById = botAPI.getMembers(chat.getChatId()).userIds(
                Collections.singleton(deletedMember.getUserId())).execute();

        assertThat(membersById.getMembers().get(0), is(deletedMember));
    }
}