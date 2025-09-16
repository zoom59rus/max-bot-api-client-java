package ru.max.botapi;

import java.io.File;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import ru.max.botapi.client.MaxClient;
import ru.max.botapi.client.impl.JacksonSerializer;
import ru.max.botapi.client.impl.OkHttpTransportClient;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.exceptions.AttachmentNotReadyException;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Attachment;
import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.AudioAttachment;
import ru.max.botapi.model.AudioAttachmentRequest;
import ru.max.botapi.model.BotInfo;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ChatButton;
import ru.max.botapi.model.ChatList;
import ru.max.botapi.model.ChatStatus;
import ru.max.botapi.model.ChatType;
import ru.max.botapi.model.ContactAttachment;
import ru.max.botapi.model.ContactAttachmentRequest;
import ru.max.botapi.model.FileAttachment;
import ru.max.botapi.model.FileAttachmentRequest;
import ru.max.botapi.model.InlineKeyboardAttachment;
import ru.max.botapi.model.InlineKeyboardAttachmentRequest;
import ru.max.botapi.model.Intent;
import ru.max.botapi.model.LinkButton;
import ru.max.botapi.model.LinkedMessage;
import ru.max.botapi.model.LocationAttachment;
import ru.max.botapi.model.LocationAttachmentRequest;
import ru.max.botapi.model.Message;
import ru.max.botapi.model.MessageLinkType;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.NewMessageLink;
import ru.max.botapi.model.PhotoAttachment;
import ru.max.botapi.model.PhotoAttachmentRequest;
import ru.max.botapi.model.PhotoAttachmentRequestPayload;
import ru.max.botapi.model.PhotoTokens;
import ru.max.botapi.model.ReplyButton;
import ru.max.botapi.model.ReplyKeyboardAttachment;
import ru.max.botapi.model.ReplyKeyboardAttachmentRequest;
import ru.max.botapi.model.RequestContactButton;
import ru.max.botapi.model.RequestGeoLocationButton;
import ru.max.botapi.model.SendContactButton;
import ru.max.botapi.model.SendGeoLocationButton;
import ru.max.botapi.model.SendMessageButton;
import ru.max.botapi.model.SendMessageResult;
import ru.max.botapi.model.ShareAttachment;
import ru.max.botapi.model.ShareAttachmentRequest;
import ru.max.botapi.model.StickerAttachment;
import ru.max.botapi.model.StickerAttachmentRequest;
import ru.max.botapi.model.UploadType;
import ru.max.botapi.model.User;
import ru.max.botapi.model.UserIdsList;
import ru.max.botapi.model.VideoAttachment;
import ru.max.botapi.model.VideoAttachmentRequest;
import ru.max.botapi.queries.AddMembersQuery;
import ru.max.botapi.queries.GetChatQuery;
import ru.max.botapi.queries.GetChatsQuery;
import ru.max.botapi.queries.GetMessagesQuery;
import ru.max.botapi.queries.RemoveMemberQuery;
import ru.max.botapi.queries.SendMessageQuery;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;

@Tag("IntegrationTest")
public abstract class MaxIntegrationTest {
    protected static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static final AtomicLong ID_COUNTER = new AtomicLong();
    private static final AtomicBoolean ONCE = new AtomicBoolean();
    private static final boolean IS_CI = Boolean.parseBoolean(System.getenv("CI"));
    private static final String TOKEN_1 = getToken("MAX_BOTAPI_TOKEN");
    private static final String TOKEN_2 = getToken("MAX_BOTAPI_TOKEN_2");
    private static final String TOKEN_3 = getToken("MAX_BOTAPI_TOKEN_3");
    private static final ConcurrentHashMap<String, List<Chat>> CHATS_BY_CLIENT = new ConcurrentHashMap<>();

    private static final OkHttpTransportClient transportClient = new OkHttpTransportClient();
    protected static final JacksonSerializer serializer;
    public static final long LAST_ACTIVITY_IGNORED = -1L;

    static {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(User.class, new UserTestDeserializer());
        mapper.registerModule(module);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        serializer = new JacksonSerializer(mapper);
    }

    protected static MaxClient client = new MaxClient(TOKEN_1, transportClient, serializer);
    protected static MaxClient client2 = new MaxClient(TOKEN_2, transportClient, serializer);
    protected static MaxClient client3 = new MaxClient(TOKEN_3, transportClient, serializer);
    protected MaxBotAPI botAPI = new MaxBotAPI(client);
    protected MaxUploadAPI uploadAPI = new MaxUploadAPI(client);

