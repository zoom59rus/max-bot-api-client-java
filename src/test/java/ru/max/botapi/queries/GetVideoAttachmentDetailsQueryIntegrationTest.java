package ru.max.botapi.queries;

import java.io.File;
import java.util.Collections;

import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.exceptions.BadRequestException;
import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.SendMessageResult;
import ru.max.botapi.model.UploadType;
import ru.max.botapi.model.UploadedInfo;
import ru.max.botapi.model.VideoAttachment;
import ru.max.botapi.model.VideoAttachmentDetails;
import ru.max.botapi.model.VideoAttachmentRequest;
import ru.max.botapi.model.VideoUrls;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


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

    @Test(expected = BadRequestException.class)
    public void shouldThrowOnInvalidToken() throws Exception {
        new GetVideoAttachmentDetailsQuery(client, "invalidtoken").execute();
    }
}