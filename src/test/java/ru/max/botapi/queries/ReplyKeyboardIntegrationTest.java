package ru.max.botapi.queries;

import org.junit.jupiter.api.Test;
import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class ReplyKeyboardIntegrationTest extends GetUpdatesIntegrationTest {
    @Test
    public void shouldSendReplyFromKeyboardInChat() throws Exception {
        test(getByTitle(getChats(), "ReplyKeyboardIntegrationTest#shouldSendReplyFromKeyboard"));
    }

    @Test
    public void shouldSendReplyFromKeyboardInDialog() throws Exception {
        test(getChat(MaxIntegrationTest.bot1.getUserId() ^ MaxIntegrationTest.bot3.getUserId()));
    }

    @Test
    public void shouldSendKeyboardDirectly() throws Exception {
        Chat chat = getByTitle(getChats(), "ReplyKeyboardIntegrationTest#shouldSendReplyFromKeyboard");
        String payload = MaxIntegrationTest.randomText(127);
        String text = randomText();
        SendMessageButton btn = new SendMessageButton(text).intent(Intent.POSITIVE);
        btn.payload(payload);
        List<List<ReplyButton>> buttons = Collections.singletonList(Collections.singletonList(btn));
        ReplyKeyboardAttachmentRequest kbd = new ReplyKeyboardAttachmentRequest(buttons);
        kbd.setDirectUserId(MaxIntegrationTest.bot2.getUserId());
        NewMessageBody body = new NewMessageBody("/press_reply_button", Collections.singletonList(kbd), null);

        CountDownLatch keyboardReceived = new CountDownLatch(1);
        MaxIntegrationTest.bot1.addConsumer(chat.getChatId(), new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1));
        MaxIntegrationTest.bot2.addConsumer(chat.getChatId(), new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot2) {
            @Override
            public void visit(MessageCreatedUpdate model) {
                assertThat(model.getMessage().getBody().getAttachments().get(0),
                        is(new ReplyKeyboardAttachment(buttons)));
                keyboardReceived.countDown();
            }
        });

        new SendMessageQuery(MaxIntegrationTest.client, body).chatId(chat.getChatId()).disableLinkPreview(true).execute();
        MaxIntegrationTest.await(keyboardReceived, 10);
    }

    @Test
    public void shouldSendKeyboardDirectlyByReply() throws Exception {
        Chat chat = getByTitle(getChats(), "ReplyKeyboardIntegrationTest#shouldSendReplyFromKeyboard");
        SendMessageResult bot2Message = doSend(MaxIntegrationTest.client2, new NewMessageBody("expect keyboard in reply", null, null),
                chat.getChatId());

        String payload = MaxIntegrationTest.randomText(127);
        String text = randomText();
        SendMessageButton btn = new SendMessageButton(text).intent(Intent.POSITIVE);
        btn.payload(payload);
        List<List<ReplyButton>> buttons = Collections.singletonList(Collections.singletonList(btn));
        ReplyKeyboardAttachmentRequest kbd = new ReplyKeyboardAttachmentRequest(buttons);
        kbd.setDirect(true);
        NewMessageLink link = new NewMessageLink(MessageLinkType.REPLY, bot2Message.getMessage().getBody().getMid());
        NewMessageBody body = new NewMessageBody("/press_reply_button", Collections.singletonList(kbd), link);

        CountDownLatch keyboardReceived = new CountDownLatch(1);
        MaxIntegrationTest.bot2.addConsumer(chat.getChatId(), new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
            @Override
            public void visit(MessageCreatedUpdate model) {
                assertThat(model.getMessage().getBody().getAttachments().get(0),
                        is(new ReplyKeyboardAttachment(buttons)));
                keyboardReceived.countDown();
            }
        });

        new SendMessageQuery(MaxIntegrationTest.client, body).chatId(chat.getChatId()).disableLinkPreview(true).execute();
        MaxIntegrationTest.await(keyboardReceived, 10);
    }

    @Test
    public void shouldThrowInChannel() throws Exception {
        Chat chat = getByType(getChats(), ChatType.CHANNEL);
        String payload = MaxIntegrationTest.randomText(127);
        String text = randomText();
        SendMessageButton btn = new SendMessageButton(text).intent(Intent.POSITIVE);
        btn.payload(payload);
        List<List<ReplyButton>> buttons = Collections.singletonList(Collections.singletonList(btn));
        ReplyKeyboardAttachmentRequest kbd = new ReplyKeyboardAttachmentRequest(buttons);
        NewMessageBody body = new NewMessageBody("/press_reply_button", Collections.singletonList(kbd), null);
        new SendMessageQuery(MaxIntegrationTest.client, body).chatId(chat.getChatId()).disableLinkPreview(true).execute();
    }

    @Test
    public void shouldThrowWhenUserNotFound() throws Exception {
        Chat chat = getByTitle(getChats(), "ReplyKeyboardIntegrationTest#shouldSendReplyFromKeyboard");
        String payload = MaxIntegrationTest.randomText(127);
        String text = randomText();
        SendMessageButton btn = new SendMessageButton(text).intent(Intent.POSITIVE);
        btn.payload(payload);
        List<List<ReplyButton>> buttons = Collections.singletonList(Collections.singletonList(btn));
        ReplyKeyboardAttachmentRequest kbd = new ReplyKeyboardAttachmentRequest(buttons);
        kbd.setDirectUserId(-123L);
        NewMessageBody body = new NewMessageBody("/press_reply_button", Collections.singletonList(kbd), null);
        new SendMessageQuery(MaxIntegrationTest.client, body).chatId(chat.getChatId()).disableLinkPreview(true).execute();
    }

    private void test(Chat chat) throws Exception {
        String payload = MaxIntegrationTest.randomText(127);
        String text = randomText();
        SendMessageButton btn = new SendMessageButton(text);
        btn.payload(payload);
        List<List<ReplyButton>> buttons = Collections.singletonList(Collections.singletonList(btn));
        AttachmentRequest kbd = new ReplyKeyboardAttachmentRequest(buttons);
        NewMessageBody body = new NewMessageBody("/press_reply_button", Collections.singletonList(kbd), null);

        CountDownLatch buttonPressed = new CountDownLatch(1);
        MaxIntegrationTest.bot1.addConsumer(chat.getChatId(), new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
            @Override
            public void visit(MessageCreatedUpdate model) {
                assertThat(model.getMessage().getBody().getText(), is(text));
                assertThat(model.getMessage().getBody().getAttachments().get(0), is(new DataAttachment(payload)));
                buttonPressed.countDown();
            }
        });

        new SendMessageQuery(MaxIntegrationTest.client, body).chatId(chat.getChatId()).disableLinkPreview(true).execute();
        MaxIntegrationTest.await(buttonPressed, 10);
    }
}