    protected static TestBot bot1;
    protected static TestBot bot2;
    protected static TestBot3 bot3;

    static {
        try {
            bot1 = new TestBot(client, IS_CI);
            bot2 = new TestBot(client2, IS_CI);
            bot3 = new TestBot3(client3, client, IS_CI);
        } catch (APIException | ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void beforeClass() {
        info("Endpoint: {}", System.getenv("MAX_BOTAPI_ENDPOINT"));
        if (!ONCE.compareAndSet(false, true)) {
            return;
        }

        bot1.start();
        bot2.start();

        info("Bot 1: {}", bot1);
        info("Bot 2: {}", bot2);
        info("Bot 3: {}", bot3);
    }

    protected BotInfo getBot1() throws APIException, ClientException {
        return botAPI.getMyInfo().execute();
    }

    protected List<Chat> getChats() {
        return getChats(client);
    }

    protected List<Chat> getChats(MaxClient client) {
        return CHATS_BY_CLIENT.computeIfAbsent(client.getAccessToken(), (k) -> {
            Long marker;
            List<Chat> chats = new ArrayList<>();
            do {
                ChatList chatList;
                try {
                    chatList = new GetChatsQuery(client).count(100).execute();
                } catch (APIException | ClientException e) {
                    throw new RuntimeException(e);
                }

                chats.addAll(chatList.getChats());
                marker = chatList.getMarker();
            } while (marker != null);

            return chats;
        });
    }

    protected Chat getChat(long chatId) throws APIException, ClientException {
        return getChat(client, chatId);
    }

    protected Chat getChat(MaxClient client, long chatId) throws APIException, ClientException {
        return new GetChatQuery(client, chatId).execute();
    }

    protected List<Chat> getChatsCanSend() throws APIException, ClientException {
        ChatList chatList = botAPI.getChats().count(10).execute();
        return chatList.getChats().stream()
                .filter(c -> {
                    if (c.getType() == ChatType.CHANNEL) {
                        return c.getTitle().contains("bot is admin");
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    protected List<Message> send(NewMessageBody newMessage, List<Chat> toChats) throws Exception {
        List<Message> sent = new ArrayList<>();
        for (Chat c : toChats) {
            SendMessageResult sendMessageResult = doSend(newMessage, c.getChatId());
            sent.add(sendMessageResult.getMessage());
        }

        return sent;
    }

    protected SendMessageResult doSend(NewMessageBody newMessage, Long chatId) throws Exception {
        return doSend(client, newMessage, chatId);
    }

    protected SendMessageResult doSend(MaxClient client, NewMessageBody newMessage, Long chatId) throws Exception {
        do {
            try {
                SendMessageResult sendMessageResult = new SendMessageQuery(client, newMessage).chatId(chatId).execute();
                assertThat(sendMessageResult, is(notNullValue()));
                String messageId = sendMessageResult.getMessage().getBody().getMid();
                Message lastMessage = getMessage(client, messageId);
                compare(client, messageId, newMessage, lastMessage);
                return sendMessageResult;
            } catch (AttachmentNotReadyException e) {
                // it is ok, try again
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        } while (true);
    }

    protected void compare(MaxClient client, String messageId, NewMessageBody newMessage,
                           Message lastMessage) throws Exception {
        String text = newMessage.getText();
        assertThat(lastMessage.getBody().getMid(), is(messageId));
        if (text != null) {
            assertThat(lastMessage.getBody().getText(), is(text));
        }

        List<AttachmentRequest> attachments = newMessage.getAttachments();
        if (attachments != null) {
            for (int i = 0; i < attachments.size(); i++) {
                AttachmentRequest request = attachments.get(i);
                Attachment attachment = lastMessage.getBody().getAttachments().get(i);
                compare(request, attachment);
            }
        }

        NewMessageLink link = newMessage.getLink();
        if (link != null) {
            LinkedMessage linkedMessage = lastMessage.getLink();
            assertThat(linkedMessage, is(notNullValue()));
            compare(client, linkedMessage, link);
        }

        Chat chat = getChat(client, lastMessage.getRecipient().getChatId());
        if (chat.getType() == ChatType.CHANNEL) {
            assertThat(lastMessage.getRecipient().getChatType(), is(ChatType.CHANNEL));
        }

        if (chat.isPublic()) {
            assertThat(lastMessage.getUrl().length(), is(greaterThan(0)));
        } else {
            assertThat(lastMessage.getUrl(), is(nullValue()));
        }
    }

    protected Chat getByType(List<Chat> chats, ChatType type) throws Exception {
        return chats.stream()
                .filter(c -> c.getType() == type)
                .filter(c -> c.getStatus() == ChatStatus.ACTIVE)
                .findFirst()
                .orElseThrow(notFound(type.getValue()));
    }

    protected Chat getBy(List<Chat> chats, Predicate<Chat> filter) throws Exception {
        return chats.stream()
                .filter(filter)
                .filter(c -> c.getStatus() == ChatStatus.ACTIVE)
                .findFirst()
                .orElseThrow(notFound(filter.toString()));
    }

    protected Chat getByTitle(List<Chat> chats, String title) throws Exception {
        return chats.stream()
                .filter(c -> title.equals(c.getTitle()))
                .filter(c -> c.getStatus() == ChatStatus.ACTIVE)
                .findFirst()
                .orElseThrow(notFound(title));
    }

    protected static void compare(List<AttachmentRequest> attachmentRequests, List<Attachment> attachments) {
        Iterator<Attachment> attachmentIterator = attachments.iterator();
        for (AttachmentRequest request : attachmentRequests) {
            compare(request, attachmentIterator.next());
        }
    }

    protected static void compare(AttachmentRequest attachmentRequest, Attachment attachment) {
        attachmentRequest.visit(new AttachmentRequest.Visitor() {
            @Override
            public void visit(PhotoAttachmentRequest model) {
                compare(model, ((PhotoAttachment) attachment));
            }

            @Override
            public void visit(VideoAttachmentRequest model) {
                compare(model, ((VideoAttachment) attachment));
            }

            @Override
            public void visit(AudioAttachmentRequest model) {
                compare(model, ((AudioAttachment) attachment));
            }

            @Override
            public void visit(InlineKeyboardAttachmentRequest model) {
                compare(model, (InlineKeyboardAttachment) attachment);
            }

            @Override
            public void visit(ReplyKeyboardAttachmentRequest model) {
                compare(model, (ReplyKeyboardAttachment) attachment);
            }

            @Override
            public void visit(LocationAttachmentRequest model) {
                compare(model, (LocationAttachment) attachment);
            }

            @Override
            public void visit(ShareAttachmentRequest model) {
                compare(model, ((ShareAttachment) attachment));
            }

            @Override
            public void visit(FileAttachmentRequest model) {
                compare(model, ((FileAttachment) attachment));
            }

            @Override
            public void visit(StickerAttachmentRequest model) {
                compare(model, (StickerAttachment) attachment);
            }

            @Override
            public void visit(ContactAttachmentRequest model) {
                compare(model, ((ContactAttachment) attachment));
            }

            @Override
            public void visitDefault(AttachmentRequest model) {

            }
        });
    }

    protected List<Chat> getChatsForSend() throws Exception {
        List<Chat> chats = getChats();
        Chat dialog = getByType(chats, ChatType.DIALOG);
        Chat chat = getByTitle(chats, "test chat #4");
        Chat channel = getByTitle(chats, "test channel #1");
        return Arrays.asList(dialog, chat, channel);
    }

    protected void removeUser(MaxClient client, Long commonChatId, Long userId) {
        try {
            new RemoveMemberQuery(client, commonChatId, userId).execute();
        } catch (APIException | ClientException e) {
            fail(e.getMessage());
        }
    }

    protected void addUser(MaxClient client, Long chatId, Long userId) throws Exception {
        new AddMembersQuery(client, new UserIdsList(Collections.singletonList(userId)), chatId).execute();
    }

    protected Message getMessage(MaxClient client, String messageId) throws APIException, ClientException {
        return new GetMessagesQuery(client)
                .messageIds(Collections.singleton(messageId))
                .execute()
                .getMessages()
                .get(0);
    }

    protected static void await(CountDownLatch latch) throws InterruptedException {
        await(latch, IS_CI ? 30 : 3);
    }

    protected static void await(CountDownLatch latch, int seconds) throws InterruptedException {
        if (!latch.await(seconds, TimeUnit.SECONDS)) {
            fail();
        }
    }

    protected static void info(String text, Object... objects) {
        if (!IS_CI) {
            LOG.info(text, objects);
        }
    }

    private static void compare(StickerAttachmentRequest model, StickerAttachment attachment) {
        assertThat(model.getPayload().getCode(), is(attachment.getPayload().getCode()));
        assertThat(attachment.getPayload().getUrl(), is(notNullValue()));
        assertThat(attachment.getWidth(), is(greaterThan(0)));
        assertThat(attachment.getHeight(), is(greaterThan(0)));
    }

    private static void compare(MaxClient client, LinkedMessage linkedMessage,
                                NewMessageLink link) throws APIException, ClientException {
        assertThat(linkedMessage.getType(), is(link.getType()));

        if (link.getType() == MessageLinkType.REPLY) {
            Message message = new GetMessagesQuery(client).messageIds(
                    Collections.singleton(link.getMid())).execute().getMessages().get(0);
            assertThat(linkedMessage.getMessage().getSeq(), is(message.getBody().getSeq()));
            assertThat(linkedMessage.getMessage().getText(), is(message.getBody().getText()));
            assertThat(linkedMessage.getMessage().getAttachments(), is(message.getBody().getAttachments()));
            return;
        }

        assertThat(linkedMessage.getMessage().getMid(), is(link.getMid()));
    }

    protected static long now() {
        return System.currentTimeMillis();
    }

    @NotNull
    protected PhotoAttachmentRequest getPhotoAttachmentRequest() throws Exception {
        String uploadUrl = getUploadUrl(UploadType.IMAGE);
        File file = new File(getClass().getClassLoader().getResource("test.png").toURI());
        PhotoTokens photoTokens = uploadAPI.uploadImage(uploadUrl, file).execute();
        PhotoAttachmentRequestPayload payload = new PhotoAttachmentRequestPayload().photos(photoTokens.getPhotos());
        return new PhotoAttachmentRequest(payload);
    }

    @NotNull
    protected PhotoAttachmentRequest getPhotoAttachmentRequest(InputStream stream) throws Exception {
        String uploadUrl = getUploadUrl(UploadType.IMAGE);
        PhotoTokens photoTokens = uploadAPI.uploadImage(uploadUrl, randomText(16) + ".png", stream).execute();
        PhotoAttachmentRequestPayload payload = new PhotoAttachmentRequestPayload().photos(photoTokens.getPhotos());
        return new PhotoAttachmentRequest(payload);
    }

    protected String getUploadUrl(UploadType uploadType) throws Exception {
        String url = botAPI.getUploadUrl(uploadType).execute().getUrl();
        if (url.startsWith("http")) {
            return url;
        }

        return "http:" + url;
    }

    protected String randomText() {
        return getClass().getSimpleName() + "\n" + UUID.randomUUID().toString();
    }

    protected static String randomText(int length) {
        String alphabet = "qwertyuiopasdfghjklzxcvbnm";
        return Stream.generate(() -> alphabet.charAt(ThreadLocalRandom.current().nextInt(alphabet.length())))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static String getToken(String envVar) {
        String tokenEnv = System.getenv(envVar);
        if (tokenEnv != null) {
            return tokenEnv;
        }

        throw new NullPointerException("No token provided. Please set " + envVar + " environment variable.");
    }

    private static Supplier<Exception> notFound(String entity) {
        return () -> new RuntimeException(entity + " not found");
    }

    private static void compare(InlineKeyboardAttachmentRequest request, InlineKeyboardAttachment attachment) {
        List<List<Button>> expected = request.getPayload().getButtons();
        List<List<Button>> actual = attachment.getPayload().getButtons();
        for (int i = 0; i < actual.size(); i++) {
            for (int j = 0; j < actual.get(i).size(); j++) {
                Button actualButton = actual.get(i).get(j);
                Button expectedButton = expected.get(i).get(j);
                compare(actualButton, expectedButton);
            }
        }
    }

    private static void compare(ReplyKeyboardAttachmentRequest request, ReplyKeyboardAttachment attachment) {
        List<List<ReplyButton>> expected = request.getButtons();
        List<List<ReplyButton>> actual = attachment.getButtons();
        for (int i = 0; i < actual.size(); i++) {
            for (int j = 0; j < actual.get(i).size(); j++) {
                ReplyButton actualButton = actual.get(i).get(j);
                ReplyButton expectedButton = expected.get(i).get(j);
                compare(actualButton, expectedButton);
            }
        }
    }

    private static void compare(Button actualButton, Button expectedButton) {
        actualButton.visit(new Button.Visitor() {
            @Override
            public void visit(CallbackButton model) {
                CallbackButton expectedCallbackButton = (CallbackButton) expectedButton;
                assertThat(model.getPayload(), is(expectedCallbackButton.getPayload()));
                assertThat(model.getText(), is(expectedCallbackButton.getText()));
                if (expectedCallbackButton.getIntent() != null) {
                    assertThat(model.getIntent(), is(expectedCallbackButton.getIntent()));
                } else {
                    assertThat(model.getIntent(), is(Intent.DEFAULT));
                }
            }

            @Override
            public void visit(LinkButton model) {
                assertThat(model, is(expectedButton));
            }

            @Override
            public void visit(RequestGeoLocationButton model) {
                assertThat(model, is(expectedButton));
            }

            @Override
            public void visit(RequestContactButton model) {
                assertThat(model, is(expectedButton));
            }

            @Override
            public void visit(ChatButton model) {
                ChatButton cb = (ChatButton) expectedButton;
                assertThat(model.getChatDescription(), is(cb.getChatDescription()));
                assertThat(model.getChatTitle(), is(cb.getChatTitle()));
                assertThat(model.getStartPayload(), is(cb.getStartPayload()));
            }

            @Override
            public void visitDefault(Button model) {
                assertThat(model, is(expectedButton));
            }
        });
    }

    private static void compare(ReplyButton actualButton, ReplyButton expectedButton) {
        actualButton.visit(new ReplyButton.Visitor() {
            @Override
            public void visit(SendMessageButton model) {
                assertThat(model.getText(), is(expectedButton.getText()));
                assertThat(model.getPayload(), is(expectedButton.getPayload()));
                assertThat(model.getIntent(), is(((SendMessageButton) expectedButton).getIntent()));
            }

            @Override
            public void visit(SendGeoLocationButton model) {
                assertThat(model.getText(), is(expectedButton.getText()));
                assertThat(model.isQuick(), is(((SendGeoLocationButton) expectedButton).isQuick()));
            }

            @Override
            public void visit(SendContactButton model) {
                assertThat(model.getText(), is(expectedButton.getText()));
                assertThat(model.getPayload(), is(expectedButton.getPayload()));
            }

            @Override
            public void visitDefault(ReplyButton model) {
                assertThat(model, is(expectedButton));
            }
        });
    }

    private static void compare(PhotoAttachmentRequest request, PhotoAttachment attachment) {
        assertThat(attachment.getPayload().getPhotoId(), is(notNullValue()));
    }

    private static void compare(VideoAttachmentRequest request, VideoAttachment attachment) {
        assertThat(attachment.getPayload().getUrl(), is(notNullValue()));
        compareTokens(attachment.getPayload().getToken(), request.getPayload().getToken());
    }

    private static void compare(FileAttachmentRequest request, FileAttachment attachment) {
        assertThat(attachment.getPayload().getUrl(), is(notNullValue()));
        compareTokens(attachment.getPayload().getToken(), request.getPayload().getToken());
    }

    private static void compare(AudioAttachmentRequest request, AudioAttachment attachment) {
        assertThat(attachment.getPayload().getUrl(), is(notNullValue()));
        compareTokens(attachment.getPayload().getToken(), request.getPayload().getToken());
    }

    private static void compare(ContactAttachmentRequest request, ContactAttachment attachment) {
        if (request.getPayload().getVcfInfo() != null) {
            assertThat(attachment.getPayload().getVcfInfo(), is(request.getPayload().getVcfInfo()));
        }
    }

    private static void compare(ShareAttachmentRequest request, ShareAttachment attachment) {
        assertThat(attachment.getPayload().getUrl(), not(isEmptyString()));
        assertThat(attachment.getPayload().getToken(), not(isEmptyString()));
        assertThat(attachment.getTitle(), not(isEmptyString()));
        if (request.getPayload().getUrl() != null) {
            assertThat(request.getPayload().getUrl(), is(attachment.getPayload().getUrl()));
        }
    }

    private static void compare(LocationAttachmentRequest request, LocationAttachment attachment) {
        assertThat(request.getLatitude(), is(attachment.getLatitude()));
        assertThat(request.getLongitude(), is(attachment.getLongitude()));
    }

    private static void compareTokens(String token1, String token2) {
        assertThat(token1.substring(0, 21), is(token2.substring(0, 21)));
    }

    protected static void assertUser(User actual, User expected) {
        assertThat(actual.getName(), is(expected.getName()));
        assertThat(actual.getUserId(), is(expected.getUserId()));
        assertThat(actual.getUsername(), is(expected.getUsername()));
        assertThat(actual.getUsername(), is(expected.getUsername()));
    }
}
