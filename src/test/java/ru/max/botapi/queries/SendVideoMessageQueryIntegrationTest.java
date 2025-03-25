package ru.max.botapi.queries;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.Message;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.UploadEndpoint;
import ru.max.botapi.model.UploadType;
import ru.max.botapi.model.UploadedInfo;
import ru.max.botapi.model.VideoAttachment;
import ru.max.botapi.model.VideoAttachmentRequest;


public class SendVideoMessageQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldSendVideo() throws Exception {
        String uploadUrl = getUploadUrl(UploadType.VIDEO);
        File file = new File(getClass().getClassLoader().getResource("test.mp4").toURI());
        UploadedInfo uploadedInfo = uploadAPI.uploadAV(uploadUrl, file).execute();
        AttachmentRequest attach = new VideoAttachmentRequest(uploadedInfo);
        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(attach), null);
        send(newMessage, getChatsForSend());
    }

    @Test
    public void shouldSendAnyAccessibleVideoUsingToken() throws Exception {
        UploadEndpoint uploadEndpoint = botAPI.getUploadUrl(UploadType.VIDEO).execute();
        File videoFile = new File(getClass().getClassLoader().getResource("test.mp4").toURI());
        UploadedInfo uploadedInfo = uploadAPI.uploadAV(uploadEndpoint.getUrl(), videoFile).execute();
        AttachmentRequest request = new VideoAttachmentRequest(uploadedInfo);
        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(request), null);
        List<Chat> chats = getChats();
        Chat chat = getByTitle(chats, "test chat #4");
        Chat channel = getByTitle(chats, "test channel #1");
        List<Chat> chatsToSend = Arrays.asList(chat, channel);

        List<Message> createdMessages = new GetMessagesQuery(client2).messageIds(
                send(newMessage, chatsToSend).stream().map(m -> m.getBody().getMid()).collect(
                        Collectors.toSet())).execute().getMessages();

        for (Message createdMessage : createdMessages) {
            VideoAttachment attachment = (VideoAttachment) createdMessage.getBody().getAttachments().get(0);
            VideoAttachmentRequest copyAttach = new VideoAttachmentRequest(
                    new UploadedInfo().token(attachment.getPayload().getToken()));

            List<Chat> client2Chats = getChats(client2);
            for (Chat c : Arrays.asList(/*getByType(client2Chats, ChatType.DIALOG),*/
                    getByTitle(client2Chats, "test chat #7"), getByTitle(client2Chats, "test channel #5"))) {

                info("Sending to " + c.getChatId());
                doSend(client2, new NewMessageBody("resent with attach", Collections.singletonList(copyAttach), null),
                        c.getChatId());
            }
        }
    }
}
