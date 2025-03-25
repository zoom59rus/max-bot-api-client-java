package ru.max.botapi;

import ru.max.botapi.model.BotAddedToChatUpdate;
import ru.max.botapi.model.BotRemovedFromChatUpdate;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.ChatTitleChangedUpdate;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageChatCreatedUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.MessageEditedUpdate;
import ru.max.botapi.model.MessageRemovedUpdate;
import ru.max.botapi.model.Update;
import ru.max.botapi.model.UserAddedToChatUpdate;
import ru.max.botapi.model.UserRemovedFromChatUpdate;


public class GetChatId implements Update.Mapper<Long> {
    public static final Update.Mapper<Long> INSTANCE = new GetChatId();

    @Override
    public Long map(MessageCreatedUpdate model) {
        return model.getMessage().getRecipient().getChatId();
    }

    @Override
    public Long map(MessageCallbackUpdate model) {
        return model.getMessage().getRecipient().getChatId();
    }

    @Override
    public Long map(MessageEditedUpdate model) {
        return model.getMessage().getRecipient().getChatId();
    }

    @Override
    public Long map(MessageRemovedUpdate model) {
        return model.getChatId();
    }

    @Override
    public Long map(BotAddedToChatUpdate model) {
        return model.getChatId();
    }

    @Override
    public Long map(BotRemovedFromChatUpdate model) {
        return model.getChatId();
    }

    @Override
    public Long map(UserAddedToChatUpdate model) {
        return model.getChatId();
    }

    @Override
    public Long map(UserRemovedFromChatUpdate model) {
        return model.getChatId();
    }

    @Override
    public Long map(BotStartedUpdate model) {
        return model.getChatId();
    }

    @Override
    public Long map(ChatTitleChangedUpdate model) {
        return model.getChatId();
    }

    @Override
    public Long map(MessageChatCreatedUpdate model) {
        return model.getChat().getChatId();
    }

    @Override
    public Long mapDefault(Update model) {
        throw new UnsupportedOperationException("Chat is unknown");
    }
}
