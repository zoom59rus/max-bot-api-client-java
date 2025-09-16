package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;


public class SendFileMessageQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldSendFile() throws Exception {
        UploadEndpoint uploadEndpoint = botAPI.getUploadUrl(UploadType.FILE).execute();
        String fileName = "test.txt";
        File file = new File(getClass().getClassLoader().getResource(fileName).toURI());
        UploadedInfo uploadedFileInfo = uploadAPI.uploadFile(uploadEndpoint.getUrl(), file).execute();
        AttachmentRequest request = new FileAttachmentRequest(uploadedFileInfo);
        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(request), null);
        List<Message> messages = send(newMessage);

        for (Message message : messages) {
            FileAttachment attachment = (FileAttachment) message.getBody().getAttachments().get(0);
            assertThat(attachment.getFilename(), is(fileName));
            assertThat(attachment.getSize(), is(greaterThan(0L)));
        }
    }

    @Test
    public void shouldSendAnyAccessibleFileUsingToken() throws Exception {
        UploadEndpoint uploadEndpoint = botAPI.getUploadUrl(UploadType.FILE).execute();
        File file = new File(getClass().getClassLoader().getResource("test.txt").toURI());
        UploadedInfo uploadedFileInfo = uploadAPI.uploadFile(uploadEndpoint.getUrl(), file).execute();
        AttachmentRequest request = new FileAttachmentRequest(uploadedFileInfo);
        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(request), null);
        List<Chat> chats = getChats();
        Chat chat = getByTitle(chats, "test chat #4");
        Chat channel = getByTitle(chats, "test channel #1");
        List<Chat> chatsToSend = Arrays.asList(chat, channel);

        List<Message> createdMessages = new GetMessagesQuery(client2).messageIds(
                send(newMessage, chatsToSend).stream().map(m -> m.getBody().getMid()).collect(
                        Collectors.toSet())).execute().getMessages();

        for (Message createdMessage : createdMessages) {
            FileAttachment attachment = (FileAttachment) createdMessage.getBody().getAttachments().get(0);
            FileAttachmentRequest copyAttach = new FileAttachmentRequest(
                    new UploadedInfo().token(attachment.getPayload().getToken()));

            List<Chat> client2Chats = getChats(client2);
            for (Chat c : Arrays.asList(/*getByType(client2Chats, ChatType.DIALOG),*/
                    getByTitle(client2Chats, "test chat #7"), getByTitle(client2Chats, "test channel #5"))) {

                doSend(client2, new NewMessageBody("resent with attach", Collections.singletonList(copyAttach), null),
                        c.getChatId());
            }
        }
    }

    private List<Message> send(NewMessageBody newMessage) throws Exception {
        return send(newMessage, getChatsForSend());
    }
}
