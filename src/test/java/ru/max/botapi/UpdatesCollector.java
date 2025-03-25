package ru.max.botapi;

import java.util.ArrayList;
import java.util.List;

import ru.max.botapi.model.BotAddedToChatUpdate;
import ru.max.botapi.model.BotRemovedFromChatUpdate;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.ChatTitleChangedUpdate;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.MessageEditedUpdate;
import ru.max.botapi.model.MessageRemovedUpdate;
import ru.max.botapi.model.Update;
import ru.max.botapi.model.UserAddedToChatUpdate;
import ru.max.botapi.model.UserRemovedFromChatUpdate;


public class UpdatesCollector extends DelegatingUpdateVisitor {
    private final List<Update> updates = new ArrayList<>();

    public UpdatesCollector(Update.Visitor delegate) {
        super(delegate);
    }

    @Override
    public void visit(MessageCreatedUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visit(MessageCallbackUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visit(MessageEditedUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visit(MessageRemovedUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visit(BotAddedToChatUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visit(BotRemovedFromChatUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visit(UserAddedToChatUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visit(UserRemovedFromChatUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visit(BotStartedUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visit(ChatTitleChangedUpdate model) {
        super.visit(model);
        updates.add(model);
    }

    @Override
    public void visitDefault(Update model) {
        super.visitDefault(model);
        updates.add(model);
    }

    public List<Update> getUpdates() {
        return updates;
    }
}
