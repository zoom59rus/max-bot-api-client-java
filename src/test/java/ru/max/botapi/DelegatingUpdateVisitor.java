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


public class DelegatingUpdateVisitor implements Update.Visitor {
    private final Update.Visitor delegate;

    public DelegatingUpdateVisitor(Update.Visitor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void visit(MessageChatCreatedUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(MessageCreatedUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(MessageCallbackUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(MessageEditedUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(MessageRemovedUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(BotAddedToChatUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(BotRemovedFromChatUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(UserAddedToChatUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(UserRemovedFromChatUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(BotStartedUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visit(ChatTitleChangedUpdate model) {
        delegate.visit(model);
    }

    @Override
    public void visitDefault(Update model) {
        delegate.visitDefault(model);
    }
}
