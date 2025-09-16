package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.*;

import java.util.Collections;
import java.util.List;


public class MessageWithShareAttachIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldSendMessageWithShareAttachment() throws Exception {
        ShareAttachmentPayload payload = new ShareAttachmentPayload();
        payload.url("https://max.ru/");
        AttachmentRequest attach = new ShareAttachmentRequest(payload);
        NewMessageBody newMessage = new NewMessageBody(randomText(), Collections.singletonList(attach), null);
        List<Message> messages = send(newMessage, getChatsForSend());
        Attachment attachment = messages.get(0).getBody().getAttachments().get(0);
        attachment.visit(new FailByDefaultAttachmentVisitor() {
            @Override
            public void visit(ShareAttachment model) {
                // send same attach by token
                ShareAttachmentPayload payload = new ShareAttachmentPayload();
                payload.token(model.getPayload().getToken());
                AttachmentRequest attach = new ShareAttachmentRequest(payload);
                NewMessageBody newMessage = new NewMessageBody(randomText(), Collections.singletonList(attach), null);
                try {
                    send(newMessage, getChatsForSend());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
