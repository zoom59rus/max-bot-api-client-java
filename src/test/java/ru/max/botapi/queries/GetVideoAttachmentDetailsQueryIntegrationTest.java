package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.*;

import java.io.File;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetVideoAttachmentDetailsQueryIntegrationTest extends MaxIntegrationTest {
    @Test
    public void shouldReturnDetails() throws Exception {
        Chat chat = getByTitle(getChats(), "GetVideoAttachmentDetailsQueryIntegrationTest");
        String uploadUrl = getUploadUrl(UploadType.VIDEO);
        File file = new File(getClass().getClassLoader().getResource("test.mp4").toURI());
        UploadedInfo uploadedInfo = uploadAPI.uploadAV(uploadUrl, file).execute();
        AttachmentRequest attach = new VideoAttachmentRequest(uploadedInfo);
        NewMessageBody newMessage = new NewMessageBody(null, Collections.singletonList(attach), null);
        SendMessageResult result = doSend(newMessage, chat.getChatId());
        VideoAttachment videoAttachment = (VideoAttachment) result.getMessage().getBody().getAttachments().get(0);
        String token = videoAttachment.getPayload().getToken();
        VideoAttachmentDetails details = new GetVideoAttachmentDetailsQuery(client, token).execute();
        VideoUrls urls = details.getUrls();
        assertThat(urls, is(notNullValue()));
        assertThat(urls.getMp4480(), containsString("expires"));
        assertThat(urls.getMp4360(), containsString("expires"));
        assertThat(urls.getMp4240(), containsString("expires"));
        assertThat(details.getDuration(), is(14877));
        assertThat(details.getWidth(), is(852));
        assertThat(details.getHeight(), is(478));
    }

    @Test
    public void shouldThrowOnInvalidToken() throws Exception {
        new GetVideoAttachmentDetailsQuery(client, "invalidtoken").execute();
    }
}