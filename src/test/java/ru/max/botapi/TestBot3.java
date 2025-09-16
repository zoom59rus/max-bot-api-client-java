package ru.max.botapi;

import org.jetbrains.annotations.Nullable;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Chat;
import ru.max.botapi.model.ChatList;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.ReplyKeyboardAttachmentRequest;
import ru.max.botapi.queries.SendMessageQuery;

import java.util.Collections;
import java.util.Objects;


public class TestBot3 extends TestBot {
    private final Chat controlChat;
    private final MaxClient controlBot;

    public TestBot3(MaxClient bot3client, MaxClient controlBot, boolean isTravis) throws APIException, ClientException {
        super(bot3client, isTravis);
        this.controlBot = controlBot;
        this.controlChat = findControlChat();
    }

    public void pressCallbackButton(String messageId, String payload) throws APIException, ClientException {
        sendCommand(String.format("/press_callback_button %s %s", messageId, payload));
    }

    public void pressReplyButton(long chatId, ReplyKeyboardAttachmentRequest kbd) throws APIException, ClientException {
        NewMessageBody body = new NewMessageBody("/press_reply_button", Collections.singletonList(kbd), null);
        new SendMessageQuery(controlBot, body).chatId(chatId).disableLinkPreview(true).execute();
    }

    public void startYourself(long userId, @Nullable String payload) throws Exception {
        String command = String.format("/start_bot --session=%d %d", userId, getUserId());
        if (payload != null) {
            command += String.format(" \"%s\"", payload);
        }

        sendCommand(command);
    }

    public void startAnotherBot(long botId, @Nullable String payload) throws APIException, ClientException {
        String command = String.format("/start_bot %d", botId);
        if (payload != null) {
            command += String.format(" \"%s\"", payload);
        }

        sendCommand(command);
    }

    public void joinChat(String link) throws APIException, ClientException {
        sendCommand(String.format("/join_chat %s", Objects.requireNonNull(link, "link")));
    }

    public void leaveChat(Long chatId) throws APIException, ClientException {
        sendCommand(String.format("/leave_chat %d", chatId));
    }

    private void sendCommand(String command) throws APIException, ClientException {
        NewMessageBody body = new NewMessageBody(command, null, null);
        new SendMessageQuery(controlBot, body).chatId(controlChat.getChatId()).disableLinkPreview(true).execute();
    }

    private Chat findControlChat() throws APIException, ClientException {
        ChatList chatList = api.getChats().count(100).execute();
        return chatList.getChats().stream()
                .filter(c -> "test bot 3 control chat".equals(c.getTitle()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Control chat not found!"));
    }
}
