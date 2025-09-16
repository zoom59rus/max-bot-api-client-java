package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.model.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;


public class SendMessageAttachmentsIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shoudThrowExceptionWhenSendManyInlineKeyboards() throws Exception {
        List<List<Button>> buttons = Collections.singletonList(
                Collections.singletonList(new RequestGeoLocationButton("text")));
        InlineKeyboardAttachmentRequest keyboard = new InlineKeyboardAttachmentRequest(
                new InlineKeyboardAttachmentRequestPayload(buttons));
        List<AttachmentRequest> attaches = Arrays.asList(keyboard, keyboard);
        NewMessageBody newMessage = new NewMessageBody(null, attaches, null);
        sendAndVerify(newMessage);
    }

    @Test
    public void shoudThrowExceptionWhenSendManyFiles() throws Exception {
        AttachmentRequest file = new FileAttachmentRequest(new UploadedInfo().token("sometoken"));
        List<AttachmentRequest> attaches = Arrays.asList(file, file);
        NewMessageBody newMessage = new NewMessageBody(null, attaches, null);
        sendAndVerify(newMessage);
    }

    @Test
    public void shoudThrowExceptionWhenSendManyFilesWithKeyboard() throws Exception {
        AttachmentRequest file = new FileAttachmentRequest(new UploadedInfo().token("sometoken"));
        List<List<Button>> buttons = Collections.singletonList(
                Collections.singletonList(new RequestGeoLocationButton("text")));
        InlineKeyboardAttachmentRequest keyboard = new InlineKeyboardAttachmentRequest(
                new InlineKeyboardAttachmentRequestPayload(buttons));
        List<AttachmentRequest> attaches = Arrays.asList(file, file, keyboard);
        NewMessageBody newMessage = new NewMessageBody(null, attaches, null);
        sendAndVerify(newMessage);
    }

    @Test
    public void shoudSendOneFileWithKeyboard() throws Exception {
        UploadEndpoint uploadEndpoint = botAPI.getUploadUrl(UploadType.FILE).execute();
        String fileName = "test.txt";
        File file = new File(getClass().getClassLoader().getResource(fileName).toURI());
        UploadedInfo uploadedFileInfo = uploadAPI.uploadFile(uploadEndpoint.getUrl(), file).execute();
        AttachmentRequest fileAttachmentRequest = new FileAttachmentRequest(uploadedFileInfo);
        List<List<Button>> buttons = Collections.singletonList(
                Collections.singletonList(new RequestGeoLocationButton("text").quick(true)));
        InlineKeyboardAttachmentRequest keyboard = new InlineKeyboardAttachmentRequest(
                new InlineKeyboardAttachmentRequestPayload(buttons));
        List<AttachmentRequest> attaches = Arrays.asList(fileAttachmentRequest, keyboard);
        NewMessageBody newMessage = new NewMessageBody(null, attaches, null);
        send(newMessage, getChatsForSend());
    }

    private void sendAndVerify(NewMessageBody newMessageBody) throws Exception {
        int exceptions = 0;
        List<Chat> chats = getChatsCanSend();
        for (Chat chat : chats) {
            try {
                doSend(newMessageBody, chat.getChatId());
                fail(chat.getType() + " should fail");
            } catch (APIException e) {
                exceptions++;
            }
        }

        assertThat(exceptions, is(chats.size()));
    }
}
