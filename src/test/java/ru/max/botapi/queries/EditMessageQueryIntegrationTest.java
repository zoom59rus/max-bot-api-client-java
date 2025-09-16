package ru.max.botapi.queries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@Tag("IntegrationTest")
public class EditMessageQueryIntegrationTest extends MaxIntegrationTest {
    private AttachmentRequest photoAR;
    private AttachmentRequest photoAR2;
    private List<Chat> chats;

    @BeforeEach
    public void setUp() throws Exception {
        String uploadUrl = getUploadUrl(UploadType.IMAGE);
        File file = new File(getClass().getClassLoader().getResource("test.png").toURI());
        PhotoTokens photoTokens = uploadAPI.uploadImage(uploadUrl, file).execute();
        photoAR = new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().photos(photoTokens.getPhotos()));

        String uploadUrl2 = getUploadUrl(UploadType.IMAGE);
        File file2 = new File(getClass().getClassLoader().getResource("test2.png").toURI());
        PhotoTokens photoTokens2 = uploadAPI.uploadImage(uploadUrl2, file2).execute();
        photoAR2 = new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().photos(photoTokens2.getPhotos()));

        List<Chat> allChats = getChats();
        chats = Arrays.asList(
                getByType(allChats, ChatType.DIALOG),
                getByTitle(allChats, "test chat #1"),
                getByTitle(allChats, "test channel #1")
        );
    }

    @Test
    public void shouldEditText() throws Exception {
        for (Chat chat : chats) {
            NewMessageBody newMessageBody = new NewMessageBody(randomText(), Collections.singletonList(photoAR), null);
            SendMessageResult result = botAPI.sendMessage(newMessageBody).chatId(chat.getChatId()).execute();
            NewMessageBody editedMessageBody = new NewMessageBody("edited message text", null, null);
            String messageId = result.getMessage().getBody().getMid();
            botAPI.editMessage(editedMessageBody, messageId).execute();
            MessageBody lastMessage = getMessage(client, messageId).getBody();

            assertThat(lastMessage.getText(), is(editedMessageBody.getText()));
            compare(photoAR, lastMessage.getAttachments().get(0));
        }
    }

    @Test
    public void shouldEditAttachments() throws Exception {
        for (Chat chat : chats) {
            List<AttachmentRequest> attachmentRequests = Collections.singletonList(photoAR);
            String text = randomText();
            NewMessageBody newMessageBody = new NewMessageBody(text, attachmentRequests, null);
            SendMessageResult result = botAPI.sendMessage(newMessageBody).chatId(chat.getChatId()).execute();

            List<AttachmentRequest> editAttachmentRequests = Collections.singletonList(photoAR2);
            NewMessageBody editedMessageBody = new NewMessageBody(null, editAttachmentRequests, null);
            String messageId = result.getMessage().getBody().getMid();
            botAPI.editMessage(editedMessageBody, messageId).execute();
            MessageBody lastMessage = getMessage(client, messageId).getBody();

            assertThat(lastMessage.getText(), is(text));
            compare(editAttachmentRequests, lastMessage.getAttachments());
        }
    }

    @Test
    public void shouldEditBothTextAndAttachments() throws Exception {
        for (Chat chat : chats) {
            List<AttachmentRequest> attachmentRequests = Collections.singletonList(photoAR);
            String text = randomText();
            NewMessageBody newMessageBody = new NewMessageBody(text, attachmentRequests, null);
            SendMessageResult result = botAPI.sendMessage(newMessageBody).chatId(chat.getChatId()).execute();

            List<AttachmentRequest> editAttachmentRequests = Collections.singletonList(photoAR2);
            String newText = "edited " + text;
            NewMessageBody editedMessageBody = new NewMessageBody(newText, editAttachmentRequests, null);
            String messageId = result.getMessage().getBody().getMid();
            botAPI.editMessage(editedMessageBody, messageId).execute();
            MessageBody lastMessage = getMessage(client, messageId).getBody();

            assertThat(lastMessage.getText(), is(newText));
            compare(editAttachmentRequests, lastMessage.getAttachments());
        }
    }

    @Test
    public void shouldRemoveAttachmentButLeaveText() throws Exception {
        for (Chat chat : chats) {
            List<AttachmentRequest> attachmentRequests = Collections.singletonList(photoAR);
            String text = randomText();
            NewMessageBody newMessageBody = new NewMessageBody(text, attachmentRequests, null);
            SendMessageResult result = botAPI.sendMessage(newMessageBody).chatId(chat.getChatId()).execute();

            NewMessageBody editedMessageBody = new NewMessageBody(null, Collections.emptyList(), null);
            String messageId = result.getMessage().getBody().getMid();
            botAPI.editMessage(editedMessageBody, messageId).execute();
            MessageBody lastMessage = getMessage(client, messageId).getBody();

            assertThat("chatType: " + chat.getType(), lastMessage.getText(), is(text));
            assertThat("chatType: " + chat.getType(), lastMessage.getAttachments(), is(nullValue()));
        }
    }

    @Test
    public void shouldRemoveTextButLeaveAttachment() throws Exception {
        for (Chat chat : chats) {
            List<AttachmentRequest> attachmentRequests = Collections.singletonList(photoAR);
            String text = randomText();
            NewMessageBody newMessageBody = new NewMessageBody(text, attachmentRequests, null);
            SendMessageResult result = botAPI.sendMessage(newMessageBody).chatId(chat.getChatId()).execute();

            NewMessageBody editedMessageBody = new NewMessageBody("", null, null);
            String messageId = result.getMessage().getBody().getMid();
            botAPI.editMessage(editedMessageBody, messageId).execute();
            MessageBody lastMessage = getMessage(client, messageId).getBody();

            assertThat(lastMessage.getText().length(), is(0));
            compare(attachmentRequests, lastMessage.getAttachments());
        }
    }

    @Test
    public void cannotRemoveBothTextAndAttaches() throws Exception {
        for (Chat chat : chats) {
            List<AttachmentRequest> attachmentRequests = Collections.singletonList(photoAR);
            String text = randomText();
            NewMessageBody newMessageBody = new NewMessageBody(text, attachmentRequests, null);
            SendMessageResult result = botAPI.sendMessage(newMessageBody).chatId(chat.getChatId()).execute();

            NewMessageBody editedMessageBody = new NewMessageBody("", Collections.emptyList(), null);
            SimpleQueryResult queryResult = botAPI.editMessage(editedMessageBody,
                    result.getMessage().getBody().getMid()).execute();
            assertThat(queryResult.isSuccess(), is(false));
        }
    }

    @Test
    public void cannotRemoveTextWhenThereIsNoAttaches() throws Exception {
        for (Chat chat : chats) {
            String text = randomText();
            NewMessageBody newMessageBody = new NewMessageBody(text, null, null);
            SendMessageResult result = botAPI.sendMessage(newMessageBody).chatId(chat.getChatId()).execute();

            NewMessageBody editedMessageBody = new NewMessageBody("", null, null);
            SimpleQueryResult queryResult = botAPI.editMessage(editedMessageBody,
                    result.getMessage().getBody().getMid()).execute();
            assertThat(queryResult.isSuccess(), is(false));
        }
    }

    @Test
    public void cannotRemoveAttachesWhenThereIsNoText() throws Exception {
        for (Chat chat : chats) {
            NewMessageBody newMessageBody = new NewMessageBody("", Collections.singletonList(photoAR), null);
            SendMessageResult result = botAPI.sendMessage(newMessageBody).chatId(chat.getChatId()).execute();
            NewMessageBody editedMessageBody = new NewMessageBody("", Collections.emptyList(), null);
            SimpleQueryResult queryResult = botAPI.editMessage(editedMessageBody,
                    result.getMessage().getBody().getMid()).execute();
            assertThat(queryResult.isSuccess(), is(false));
        }
    }

    @Test
    public void shouldEditSingleAttachment() throws Exception {
        for (Chat chat : chats) {
            List<AttachmentRequest> attachmentRequests = Collections.singletonList(photoAR);
            String text = randomText();
            NewMessageBody newMessageBody = new NewMessageBody(text, attachmentRequests, null);
            SendMessageResult result = botAPI.sendMessage(newMessageBody).chatId(chat.getChatId()).execute();

            ContactAttachmentRequestPayload arPayload = new ContactAttachmentRequestPayload("test name")
                    .contactId(bot1.getUserId())
                    .vcfPhone("+79991234567");

            ContactAttachmentRequest contactAR = new ContactAttachmentRequest(arPayload);
            NewMessageBody editedMessageBody = new NewMessageBody(null, Collections.singletonList(contactAR), null);

            String messageId = result.getMessage().getBody().getMid();
            botAPI.editMessage(editedMessageBody, messageId).execute();
            MessageBody lastMessage = getMessage(client, messageId).getBody();

            assertThat(lastMessage.getText(), is(text));
            compare(Collections.singletonList(contactAR), lastMessage.getAttachments());
        }
    }

    @Test
    public void shouldEditMessageWithShareAttachment() throws Exception {
        ShareAttachmentPayload payload = new ShareAttachmentPayload();
        payload.url("https://max.ru");
        AttachmentRequest attach = new ShareAttachmentRequest(payload);
        NewMessageBody newMessage = new NewMessageBody(randomText(), Collections.singletonList(attach), null);
        List<Message> messages = send(newMessage, getChatsForSend());
        for (Message message : messages) {
            String messageId = message.getBody().getMid();
            Attachment attachment = message.getBody().getAttachments().get(0);
            attachment.visit(new FailByDefaultAttachmentVisitor() {
                @Override
                public void visit(ShareAttachment model) {
                    // send same attach by token
                    ShareAttachmentPayload payload = new ShareAttachmentPayload();
                    payload.token(model.getPayload().getToken());
                    AttachmentRequest shareAttach = new ShareAttachmentRequest(payload);
                    InlineKeyboardAttachmentRequestPayload kPayload = new InlineKeyboardAttachmentRequestPayload(
                            Collections.singletonList(
                                    Collections.singletonList(new CallbackButton("payload", randomText(10)))));
                    InlineKeyboardAttachmentRequest keyboardAttach = new InlineKeyboardAttachmentRequest(kPayload);
                    List<AttachmentRequest> attaches = Arrays.asList(shareAttach, keyboardAttach);
                    NewMessageBody newMessage = new NewMessageBody(null, attaches, null);
                    try {
                        new EditMessageQuery(client, newMessage, messageId).execute();
                        compare(client, messageId, newMessage, getMessage(client, messageId));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}