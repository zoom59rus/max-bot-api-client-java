package ru.max.botapi.queries;

import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import ru.max.botapi.MaxIntegrationTest;
import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.Callback;
import ru.max.botapi.model.CallbackAnswer;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ContactAttachmentRequest;
import ru.max.botapi.model.ContactAttachmentRequestPayload;
import ru.max.botapi.model.FailByDefaultUpdateVisitor;
import ru.max.botapi.model.InlineKeyboardAttachmentRequest;
import ru.max.botapi.model.InlineKeyboardAttachmentRequestPayload;
import ru.max.botapi.model.MessageBody;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.SendMessageResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class AnswerOnCallbackQueryIntegrationTest extends GetUpdatesIntegrationTest {
    @Test
    public void testInChat() throws Exception {
        shouldEditMessageOnAnswer(getByTitle(getChats(), "test chat #1"));
    }

    @Test
    public void testInChannel() throws Exception {
        shouldEditMessageOnAnswer(getByTitle(getChats(), "test channel #1"));
    }

    @Test
    public void testInDialog() throws Exception {
        CountDownLatch done = new CountDownLatch(1);
        FailByDefaultUpdateVisitor consumer = new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
            @Override
            public void visit(BotStartedUpdate model) {
                // ignore
            }

            @Override
            public void visit(MessageCallbackUpdate model) {
                // ignore
            }

            @Override
            public void visit(MessageCreatedUpdate model) {
                // bot 3 will reply to bot 1 that it has received `message_edited` update
                // will wait to finish test
                assertThat(model.getMessage().getSender().getUserId(), is(MaxIntegrationTest.bot3.getUserId()));
                done.countDown();
            }
        };

        try (AutoCloseable ignored = MaxIntegrationTest.bot1.addConsumer(BOT_1_BOT_3_DIALOG, consumer)) {
            shouldEditMessageOnAnswer(getChat(BOT_1_BOT_3_DIALOG));
            MaxIntegrationTest.await(done);
        }

    }

    private void shouldEditMessageOnAnswer(Chat chat) throws Exception {
        MaxIntegrationTest.bot3.startAnotherBot(MaxIntegrationTest.bot1.getUserId(), null);

        ArrayBlockingQueue<Callback> callbacks = new ArrayBlockingQueue<>(1);
        FailByDefaultUpdateVisitor consumer = new FailByDefaultUpdateVisitor(MaxIntegrationTest.bot1) {
            @Override
            public void visit(MessageCallbackUpdate model) {
                callbacks.add(model.getCallback());
            }

            @Override
            public void visit(BotStartedUpdate model) {
                assertThat(model.getUser().getUserId(), is(MaxIntegrationTest.bot3.getUserId()));
            }

            @Override
            public void visit(MessageCreatedUpdate model) {
                // ignore
            }
        };


        Long chatId = chat.getChatId();
        try (AutoCloseable ignored = MaxIntegrationTest.bot1.addConsumer(chatId, consumer)) {
            // bot1 send message with button
            String payload = MaxIntegrationTest.randomText(16);
            NewMessageBody body = originalMessage(payload);
            SendMessageResult result = botAPI.sendMessage(body).chatId(chatId).execute();
            String messageId = result.getMessage().getBody().getMid();

            // bot3 presses callback button
            MaxIntegrationTest.bot3.pressCallbackButton(messageId, payload);

            String editedText = randomText();
            ContactAttachmentRequestPayload arPayload = new ContactAttachmentRequestPayload(MaxIntegrationTest.randomText(16))
                    .contactId(MaxIntegrationTest.bot1.getUserId())
                    .vcfPhone("+79991234567");

            AttachmentRequest contactAR = new ContactAttachmentRequest(arPayload);
            NewMessageBody answerMessage = new NewMessageBody(editedText, Collections.singletonList(contactAR),
                    null);

            CallbackAnswer answer = new CallbackAnswer().message(answerMessage);
            Callback callback = callbacks.poll(10, TimeUnit.SECONDS);
            new AnswerOnCallbackQuery(MaxIntegrationTest.client, answer, callback.getCallbackId()).execute();

            MessageBody editedMessage = getMessage(MaxIntegrationTest.client, messageId).getBody();

            assertThat(editedMessage.getText(), is(editedText));
            MaxIntegrationTest.compare(Collections.singletonList(contactAR), editedMessage.getAttachments());
        }
    }

    @NotNull
    private static NewMessageBody originalMessage(String payload) {
        CallbackButton button = new CallbackButton(payload, "button text");
        InlineKeyboardAttachmentRequestPayload keyboardPayload = new InlineKeyboardAttachmentRequestPayload(
                Collections.singletonList(Collections.singletonList(button)));
        AttachmentRequest keyboardAttach = new InlineKeyboardAttachmentRequest(keyboardPayload);
        String text = "AnswerOnCallbackQueryIntegrationTest message " + MaxIntegrationTest.now();
        return new NewMessageBody(text, Collections.singletonList(keyboardAttach), null);
    }
}