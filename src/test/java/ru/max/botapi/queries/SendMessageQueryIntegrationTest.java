package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.fail;


public class SendMessageQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldSendSimpleTextMessage() throws Exception {
        NewMessageBody newMessage = new NewMessageBody("text", null, null);
        send(newMessage);
    }

    @Test
    public void shouldThrowException() throws Exception {
        NewMessageBody newMessage = new NewMessageBody(null, null, null);
        List<Chat> chats = getChats();
        Chat dialog = getByType(chats, ChatType.DIALOG);
        Chat chat = getByType(chats, ChatType.CHAT);
        Chat channel = getByType(chats, ChatType.CHANNEL);

        int exceptions = 0;
        List<Chat> list = Arrays.asList(dialog, chat, channel);
        for (Chat c : list) {
            try {
                doSend(newMessage, c.getChatId());
            } catch (Exception e) {
                exceptions++;
            }
        }

        if (exceptions != list.size()) {
            fail();
        }
    }

    @Test
    public void shouldThrowExceptionWhenSendButtonsOnly() throws Exception {
        AttachmentRequest keyboard = new InlineKeyboardAttachmentRequest(new InlineKeyboardAttachmentRequestPayload(
                Collections.singletonList(Collections.singletonList(new CallbackButton("test", "ok")))
        ));

        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(keyboard), null);
        List<Chat> chats = getChats();
        Chat dialog = getByType(chats, ChatType.DIALOG);
        Chat chat = getByType(chats, ChatType.CHAT);
        Chat channel = getByType(chats, ChatType.CHANNEL);

        int exceptions = 0;
        List<Chat> list = Arrays.asList(dialog, chat, channel);
        for (Chat c : list) {
            try {
                doSend(newMessage, c.getChatId());
            } catch (Exception e) {
                exceptions++;
            }
        }

        if (exceptions != list.size()) {
            fail();
        }
    }

    @Test
    public void shouldThrowExceptionWhenTextIsTooLong() throws Exception {
        NewMessageBody newMessage = new NewMessageBody(randomText(4001), null, null);
        List<Chat> chats = getChats();
        Chat dialog = getByType(chats, ChatType.DIALOG);
        Chat chat = getByTitle(chats, "test chat #1");
        Chat channel = getByTitle(chats, "test channel #1");

        int exceptions = 0;
        List<Chat> list = Arrays.asList(dialog, chat, channel);
        for (Chat c : list) {
            try {
                doSend(newMessage, c.getChatId());
            } catch (APIException e) {
                exceptions++;
                assertThat(e.getMessage(), is("text: size must be between 0 and 4000"));
            }
        }

        if (exceptions != list.size()) {
            fail();
        }
    }

    @Test
    public void shouldSendKeyboard() throws Exception {
        List<List<Button>> buttons = Arrays.asList(
                Arrays.asList(
                        new CallbackButton("payload", "text"),
                        new CallbackButton("payload2", "text").intent(Intent.NEGATIVE),
                        new CallbackButton("payload2", "text").intent(Intent.POSITIVE)
                ),
                Arrays.asList(
                        new LinkButton("https://mail.ru", "link"),
                        new LinkButton("https://max.ru/beer", "link"),
                        new LinkButton(randomText(2048), "link")
                ),
                Collections.singletonList(
                        new RequestContactButton("contact")
                ),
                Collections.singletonList(
                        new RequestGeoLocationButton("geo location").quick(true)
                ),
                Collections.singletonList(
                        new ChatButton("new chat title " + ID_COUNTER.incrementAndGet(), "chat button")
                                .chatDescription(randomText())
                                .startPayload(randomText())
                                .uuid(ThreadLocalRandom.current().nextInt())
                )
        );

        InlineKeyboardAttachmentRequestPayload keyboard = new InlineKeyboardAttachmentRequestPayload(buttons);
        AttachmentRequest attach = new InlineKeyboardAttachmentRequest(keyboard);
        NewMessageBody newMessage = new NewMessageBody("keyboard", Collections.singletonList(attach), null);
        send(newMessage);
    }

    @Test
    public void shouldSendPhoto() throws Exception {
        AttachmentRequest attach = getPhotoAttachmentRequest();
        NewMessageBody newMessage = new NewMessageBody("image", Collections.singletonList(attach), null);
        send(newMessage);
    }

    @Test
    public void shouldSendPhotoAsSingleAttach() throws Exception {
        AttachmentRequest attach = getPhotoAttachmentRequest();
        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(attach), null);
        send(newMessage);
    }

    @Test
    public void shouldSendPhotoByToken() throws Exception {
        List<Chat> chats = getChats();
        Chat dialog = getByType(chats, ChatType.DIALOG);
        Chat chat = getByTitle(chats, "test chat #4");
        Chat channel = getByTitle(chats, "test channel #1");

        AttachmentRequest attach = getPhotoAttachmentRequest();
        NewMessageBody newMessage = new NewMessageBody("image", Collections.singletonList(attach), null);

        SendMessageResult result = doSend(newMessage, dialog.getChatId());
        PhotoAttachment attachment = (PhotoAttachment) result.getMessage().getBody().getAttachments().get(0);
        String token = attachment.getPayload().getToken();

        newMessage = new NewMessageBody("image", Collections.singletonList(new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().token(token))), null);
        doSend(newMessage, chat.getChatId());
        doSend(newMessage, channel.getChatId());

        assertThat(result.getMessage().getBody().getAttachments(), is(not(empty())));
        assertThat(result.getMessage().getBody().getAttachments(), is(not(empty())));
    }

    @Test
    public void shouldSendGifByURL() throws Exception {
        String url = "https://media1.giphy.com/media/2RCQECf4JBfoc/giphy.gif?cid=e1bb72ff5c936527514b67642ec770cf";
        PhotoAttachmentRequestPayload payload = new PhotoAttachmentRequestPayload().url(url);
        AttachmentRequest attach = new PhotoAttachmentRequest(payload);
        NewMessageBody newMessage = new NewMessageBody("image", Collections.singletonList(attach), null);
        send(newMessage);
    }

    @Test
    public void shouldSendAudio() throws Exception {
        String uploadUrl = getUploadUrl(UploadType.AUDIO);
        File file = new File(getClass().getClassLoader().getResource("test.m4a").toURI());
        UploadedInfo uploadedInfo = uploadAPI.uploadAV(uploadUrl, file).execute();
        AttachmentRequest request = new AudioAttachmentRequest(uploadedInfo);
        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(request), null);
        send(newMessage);
    }

    @Test
    public void shouldSendAudioReusingId() throws Exception {
        String uploadUrl = getUploadUrl(UploadType.AUDIO);
        File file = new File(getClass().getClassLoader().getResource("test.m4a").toURI());
        UploadedInfo uploadedInfo = uploadAPI.uploadAV(uploadUrl, file).execute();
        AttachmentRequest request = new AudioAttachmentRequest(uploadedInfo);
        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(request), null);
        List<Message> createdMessages = send(newMessage);
        for (Message createdMessage : createdMessages) {
            AudioAttachment attachment = (AudioAttachment) createdMessage.getBody().getAttachments().get(0);
            AudioAttachmentRequest copyAttach = new AudioAttachmentRequest(
                    new UploadedInfo().token(attachment.getPayload().getToken()));

            doSend(new NewMessageBody("resend with attach", Collections.singletonList(copyAttach), null),
                    createdMessage.getRecipient().getChatId());
        }
    }

    @Test
    public void shouldSendContact() throws Exception {
        UserWithPhoto me = getBot1();
        ContactAttachmentRequestPayload payload = new ContactAttachmentRequestPayload(me.getName()).contactId(me.getUserId()).vcfPhone("+79991234567");
        AttachmentRequest request = new ContactAttachmentRequest(payload);
        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(request), null);
        send(newMessage);
    }

    @Test
    public void shouldSendForward() throws Exception {
        NewMessageBody messageToForward = new NewMessageBody("message to forward", null, null);
        List<Message> sent = send(messageToForward);
        for (Message message : sent) {
            String mid = message.getBody().getMid();
            send(new NewMessageBody(null, null, new NewMessageLink(MessageLinkType.FORWARD, mid)));
        }
    }

    @Test
    public void shouldSendForwardWithAttach() throws Exception {
        NewMessageBody messageToForward = new NewMessageBody("message to forward", null, null);
        List<Message> sent = send(messageToForward);
        for (Message message : sent) {
            String mid = message.getBody().getMid();
            send(new NewMessageBody(null, Collections.singletonList(getPhotoAttachmentRequest()),
                    new NewMessageLink(MessageLinkType.FORWARD, mid)));
        }
    }

    @Test
    public void shouldSendReply() throws Exception {
        for (Chat chat : getChatsForSend()) {
            NewMessageBody message1body = new NewMessageBody(randomText(16), null, null);
            Message message1 = send(message1body, Collections.singletonList(chat)).get(0);

            NewMessageBody reply1Body = new NewMessageBody(randomText(16), null, new NewMessageLink(MessageLinkType.REPLY, message1.getBody().getMid()));
            Message reply1 = send(reply1Body, Collections.singletonList(chat)).get(0);
            assertThat(reply1.getLink().getMessage(), is(message1.getBody()));

            NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, reply1.getBody().getMid());
            NewMessageBody reply2body = new NewMessageBody(randomText(16), null, link);
            Message reply2 = send(reply2body, Collections.singletonList(chat)).get(0);

            assertThat(reply2.getLink().getMessage(), is(reply1.getBody()));
        }
    }

    @Test
    public void shouldSendReplyOnForward() throws Exception {
        for (Chat chat : getChatsForSend()) {
            NewMessageBody message1body = new NewMessageBody(randomText(16), null, null);
            Message message = send(message1body, Collections.singletonList(chat)).get(0);

            NewMessageBody forwardBody = new NewMessageBody(null, null, new NewMessageLink(MessageLinkType.FORWARD, message.getBody().getMid()));
            Message forward = send(forwardBody, Collections.singletonList(chat)).get(0);
            assertThat(forward.getLink().getMessage(), is(message.getBody()));

            NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, forward.getBody().getMid());
            NewMessageBody replyBody = new NewMessageBody(randomText(16), null, link);
            Message reply = send(replyBody, Collections.singletonList(chat)).get(0);

            assertThat(reply.getLink().getMessage(), is(forward.getBody()));
        }
    }

    @Test
    public void shouldSendReplyWithAttach() throws Exception {
        for (Chat chat : getChatsForSend()) {
            NewMessageBody messageToForward = new NewMessageBody("message to reply", null, null);
            List<Message> sent = send(messageToForward, Collections.singletonList(chat));
            for (Message message : sent) {
                String mid = message.getBody().getMid();
                NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, mid);
                send(new NewMessageBody("Reply with attach", Collections.singletonList(getPhotoAttachmentRequest()),
                        link), Collections.singletonList(chat));
            }
        }
    }

    @Test
    public void shouldContainsSenderAndRecipientInDialog() throws Exception {
        Chat dialog = getByType(getChats(), ChatType.DIALOG);
        NewMessageBody newMessage = new NewMessageBody(randomText(), null, null);
        SendMessageResult result = doSend(newMessage, dialog.getChatId());
        assertThat(result.getMessage().getSender().getUserId(), is(bot1.getUserId()));
        assertThat(result.getMessage().getRecipient().getChatId(), is(dialog.getChatId()));
        assertThat(result.getMessage().getRecipient().getUserId(), is(not(bot1.getUserId())));
    }

    @Test
    public void shouldNOTContainsRecipientUserIdInChat() throws Exception {
        Chat chat = getByTitle(getChats(), "test chat #4");
        NewMessageBody newMessage = new NewMessageBody(randomText(), null, null);
        SendMessageResult result = doSend(newMessage, chat.getChatId());
        assertThat(result.getMessage().getSender().getUserId(), is(bot1.getUserId()));
        assertThat(result.getMessage().getRecipient().getChatId(), is(chat.getChatId()));
        assertThat(result.getMessage().getRecipient().getUserId(), is(nullValue()));
    }

    @Test
    public void shoulReturnSenderInChannelIfSigned() throws Exception {
        Chat channel = getByTitle(getChats(), "test channel #3");
        NewMessageBody newMessage = new NewMessageBody(randomText(), null, null);
        SendMessageResult result = doSend(newMessage, channel.getChatId());
        assertThat(result.getMessage().getSender().getUserId(), is(bot1.getUserId()));
        assertThat(result.getMessage().getSender().getName(), is(bot1.getFirstName()));
    }

    @Test
    public void should_NOT_ReturnSenderInChannelIf_NOT_Signed() throws Exception {
        Chat channel = getByTitle(getChats(), "test channel #1");
        NewMessageBody newMessage = new NewMessageBody(randomText(), null, null);
        SendMessageResult result = doSend(newMessage, channel.getChatId());
        assertThat(result.getMessage().getSender(), is(nullValue()));
    }

    @Test
    public void shouldSendSticker() throws Exception {
        Chat chat = getByTitle(getChats(client), "SendMessageQueryIntegrationTest#shouldSendSticker");
        MessageList messageList = new GetMessagesQuery(client).chatId(chat.getChatId()).count(1).execute();
        Message message = messageList.getMessages().get(0);
        StickerAttachment stickerAttach = (StickerAttachment) message.getBody().getAttachments().get(0);
        AttachmentRequest stickerAR = new StickerAttachmentRequest(new StickerAttachmentRequestPayload(stickerAttach.getPayload().getCode()));
        send(new NewMessageBody(null, Collections.singletonList(stickerAR), null));
    }

    @Test
    public void shouldSendWithSpecialCharacter() throws Exception {
        String text = randomText(3999) + "<";
        NewMessageBody newMessage = new NewMessageBody(text, null, null);
        Chat dialog = getByType(getChats(), ChatType.DIALOG);
        SendMessageResult result = doSend(newMessage, dialog.getChatId());
        assertThat(result.getMessage().getBody().getText(), is(text));
    }

    @Test
    public void shouldSendMessageWithURL() throws Exception {
        NewMessageBody nmb = new NewMessageBody("https://max.ru", null, null);
        List<Message> sent = send(nmb);
        for (Message message : sent) {
            assertThat(message.getBody().getAttachments().get(0), is(instanceOf(ShareAttachment.class)));
        }
    }

    @Test
    public void shouldNotParseURLInText() throws Exception {
        NewMessageBody nmb = new NewMessageBody("https://max.ru", null, null);
        List<Message> sent = new ArrayList<>();
        for (Chat chat : getChatsForSend()) {
            SendMessageResult result = new SendMessageQuery(client, nmb).disableLinkPreview(true).chatId(
                    chat.getChatId()).execute();

            sent.add(result.getMessage());
        }

        for (Message message : sent) {
            assertThat(message.getBody().getAttachments(), is(nullValue()));
        }
    }

    @Test
    public void shouldThrowExceptionWhenUrlIsTooLong() throws Exception {
        Button linkButton = new LinkButton(randomText(2049), randomText());
        List<List<Button>> buttons = Collections.singletonList(Collections.singletonList(linkButton));
        InlineKeyboardAttachmentRequestPayload payload = new InlineKeyboardAttachmentRequestPayload(buttons);
        AttachmentRequest keyboard = new InlineKeyboardAttachmentRequest(payload);
        NewMessageBody newMessage = new NewMessageBody(randomText(), Collections.singletonList(keyboard), null);
        List<Chat> chats = getChatsForSend();
        int exceptions = 0;
        for (Chat c : chats) {
            try {
                doSend(newMessage, c.getChatId());
            } catch (APIException e) {
                exceptions++;
                assertThat(e.getMessage(), is("attachments[0].payload.buttons[0].<list element>[0].url: size must be between 0 and 2048"));
            }
        }

        if (exceptions != chats.size()) {
            fail();
        }

    }

    private List<Message> send(NewMessageBody newMessage) throws Exception {
        return send(newMessage, getChatsForSend());
    }
}
